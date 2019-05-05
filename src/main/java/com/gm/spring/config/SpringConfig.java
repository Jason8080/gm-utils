package com.gm.spring.config;

import com.gm.spring.InstanceFactory;
import com.gm.spring.SpringInstanceProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Spring config.
 *
 * @author Jason
 */
@Configuration
public class SpringConfig {
    /**
     * 初始化GM InstanceFactory.
     *
     * @param context the context
     * @return the spring instance provider
     */
    @Bean
    public SpringInstanceProvider instanceFactory(ApplicationContext context) {
        SpringInstanceProvider provider = new SpringInstanceProvider(context);
        InstanceFactory.setInstanceProvider(provider);
        return provider;
    }
}
