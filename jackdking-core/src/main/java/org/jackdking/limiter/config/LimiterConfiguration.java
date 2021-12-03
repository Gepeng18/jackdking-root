package org.jackdking.limiter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class LimiterConfiguration {

    @Bean
    public DefaultRedisScript<Boolean> limitUserAccessLua() {
        // ��ʼ��һ��lua�ű��Ķ���DefaultRedisScript
        DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        // ͨ���������ȥ����lua�ű���λ�� ClassPathResource��ȡ��·���µ�lua�ű�
        // ClassPathResource ʲô����·����������maven����õ�target/classesĿ¼
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("org/jackdking/limiter/config/countLimit.lua")));
        // lua�ű����յķ���ֵ �������ַ��ء�1/0
        defaultRedisScript.setResultType(Boolean.class);
        return defaultRedisScript;
    }
}
