package com.lyflexi.samplefirstlevel;

import org.lyflexi.firstlevel_robotstarter.RobotAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(RobotAutoConfiguration.class)
@SpringBootApplication
public class SampleFirstlevelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleFirstlevelApplication.class, args);
    }

}
