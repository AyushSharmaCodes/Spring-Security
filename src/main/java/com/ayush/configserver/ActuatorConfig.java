package com.ayush.configserver;

import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorConfig {

    //HttpTraceRepository has been removed from Spring 3.x+
    //to use it downgrade to 2.6.x
    //or use micrometer tracing (Spring boot 3.x+)
    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository(); // Stores recent HTTP traces in memory
    }


    @Bean
    AuditEventRepository auditeventrepository(){
        return new InMemoryAuditEventRepository();
    }
}
