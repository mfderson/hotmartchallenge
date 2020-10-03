package com.hotmartchalenge.marketplace.api.exceptionHandlers;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.hotmartchalenge.marketplace.domain.exceptions.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
  public static final String GENERIC_MSG_ERROR_FINAL_USER =
      "An unexpected internal system error has occurred. "
          + "Try again and if the problem persists, contact your system administrator.";

  @Autowired private MessageSource messageSource;

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<?> handleEntityNotFoundException(
      EntityNotFoundException ex, WebRequest request) {

    HttpStatus status = HttpStatus.NOT_FOUND;
    ErrorType errorType = ErrorType.RESOURCE_NOT_FOUND;
    String detail = ex.getMessage();

    ErrorMessage errorMessage =
        createErrorMessageBuilder(status, errorType, detail).userMessage(detail).build();

    return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    ErrorType errorType = ErrorType.INVALID_DATA;
    String detail = "One or more fields are invalid, fill correctly and try again.";

    BindingResult bindingResult = ex.getBindingResult();

    List<ErrorMessage.Object> errorMessageObjects =
        bindingResult.getAllErrors().stream()
            .map(
                objectError -> {
                  String message =
                      messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                  String name = objectError.getObjectName();

                  if (objectError instanceof FieldError) {
                    name = ((FieldError) objectError).getField();
                  }

                  return ErrorMessage.Object.builder().name(name).userMessage(message).build();
                })
            .collect(Collectors.toList());

    ErrorMessage errorMessage =
        createErrorMessageBuilder(status, errorType, detail)
            .objects(errorMessageObjects)
            .userMessage(detail)
            .build();

    return handleExceptionInternal(ex, errorMessage, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    Throwable rootCause = ExceptionUtils.getRootCause(ex);

    if (rootCause instanceof InvalidFormatException)
      return handleInvalidFormatException(
          (InvalidFormatException) rootCause, headers, status, request);
    else if (rootCause instanceof PropertyBindingException)
      return handlePropertyBindingException(
          (PropertyBindingException) rootCause, headers, status, request);

    ErrorType errorType = ErrorType.JSON_SYNTAX_ERROR;
    String detail = "There is a sintax error in JSON";

    ErrorMessage errorMessage =
        createErrorMessageBuilder(status, errorType, detail).userMessage(detail).build();

    return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), status, request);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

    if (body == null) {
      body =
          ErrorMessage.builder()
              .timestamp(LocalDateTime.now())
              .title(status.getReasonPhrase())
              .status(status.value())
              .userMessage(GENERIC_MSG_ERROR_FINAL_USER)
              .build();
    } else if (body instanceof String) {
      body =
          ErrorMessage.builder()
              .timestamp(LocalDateTime.now())
              .title((String) body)
              .status(status.value())
              .userMessage(GENERIC_MSG_ERROR_FINAL_USER)
              .build();
    }

    return super.handleExceptionInternal(ex, body, headers, status, request);
  }

  private ResponseEntity<Object> handlePropertyBindingException(
      PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    String path = joinPath(ex.getPath());

    ErrorType errorType = ErrorType.JSON_SYNTAX_ERROR;
    String detail = String.format("There isn't no property '%s'. ", path);

    ErrorMessage errorMessage =
        createErrorMessageBuilder(status, errorType, detail).userMessage(detail).build();

    return handleExceptionInternal(ex, errorMessage, headers, status, request);
  }

  private ResponseEntity<Object> handleInvalidFormatException(
      InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    String path = joinPath(ex.getPath());

    ErrorType errorType = ErrorType.JSON_SYNTAX_ERROR;
    String detail =
        String.format(
            "Property '%s' has received '%s'. Enter a value of type %s",
            path, ex.getValue(), ex.getTargetType().getSimpleName());

    ErrorMessage errorMessage =
        createErrorMessageBuilder(status, errorType, detail).userMessage(detail).build();

    return handleExceptionInternal(ex, errorMessage, headers, status, request);
  }

  private ErrorMessage.ErrorMessageBuilder createErrorMessageBuilder(
      HttpStatus status, ErrorType errorType, String detail) {

    return ErrorMessage.builder()
        .timestamp(LocalDateTime.now())
        .status(status.value())
        .title(errorType.getTitle())
        .detail(detail);
  }

  private String joinPath(List<Reference> references) {
    return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
  }
}
