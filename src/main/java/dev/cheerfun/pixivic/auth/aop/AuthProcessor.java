package dev.cheerfun.pixivic.auth.aop;

import dev.cheerfun.pixivic.auth.annotation.AuthRequired;
import dev.cheerfun.pixivic.auth.util.JWTUtil;
import dev.cheerfun.pixivic.common.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2019/07/15 22:24
 * @description aop权限校验处理
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthProcessor {
    private final JWTUtil jwtUtil;

    @Pointcut(value = "@annotation(dev.cheerfun.pixivic.auth.annotation.AuthRequired)||@within(dev.cheerfun.pixivic.auth.annotation.AuthRequired)")
    public void pointCut() {
    }

    @Before(value = "pointCut()")
    public void handleAuthority(JoinPoint joinPoint) throws Exception {
         String token = null;
        MethodSignature signature = ((MethodSignature) joinPoint.getSignature());
        Method method = signature.getMethod();
        //取出token
        Object[] args = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        assert args.length == parameterAnnotations.length;
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (!(annotation instanceof RequestHeader)) {
                    continue;
                }
                RequestHeader requestHeader = (RequestHeader) annotation;
                if (!"Authorization".equals(requestHeader.value())) {
                    continue;
                }
                token = (String) args[argIndex];
            }
        }
        /*进行jwt校验，成功则将User放进ThreadLocal（token即将过期则将刷新后的token放入ThreadLocal）
        过期则抛出自定义未授权过期异常*/
        User user = jwtUtil.validateToken(token);
        //获取具体权限级别
        AuthRequired methodAuthRequired = AnnotationUtils.findAnnotation(method, AuthRequired.class);
        AuthRequired classAuthRequired = AnnotationUtils.findAnnotation(method.getDeclaringClass(), AuthRequired.class);
        int authLevel = methodAuthRequired != null ? methodAuthRequired.value() : classAuthRequired.value();
        /*System.out.println(annotation.roleName());*/
    }
}
