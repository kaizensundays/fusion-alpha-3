package com.kaizensundays.fusion.mu

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

/**
 * Created: Sunday 11/21/2021, 1:06 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@SpringBootApplication
@ComponentScan(useDefaultFilters = false)
@Import(ServiceContext::class)
class Main

fun main(args: Array<String>) {

    SpringApplicationBuilder(Main::class.java).build().run(*args)
}
