package com.iot.ota_web.config;

import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置tomcat,禁用不安全http方法
 * @author 唐亮
 *
 */
@Configuration
public class TomcatConfig {
  
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcatServletContainerFactory = new TomcatEmbeddedServletContainerFactory();
        tomcatServletContainerFactory.addContextCustomizers(new TomcatContextCustomizer(){

            @Override
            public void customize(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                SecurityCollection collection = new SecurityCollection();
                //http方法 
                collection.addMethod("PUT");
                collection.addMethod("DELETE");
                collection.addMethod("HEAD");
                collection.addMethod("OPTIONS");
                collection.addMethod("TRACE");
                //url匹配表达式 
                collection.addPattern("/*");
                constraint.addCollection(collection);
                constraint.setAuthConstraint(true);
                context.addConstraint(constraint );
                
                //设置使用httpOnly
                context.setUseHttpOnly(true);
            }
        });
        return tomcatServletContainerFactory;
    }
}  
