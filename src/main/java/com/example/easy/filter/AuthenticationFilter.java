package com.example.easy.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.security.Key;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.easy.commons.annotations.InSecure;
import com.example.easy.commons.model.UserInfo;
import com.example.easy.commons.security.JWTTokenUtil;

/**
 * This filter verify the access permissions for a user based on Token provided
 * in request
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	final boolean useSecurity = false;

	@Context
	private ResourceInfo resourceInfo;

	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Bearer";
	private static final Response ACCESS_DENIED_UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED)
			.type(MediaType.TEXT_PLAIN).entity("UnAuthorized to access this resource").build();
	private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
			.type(MediaType.TEXT_PLAIN).entity("Access blocked !!").build();

	@Override
	public void filter(ContainerRequestContext requestContext) {
		Class<?> resourceClass = resourceInfo.getResourceClass();

		Method resourceMethod = resourceInfo.getResourceMethod();

		Annotation[] classRoles = resourceClass.getAnnotations();

		for (Annotation annotation : classRoles) {

			logger.trace(annotation.toString());
		}
		// Get the resource method which matches with the requested URL
		// Extract the roles declared by it Method resourceMethod =
		// resourceInfo.getResourceMethod();

		Annotation[] methodRoles = resourceMethod.getAnnotations();

		logger.trace("" + resourceMethod.isAnnotationPresent(InSecure.class));

		for (Annotation annotation : methodRoles) {

			logger.trace(annotation.toString());
		}

		logger.info("AuthenticationFilter called for resourceClass::" + resourceClass + "resourceMethod::"
				+ resourceMethod + " requestContext::" + requestContext.getMethod());

		String httpMethod = requestContext.getMethod();
		String requestPath = requestContext.getUriInfo().getPath();
		// Access allowed for all
		if (!resourceClass.isAnnotationPresent(PermitAll.class)) {
			if (resourceMethod.isAnnotationPresent(InSecure.class)) {
				logger.debug("Insecure Method Skip it" + httpMethod + " And requestPath::" + requestPath);
			} else if (httpMethod.equals("OPTIONS")
					|| (httpMethod.equals("GET")) && requestPath.endsWith("swagger.json")) {
				// Skip Swagger Call
				logger.debug("Skiping as httpMethod is " + httpMethod + " And requestPath::" + requestPath);
			} else {
				try {
					// Access denied for all
					if (resourceClass.isAnnotationPresent(DenyAll.class)) {
						logger.debug("Method Restricted::");
						// throw new NotAuthorizedException("Method
						// Restricted.");
						requestContext.abortWith(ACCESS_FORBIDDEN);
					}
					
					String authToken = null;
					Key key = null;

					// Get request headers
					if (useSecurity) {

						// Get the HTTP Authorization header from the request
						String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

						// Check if the HTTP Authorization header is present and formatted correctly
						if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
							logger.debug("Authorization header must be provided");
							requestContext.abortWith(ACCESS_DENIED_UNAUTHORIZED);
						}

						// Extract the token from the HTTP Authorization header

						authToken = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim(); // validateToken(token);

						Integer accId = null;
						Integer userId = null;

						String username = null;

						if (authToken == null) {
							logger.debug("Authorization header provided without token");
							requestContext.abortWith(ACCESS_DENIED_UNAUTHORIZED);
						}

						logger.info("Verifying JWT Token...");
						key = JWTTokenUtil.getSigningKey();
						if (!JWTTokenUtil.isValid(authToken, key)) {
							logger.debug("invalid auth token");
							// requestContext.abortWith(ACCESS_DENIED_UNAUTHORIZED);
							throw new Exception("Invalid auth token");
						} else {
							userId = JWTTokenUtil.getUserId(authToken, key);
							accId = JWTTokenUtil.getAccountId(authToken, key);
						}
						/*
						 * 1. Load User+Account Details using Auth Token from AccountUsersession Table
						 * [select using auth Token where expire is false] 2. If token is valid check
						 * its expiry-time its it is expired then ????
						 */

						// Verify user access is method is annotated with roles
						if (resourceMethod.isAnnotationPresent(RolesAllowed.class)) {
							RolesAllowed rolesAnnotation = resourceMethod.getAnnotation(RolesAllowed.class);
							Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

							// load user role details

							List<String> userRoles = JWTTokenUtil.getRoles(authToken, key);
							if (userRoles == null) {
								logger.debug("No Role defined for this user.");
								requestContext.abortWith(ACCESS_FORBIDDEN);

							} else {
								boolean userHasAccess = false;
								// Check Is user has access to method role?
								for (String usrRole : userRoles) {
									if (rolesSet.contains(usrRole.toLowerCase())) {
										userHasAccess = true;
										break;
									}
								}

								if (!userHasAccess) {
									logger.debug("User does not have access to this method.");
									requestContext.abortWith(ACCESS_FORBIDDEN);
								}
							}
						}
					}

					logger.info("Setting customSecurityContext");
					requestContext.setSecurityContext(new SecurityContext() {
						@Override
						public Principal getUserPrincipal() {
							if (useSecurity) {
								return new UserInfo("", JWTTokenUtil.getAccountId(authToken, key),
										JWTTokenUtil.getUserId(authToken, key));
							} else {
								return new UserInfo("tester", 115, 100);
							}
						}

						@Override
						public boolean isUserInRole(String role) {
							return role.equals(role);
						}

						@Override
						public boolean isSecure() {
							// return false;
							return requestContext.getSecurityContext().isSecure();
						}

						@Override
						public String getAuthenticationScheme() {
							// return "custom";
							return requestContext.getSecurityContext().getAuthenticationScheme();
						}
					});

				} catch (Exception exp) {
					requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
				}
			}
		}
	}

}