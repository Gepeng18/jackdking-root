package org.jackdking.limiter.aspect;



import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jackdking.limiter.anoation.AccessLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class AccessLimiterAspect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private DefaultRedisScript<Boolean> limitUserAccessLua;

    // �����
    @Pointcut("@annotation(org.jackdking.limiter.anoation.AccessLimiter)")
    public void cut() {
        System.out.println("cut");
    }

    // ֪ͨ�����ӵ�
    @Before("cut()")
    public void before(JoinPoint joinPoint) throws Exception {

        // ��ȡ��ִ�еķ���
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // ͨ��������ȡ��ע��
        AccessLimiter annotation = method.getAnnotation(AccessLimiter.class);
        // ��� annotation==null��˵��������û������AccessLimiter,˵������Ҫ��������
        if (annotation == null) {
            return;
        }
        // ��ȡ����Ӧ��ע�����
        String key = annotation.key();
        Integer limit = annotation.limit();
        Integer timeout = annotation.timeout();

        // ���keyΪ��
        if (StringUtils.isEmpty(key)) {
            String name = method.getDeclaringClass().getName();
            // ֱ�Ӱѵ�ǰ�ķ���������key
            key = name+"#"+method.getName();
            // ��ȡ�����еĲ����б�

            //ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
            //String[] parameterNames = pnd.getParameterNames(method);

            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                System.out.println(parameterType);
            }

            // ��������в�������ô�Ͱ�key���� = ��������#����������
            if (parameterTypes != null) {
                String paramTypes = Arrays.stream(parameterTypes)
                        .map(Class::getName)
                        .collect(Collectors.joining(","));
                key = key +"#" + paramTypes;
            }
        }

        // ����key�ǵ��б�
        List<String> keysList = new ArrayList<>();
        keysList.add(key);
        // ִ��ִ��lua�ű�����
        Boolean accessFlag = stringRedisTemplate.execute(limitUserAccessLua, keysList, limit.toString(), timeout.toString());
        // �жϵ�ǰִ�еĽ���������0�������ƣ�1��������
        if (!accessFlag) {
            throw new Exception("server is busy!!!");
        }
    }
}
