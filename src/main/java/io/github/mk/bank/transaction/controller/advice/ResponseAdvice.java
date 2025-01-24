package io.github.mk.bank.transaction.controller.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

@RestControllerAdvice
public class ResponseAdvice extends AbstractMappingJacksonResponseBodyAdvice {
  @Override
  protected void beforeBodyWriteInternal(
      MappingJacksonValue bodyContainer,
      MediaType contentType,
      MethodParameter returnType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
    Object value = bodyContainer.getValue();
    if (!(value instanceof Response)) {
      bodyContainer.setValue(new Response<>("200", value, null));
    }
  }
}
