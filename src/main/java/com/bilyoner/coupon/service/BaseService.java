package com.bilyoner.coupon.service;


import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService <T>{

    T create(T pojo);

    T delete(Long id);

    List<T> findAll();

    List<T> findAll(Pageable pageable);

    T findById(Long id);

    T update(T pojo);
}
