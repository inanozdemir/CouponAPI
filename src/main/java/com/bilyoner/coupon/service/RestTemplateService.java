package com.bilyoner.coupon.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Slf4j
public class RestTemplateService {

    public static int getUsersBalance(long userId) {
        final String uri = "http://localhost:8082/api/userbalance/getbalance?userId=" + userId;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<Integer> entity = new HttpEntity<>(headers);

        ResponseEntity<Integer> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Integer.class);

        return response.getBody();
    }

    public static String buyCoupons(int userId, int amount) {
        try {
            final String uri = "http://localhost:8082/api/userbalance/buycoupon?userId=" + userId + "&amount=" + amount;
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            HttpEntity<Integer> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }

    public static String cancelCoupons(int userId, int amount) {
        try {
            final String uri = "http://localhost:8082/api/userbalance/cancelcoupon?userId=" + userId + "&amount=" + amount;
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            HttpEntity<Integer> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }

}
