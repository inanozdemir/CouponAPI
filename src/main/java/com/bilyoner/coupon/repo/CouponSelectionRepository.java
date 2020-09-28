package com.bilyoner.coupon.repo;

import com.bilyoner.coupon.entities.CouponSelection;
import org.springframework.data.repository.Repository;

import java.util.List;


public interface CouponSelectionRepository extends Repository<CouponSelection, Long> {

    void delete(CouponSelection couponSelection);

    List<CouponSelection> findAll();

    CouponSelection findById(Long id);

    CouponSelection save(CouponSelection couponSelection);
}
