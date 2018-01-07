package com.example.easy.commons.helper;

import java.util.Collection;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.example.easy.commons.component.Messages;
import com.example.easy.commons.model.ErrorResponse;
import com.example.easy.commons.model.IBaseDTO;

@Scope(value = "singleton")
@Component
public class RestResponseBuilder {
	

	@Autowired
	protected Messages messages;

	private Status status;
	private String message;
	private String details;
	private Integer code;
	private IBaseDTO response;
	private Collection<?> responseList;
	private Page<?> responsePaged;
	
	//singlton bean inject it
//	protected RestResponseBuilder() {}
//	
//	public static RestResponseBuilder INSTANCE() {
//		return new RestResponseBuilder();
//	}

	private void clearState() {
		this.status = null;
		this.message = null;
		this.details = null;
		this.code = null;
		this.response = null;
		this.responseList = null;
		this.responsePaged = null;
	}
	

	public RestResponseBuilder withStatus(final Status status) {
		this.status = status;
		this.code = status.getStatusCode();
		return this;
	}

	public RestResponseBuilder withMessage(final String message) {
		this.message = message;
		return this;
	}

	public RestResponseBuilder withDetails(final String details) {
		this.details = details;
		return this;
	}

	public RestResponseBuilder withOkResponse(final Collection<?> responseList) {
		clearState();
		this.responseList = responseList;
		/*if(CollectionUtils.isEmpty(responseList)) {
			//message = "no record found.";
			//return with404Response();
		}*/
		return withOkResponse();
	}
	
	public RestResponseBuilder withOkResponse(final Page<?> responsePaged) {
		clearState();
		this.responsePaged = responsePaged;
		
		return withOkResponse();
	}

	public RestResponseBuilder withOkResponse(final IBaseDTO response) {
		clearState();
		this.response = response;
		return withOkResponse();
	}

	public RestResponseBuilder withOkResponse() {
		this.status = Response.Status.OK;
		this.code = status.getStatusCode();
		return this;
	}
	
	public RestResponseBuilder with404Response() {
		this.status = Response.Status.NOT_FOUND;
		this.code = status.getStatusCode();
		return this;
	}

	public RestResponseBuilder withInternalServerError(final String details) {
		clearState();
		this.details = details;
		this.status = Response.Status.INTERNAL_SERVER_ERROR;
		this.message = messages.get("server.error.check.details");
		this.code = status.getStatusCode();
		return this;
	}

	public RestResponseBuilder withBadRequest(final String message)
	{
		return withBadRequest(message, message);
	}
	
	public RestResponseBuilder withBadRequest(final String message, final String details) {
		clearState();
		if (null == message) {
			this.details = messages.get("bad.request.msg");
			this.message = messages.get("bad.request.msg");
		} else {
			this.message = message;
			this.details = details;
		}
		this.status = Response.Status.BAD_REQUEST;
		this.code = status.getStatusCode();
		return this;
	}

	public Response build() {
		if (this.status == Response.Status.OK) {
			if (null != response) {
				return Response.status(status).entity(response).build();
			} else if (!CollectionUtils.isEmpty(responseList)) {
				return Response.status(status).entity(responseList).build();
			} else if (null!=responsePaged && responsePaged.hasContent()) {
				return Response.status(status).entity(responsePaged).build();
			} else if (null != message) {
				return Response.status(status).entity(message).build();
			} else {
				return Response.status(status).build();
			}
		} else {
			ErrorResponse responseObject = new ErrorResponse();
			if (message != null) {
				responseObject.setMessage(message);
			}
			if (details != null) {
				responseObject.setDetails(details);
			}
			if (code != null) {
				responseObject.setCode(code);
			}
			return Response.status(status).entity(responseObject).build();
		}
	}

}
