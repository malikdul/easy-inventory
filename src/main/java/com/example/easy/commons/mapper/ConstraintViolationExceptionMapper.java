package com.example.easy.commons.mapper;

import javax.annotation.Priority;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.USER)
public class ConstraintViolationExceptionMapper implements ExceptionMapper<javax.validation.ValidationException>
{

	@Override
	public Response toResponse(javax.validation.ValidationException e)
	{
		final StringBuilder strBuilder = new StringBuilder();
		for (ConstraintViolation<?> cv : ((ConstraintViolationException) e).getConstraintViolations())
		{
			if (strBuilder.length() > 0)
			{
				strBuilder.append(", ").append(cv.getPropertyPath().toString() + " " + cv.getMessage());
			} else
			{
				strBuilder.append(cv.getPropertyPath().toString() + " " + cv.getMessage());
			}
		}
		return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).type(MediaType.TEXT_PLAIN).entity(strBuilder.toString()).build();
	}
}