package com.learn.spring.restful.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import com.learn.spring.restful.entity.User;
import com.learn.spring.restful.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver{

    private final UserRepository userRepository;
    
    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return User.class.equals(parameter.getParameterType());
    }

    @Override
    @Nullable
    public Object resolveArgument(@NonNull MethodParameter arg0, @Nullable ModelAndViewContainer arg1,@NonNull NativeWebRequest arg2,
            @Nullable WebDataBinderFactory arg3) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) arg2.getNativeRequest();
        String token = servletRequest.getHeader("X-API-TOKEN");

        if(token == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        User user = userRepository.findFirstByToken(token)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        if (user.getTokenExpiredAt() < System.currentTimeMillis()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return user;
    }
}
