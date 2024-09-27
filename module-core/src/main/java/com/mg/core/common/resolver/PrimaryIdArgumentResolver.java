package com.mg.core.common.resolver;

import com.mg.core.common.annotation.PrimaryId;
import com.mg.core.dto.mg.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class PrimaryIdArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * PrimaryId 어노테이션이 선언되어 있고, String 타입인 파라미터에 한해서만 동작
     * ex) @PrimaryId String id
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasPrimaryIdAnnotation = parameter.hasParameterAnnotation(PrimaryId.class);
        boolean isStringType = String.class.isAssignableFrom(parameter.getParameterType());
        return hasPrimaryIdAnnotation && isStringType;
    }

    /**
     * supportsParameter 메서드가 true를 반환할 경우에만 동작
     * resolveArgument의 반환값은 @PrimaryId가 선언된 파라미터에 자동으로 주입
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDTO userDTO = (UserDTO) authentication.getPrincipal();
            return userDTO.getUid();
        }
        return null;
    }
}
