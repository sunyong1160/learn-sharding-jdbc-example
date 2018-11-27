package learn.sharding.jdbc.example.aspect;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import learn.sharding.jdbc.example.annotation.Desc;
import learn.sharding.jdbc.example.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;

/**
 * Created by sunyong on 2018-11-20.
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    @Autowired
    ObjectMapper mapper;

    private final static StringBuffer ignoreParams = new StringBuffer();

    static {
        ignoreParams.append("org.apache.catalina.connector.RequestFacade;");
        ignoreParams.append("org.springframework.cloud.sleuth.instrument.web.TraceHttpServletResponse;");
        ignoreParams.append("org.springframework.validation.BeanPropertyBindingResult;");
    }

    /**
     * 定义切入点
     */
    @Pointcut("execution(* learn.sharding.jdbc.example.controller.*.*(..))")
    public void log() {

    }

    /**
     * 前置通知
     */
    @Before("log()")
    public void logBefore(JoinPoint joinPoint) throws JsonProcessingException {
        //log.info("请求时间:" + DateUtil.format(new Date()));
        //log.info("目标方法名为:" + joinPoint.getSignature().getName());
        //log.info("目标方法所属类的简单类名:" + joinPoint.getSignature().getDeclaringType().getSimpleName());
        //log.info("目标方法所属类的类名:" + joinPoint.getSignature().getDeclaringTypeName());
        //log.info("目标方法声明类型:" + Modifier.toString(joinPoint.getSignature().getModifiers()));
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        getAnnotation(joinPoint);
        // 请求的方法参数名称
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                Class argClazz = args[i] != null ? args[i].getClass() : null;
                if (argClazz == null) {
                    continue;
                }
                boolean isContain = ignoreParams.toString().contains(argClazz.getName());
                if (!isContain) {
                    if (args[i] instanceof Serializable) {
                        //writeValueAsString需要对象可序列化
                        //log.info("请求参数:" + paramNames[i] + "=:" + mapper.writeValueAsString(args[i]));
                    } else {
                        //log.info("请求参数:" + paramNames[i] + "=" + args[i]);
                    }
                }
            }
        }
    }

    @AfterReturning(returning = "ret", pointcut = "log()")
    public void doAfterReturning(Object ret) {
        //log.info("返回结果:" + JSONObject.toJSONString(ret));
    }

    public void getAnnotation(JoinPoint joinPoint) {
        Method method = getTargetMethod(joinPoint);
        if (method.isAnnotationPresent(Desc.class)) {// 判断某个方法上是否使用了某个注解
            String value = method.getAnnotation(Desc.class).value();// 获取注解上的内容
            //System.out.println(value);// 可以做一些日志记录入库或者限流等其他操作
        }
    }

    public static Method getTargetMethod(JoinPoint joinPoint) {
        Method targetMethod = null;
        try {
            String methodName = joinPoint.getSignature().getName();
            Method[] methodArr = joinPoint.getSignature().getDeclaringType().getMethods();
            for (Method method : methodArr) {
                if (method.getName().equals(methodName)) {
                    targetMethod = method;
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return targetMethod;
    }
}
