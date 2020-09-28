package com.bilyoner.coupon.cache;

import com.bilyoner.coupon.entities.Coupon;
import com.bilyoner.coupon.repo.CouponRepository;
import jersey.repackaged.com.google.common.cache.Cache;
import jersey.repackaged.com.google.common.cache.CacheBuilder;
import jersey.repackaged.com.google.common.cache.CacheLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class CouponCache {

    @Autowired
    private CouponRepository couponRepository;

    private Cache<Long, Coupon> cache;

    public CouponCache() {
        cache = CacheBuilder.newBuilder()

                .recordStats()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<Long, Coupon>() {
                    @Override
                    public Coupon load(Long id) throws Exception {
                        return load(id);
                    }
                });
    }

    private synchronized Coupon load(Long id) {
        return couponRepository.findById(id);
    }

    public Cache<Long, Coupon> getCache() {
        return cache;
    }

    @PostConstruct
    private void fillCouponCache() {
        for (Coupon coupon : couponRepository.findAll()) {
            cache.put(coupon.getId(), coupon);
        }
    }
}