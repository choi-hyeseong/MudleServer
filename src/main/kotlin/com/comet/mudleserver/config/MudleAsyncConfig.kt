package com.comet.mudleserver.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import kotlin.math.max

@EnableAsync
@Configuration
class MudleAsyncConfig : AsyncConfigurer {

    override fun getAsyncExecutor(): Executor? {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 2
            maxPoolSize = 10
            queueCapacity = 500
            setThreadNamePrefix("mudle-async-")
            initialize()
        }
    }
}