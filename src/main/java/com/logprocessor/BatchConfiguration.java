package com.logprocessor;

import org.aspectj.lang.annotation.Pointcut;
import org.hsqldb.util.DatabaseManagerSwing;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 
 * @author Niraj Sonawane
 *
 *Configuration class for configuring beans. Uncomment @PostConstruct to view Data 
 */
@Configuration
public class BatchConfiguration {

	   

	//@PostConstruct
	public void getDbManager() {
		System.setProperty("java.awt.headless", "false");
		DatabaseManagerSwing.main(
				new String[] { "--url", "jdbc:hsqldb:file:db/log-messagees-db", "--user", "sa", "--password", "" });
	}
	
	 @Bean
	    public ThreadPoolTaskExecutor taskExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(10);
	        executor.setMaxPoolSize(10);
	        executor.setQueueCapacity(500);
	        executor.setThreadNamePrefix("Database Insert");
	        executor.initialize();
	        return executor;
	    }

	@Pointcut("execution(public void com.logprocessor.LogFileProcessor.processLogFile(..))")
	public void monitor() {
	}

	@Bean
	public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
		return new PerformanceMonitorInterceptor(true);
	}

	@Bean
	public Advisor performanceMonitorAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("com.logprocessor.BatchConfiguration.monitor()");
		return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
	}

	

}
