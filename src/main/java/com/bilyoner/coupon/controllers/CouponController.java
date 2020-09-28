package com.bilyoner.coupon.controllers;

import com.bilyoner.coupon.cache.CouponCache;
import com.bilyoner.coupon.entities.Coupon;
import com.bilyoner.coupon.service.CouponService;
import com.bilyoner.coupon.service.RestTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping({"/api/coupon"})
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponCache couponCache;

    @RequestMapping(value = "/generate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateCouponsFromEvents() {
        try {
            log.info("Coupon generation has started...");
            couponService.createCoupons();
            log.info("Coupon generation finished...");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object[]> listCoupons() {
        try {
            return ResponseEntity.ok(couponCache.getCache().asMap().values().toArray());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/list/byStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listCouponsByStatus() {
        try {
            return ResponseEntity.ok(couponService.findAllOrderByStatusAsc());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/list/userCoupons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listCouponsByUser(@RequestParam int userId) {
        try {
            return ResponseEntity.ok(couponService.findByUserId(userId));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/buy", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity buyCoupons(@RequestParam int userId, @RequestBody List<Long> couponIds) {
        List<Coupon> couponsBought = new ArrayList<>();
        try {
            // prepare available coupons
            int couponsValue = controlAvailableCoupons(couponIds, couponsBought);

            // Control usersBalance
            Integer balance = RestTemplateService.getUsersBalance(userId);

            if (balance < couponsValue) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("User's Balance is not enough !");
            }

            buyCouponsDbOperations(userId, couponsBought, couponsValue);

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.status(HttpStatus.OK).body(couponsBought);
    }

    private void buyCouponsDbOperations(int userId, List<Coupon> couponsBought, int couponsValue) {
        for (Coupon coupon : couponsBought) {
            // update userId to coupon and save to DB and update cache
            coupon.setUserId(userId);
            coupon.setPlaydate(new Timestamp(System.currentTimeMillis()));
            couponService.update(coupon);

            // update cache
            couponCache.getCache().put(coupon.getId(), coupon);
        }
        // buy coupon rest call
        RestTemplateService.buyCoupons(userId, couponsValue);
    }

    private int controlAvailableCoupons(List<Long> couponIds, List<Coupon> couponsBought) {
        int couponsValue = 0;
        for (long id : couponIds) {
            Coupon coupon = couponCache.getCache().asMap().get(id);

            if (Objects.isNull(coupon))
                continue;

            // Control RULE : only one user can buy the coupon
            if (Objects.nonNull(coupon.getUserId())) {
                continue;
            }

            // There maybe more Rules later

            // Calculate all coupons cost
            couponsValue = couponsValue + coupon.getCost();

            couponsBought.add(coupon);
        }
        return couponsValue;
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity cancelCoupon(@RequestParam int userId, @RequestParam("couponId") Long couponId) {
        try {
            Coupon coupon = couponCache.getCache().asMap().get(couponId);

            // Control RULE : coupon can be canceled in 5 mins after played
            long playDateMilis = coupon.getPlaydate().getTime();
            long nowMilis = System.currentTimeMillis();

            long diff = nowMilis - playDateMilis;
            long diffMinutes = diff / (60 * 1000);

            if (diffMinutes > 5)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Coupon cancel time is over !");

            // update userId to coupon and save to DB and update cache
            coupon.setUserId(null);
            coupon.setPlaydate(null);
            couponService.update(coupon);

            // update cache
            couponCache.getCache().put(coupon.getId(), coupon);

            // cancel coupon rest call
            RestTemplateService.cancelCoupons(userId, coupon.getCost());

        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
