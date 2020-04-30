package com.grinnotech.patients.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

    private final AsyncProperties properties;

	public AsyncConfiguration(AsyncProperties properties) {
		this.properties = properties;
	}

	@Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        if (this.properties.getCorePoolSize() != null) {
            executor.setCorePoolSize(this.properties.getCorePoolSize());
        }

        if (this.properties.getMaxPoolSize() != null) {
            executor.setMaxPoolSize(this.properties.getMaxPoolSize());
        }

        if (this.properties.getQueueCapacity() != null) {
            executor.setQueueCapacity(this.properties.getQueueCapacity());
        }

        if (this.properties.getThreadNamePrefix() != null) {
            executor.setThreadNamePrefix(this.properties.getThreadNamePrefix());
        }

        if (this.properties.getAllowCoreThreadTimeOut() != null) {
            executor.setAllowCoreThreadTimeOut(
                    this.properties.getAllowCoreThreadTimeOut());
        }

        if (this.properties.getWaitForTasksToCompleteOnShutdown() != null) {
            executor.setWaitForTasksToCompleteOnShutdown(
                    this.properties.getWaitForTasksToCompleteOnShutdown());
        }

        if (this.properties.getAwaitTerminationSeconds() != null) {
            executor.setAwaitTerminationSeconds(
                    this.properties.getAwaitTerminationSeconds());
        }

        if (this.properties.getKeepAliveSeconds() != null) {
            executor.setKeepAliveSeconds(this.properties.getKeepAliveSeconds());
        }

        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

}
