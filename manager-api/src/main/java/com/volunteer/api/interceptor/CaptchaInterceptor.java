package com.volunteer.api.interceptor;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.volunteer.api.config.EndpointsRequiringCaptchaConfig;
import com.volunteer.api.service.CaptchaService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CaptchaInterceptor implements HandlerInterceptor {

  private final CaptchaService captchaService;

  @Value("${captcha.enable:false}")
  private boolean enableCaptcha;

  private final EndpointsRequiringCaptchaConfig config;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    if (enableCaptcha && captchaRequired(request, handler)) {
      captchaService.validateCaptchaResponse(request);
    }

    return true;
  }

  protected boolean captchaRequired(HttpServletRequest request, Object handler) {
    if (handler instanceof HandlerMethod) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      String httpMethod = request.getMethod();
      Method controllerMethod = handlerMethod.getMethod();
      String controllerMethodName = controllerMethod.getName();
      String controllerClassName = controllerMethod.getDeclaringClass().getSimpleName();

      String currentEndpoint =
          String.format("%s %s.%s", httpMethod, controllerClassName, controllerMethodName);
      return config.getEndpoints().contains(currentEndpoint);
    }
    return false;
  }
}
