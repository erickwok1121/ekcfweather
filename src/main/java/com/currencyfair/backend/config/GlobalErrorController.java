package com.currencyfair.backend.config;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import brave.Tracer;

@RestController
@RequestMapping("/error")
public class GlobalErrorController implements ErrorController {

	private static Logger logger = LoggerFactory.getLogger(GlobalErrorController.class);

	public static class ErrorResponse {

		private final Error error = new Error();

		public static class Error {

			private String traceId;

			private String errorCode;

			public String getTraceId() {
				return traceId;
			}

			public void setTraceId(String traceId) {
				this.traceId = traceId;
			}

			public String getErrorCode() {
				return errorCode;
			}

			public void setErrorCode(String errorCode) {
				this.errorCode = errorCode;
			}

		}

		public Error getError() {
			return error;
		}

	}

	@Autowired
	private Tracer tracer;

	@RequestMapping
	public ErrorResponse error(HttpServletRequest request, HttpServletResponse response) {

		String traceId = tracer.currentSpan().context().traceIdString();

		String endpoint = String.valueOf(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI));

		String statusCode = String.valueOf(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));

		Exception ex = ((Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));

		logger.error("Handling error. traceId: {}, endpoint: {}, statusCode: {}", traceId, endpoint, statusCode, ex);

		String errorCode;
		if ("404".equals(statusCode) || "403".equals(statusCode) || "401".equals(statusCode)
				|| ex instanceof AuthenticationException || ex instanceof AccessDeniedException) {

			response.setStatus(403);

			errorCode = "access_denied";

		} else {

			response.setStatus(500);

			errorCode = "unexpected_error";

		}

		ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.getError().setTraceId(traceId);
		errorResponse.getError().setErrorCode(errorCode);

		return errorResponse;
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}