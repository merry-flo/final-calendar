package com.example.calendar.api.config;

import com.example.calendar.api.dto.AuthUser;
import com.example.calendar.api.service.LoginService;
import com.example.calendar.core.exception.CalendarException;
import com.example.calendar.core.exception.ErrorCode;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserAuthArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return AuthUser.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Long userId = (Long) webRequest.getAttribute(LoginService.LOGIN_USER_KEY, NativeWebRequest.SCOPE_SESSION);
        if (userId == null) {
            throw new CalendarException(ErrorCode.NO_SESSION);
        }
        return AuthUser.of(userId);
    }
}
