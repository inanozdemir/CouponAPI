package com.bilyoner.coupon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@SpringBootApplication
@EntityScan(basePackages = {"com.bilyoner.coupon.entities"})
@EnableJpaRepositories(basePackages = {"com.bilyoner.coupon.repo"})
@ComponentScan("com.bilyoner.coupon")
public class CouponApiApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CouponApiApplication.class, args);
    }

}