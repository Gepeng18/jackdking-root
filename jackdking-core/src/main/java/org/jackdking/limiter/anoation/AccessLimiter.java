package org.jackdking.limiter.anoation;

import org.jackdking.limiter.enums.LimiterType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimiter {

    LimiterType limitType() default LimiterType.COUNT;

    // ������ @AccessLimiter(limit="1",timeout="1",key="user:ip:limit")
    // ������һ���û�key��timeoutʱ���ڣ�������limit��
    // �����key
    String key() default "";
    // ���ƵĴ���
    int limit() default  1;
    // ����ʱ��
    int timeout() default  1;
    // �������� ��������
    int step() default 1;
    // ����Ͱ����
    int rate() default 0;
    // Ͱ����
    int capacity() default 1;
    // ©Ͱ©������
    int leakRate() default 0;
}
