package com.kaizensundays.fusion.eureka;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created: Saturday 10/2/2021, 2:14 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@EnableEurekaServer
@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        new SpringApplicationBuilder(Main.class).build().run(args);

    }

}
