package com.lyflexi.samplesecondlevel;

import org.lyflexi.secondlevel_robotstarter.annotation.EnableRobot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRobot
public class SampleSecondlevelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleSecondlevelApplication.class, args);
    }

}
