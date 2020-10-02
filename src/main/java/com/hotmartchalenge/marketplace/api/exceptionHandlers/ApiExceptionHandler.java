package com.hotmartchalenge.marketplace.api.exceptionHandlers;

import java.time.LocalDateTime;

import com.hotmartchalenge.marketplace.domain.exceptions.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
  public static final String GENERIC_MSG_ERROR_FINAL_USER = "An unexpected internal system error has occurred. "
    + "Try again and if the problem persists, contact your system administrator.";

  @Autowired
  private MessageSource messageSource;

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<?> handleEntityNotFoundException(
    EntityNotFoundException ex, WebRequest request) {

    HttpStatus status = HttpStatus.NOT_FOUND;
    ErrorType errorType = ErrorType.RESOURCE_NOT_FOUND;
    String detail = ex.getMessage();

    ErrorMessage errorMessage = createErrorMessageBuilder(status, errorType, detail)
      .userMessage(detail)
      .build();

    return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), status, request);
  }

  @Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (body == null) {
			body = ErrorMessage.builder()
				.timestamp(LocalDateTime.now())
				.title(status.getReasonPhrase())
				.status(status.value())
				.userMessage(GENERIC_MSG_ERROR_FINAL_USER)
				.build();
		} else if (body instanceof String) {
			body = ErrorMessage.builder()
				.timestamp(LocalDateTime.now())
				.title((String) body)
				.status(status.value())
				.userMessage(GENERIC_MSG_ERROR_FINAL_USER)
				.build();
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

  private ErrorMessage.ErrorMessageBuilder createErrorMessageBuilder(HttpStatus status,
    ErrorType errorType, String detail) {
    
    return ErrorMessage.builder()
      .timestamp(LocalDateTime.now())
      .status(status.value())
      .title(errorType.getTitle())
      .detail(detail);
  }
}
