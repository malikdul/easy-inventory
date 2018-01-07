package com.example.easy.commons.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.example.easy.commons.constants.AppConstant;
import com.example.easy.commons.helper.Utility;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class JWTTokenUtil {

	private final static Logger logger = LoggerFactory.getLogger(JWTTokenUtil.class);

	@Value("${jwt.secret.key}")
	static String jwtSecretKey = "MIIEowIBAAKCAQEApVp4tLNM30TTuMNpChR90VgYkpgUpeyI3E8wMgJUR0d+atKd+/WyTyQaH2kvQIZmrBWipdTMYj7Xb0uInctsU0m64aXs+nPWInjb3g";

	public static String getUserAuthJWTString(Integer userId, Collection<String> roles, Integer accountId,
			Date expires) {
		return getUserAuthJWTString(userId, roles, accountId, expires, getSigningKey());
	}

	public static String getUserAuthJWTString(Integer userId, Collection<String> roles, int accountId, Date expires,
			Key key) {
		// Issue a token (can be a random String persisted to a database or a
		// JWT token)
		// The issued token must be associated to a user
		// Return the issued token
		logger.debug("Get User Auth JWTString::" + userId + "accountId::" + accountId);
		if (userId == null) {
			throw new NullPointerException("null username is illegal");
		}
		if (roles == null) {
			throw new NullPointerException("null roles are illegal");
		}
		if (expires == null) {
			throw new NullPointerException("null expires is illegal");
		}
		if (key == null) {
			throw new NullPointerException("null key is illegal");
		}

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		/*
		 * Map claims = new HashMap<>(); claims.put("accountId", 1);
		 * claims.put("eventID", 105);
		 */
		String jwtString = Jwts.builder().setIssuer(AppConstant.JWT_ISSUER).setSubject(String.valueOf(userId))
				.setAudience(StringUtils.join(roles, ",")).setExpiration(expires)
				// .setClaims(claims)
				.setIssuedAt(new Date()).setId(String.valueOf(accountId)).signWith(signatureAlgorithm, key).compact();
		return jwtString;
	}

	public static Key getSigningKey() {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		// We will sign our JWT with our ApiKey secret

		byte[] message = jwtSecretKey.getBytes(StandardCharsets.UTF_8);
		String encoded = Base64.getEncoder().encodeToString(message);

		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(encoded);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		return signingKey;
	}

	public static boolean isValid(String token, Key key) {
		try {
			Jwts.parser().setSigningKey(key).parseClaimsJws(token.trim());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Integer getUserId(String jwsToken, Key key) {
		if (isValid(jwsToken, key)) {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
			// return claimsJws.getBody().getSubject();
			return Integer.parseInt(claimsJws.getBody().getSubject());
		}
		return null;
	}

	public static List<String> getRoles(String jwsToken, Key key) {
		if (isValid(jwsToken, key)) {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
			return Arrays.asList(claimsJws.getBody().getAudience().split(","));
		}
		// return new String[]{};
		return null;
	}

	public static int getAccountId(String jwsToken, Key key) {
		if (isValid(jwsToken, key)) {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
			return Integer.parseInt(claimsJws.getBody().getId());
		}
		return -1;
	}

	public static void main(String[] args) {
		String roloes[] = { "12admin", "sadmin", "user" };
		String tok = JWTTokenUtil.getUserAuthJWTString(1, Arrays.asList(roloes), 1, Utility.getCurrentDate());
		System.out.println(tok);
		// tok += "1";
		System.out.println(JWTTokenUtil.isValid(tok, JWTTokenUtil.getSigningKey()));
	}
}