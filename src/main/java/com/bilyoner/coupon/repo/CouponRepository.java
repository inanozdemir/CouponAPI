package com.bilyoner.coupon.repo;

import com.bilyoner.coupon.entities.Coupon;
import org.springframework.data.repository.Repository;

import java.util.List;


public interface CouponRepository extends Repository<Coupon, Long> {

    void delete(Coupon coupon);

    List<Coupon> findAll();

    List<Coupon> findByOrderByStatusAsc();

    List<Coupon> findByUserId(int userId);

    Coupon findById(Long id);

    Coupon save(Coupon coupon);

}
