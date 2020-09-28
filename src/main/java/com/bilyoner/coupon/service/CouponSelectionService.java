package com.bilyoner.coupon.service;

import com.bilyoner.coupon.entities.CouponSelection;
import com.bilyoner.coupon.repo.CouponSelectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CouponSelectionService implements BaseService<CouponSelection> {

    @Autowired
    private CouponSelectionRepository repository;

    @Override
    public CouponSelection create(CouponSelection couponSelection) {
        return repository.save(couponSelection);
    }

    @Override
    public CouponSelection delete(Long id) {
        CouponSelection couponSelection = findById(id);
        if (Objects.nonNull(couponSelection)) {
            repository.delete(couponSelection);
        }
        return couponSelection;
    }

    @Override
    public List<CouponSelection> findAll() {
        return repository.findAll();
    }

    @Override
    public List<CouponSelection> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public CouponSelection findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public CouponSelection update(CouponSelection couponSelection) {
        CouponSelection couponSelection_check = findById(couponSelection.getId());
        if (Objects.nonNull(couponSelection_check)) {
            repository.save(couponSelection);
        }
        return couponSelection;
    }

}
