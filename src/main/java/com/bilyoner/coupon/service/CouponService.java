package com.bilyoner.coupon.service;

import com.bilyoner.coupon.cache.CouponCache;
import com.bilyoner.coupon.cache.EventCache;
import com.bilyoner.coupon.entities.Coupon;
import com.bilyoner.coupon.entities.CouponSelection;
import com.bilyoner.coupon.entities.Event;
import com.bilyoner.coupon.models.CouponStatus;
import com.bilyoner.coupon.models.EventStatus;
import com.bilyoner.coupon.models.EventTypes;
import com.bilyoner.coupon.repo.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class CouponService implements BaseService<Coupon> {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private CouponSelectionService couponSelectionService;

    @Autowired
    private CouponCache couponCache;

    @Autowired
    private EventCache eventCache;


    @Override
    public Coupon create(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public Coupon delete(Long id) {
        Coupon event = findById(id);
        if (Objects.nonNull(event)) {
            couponRepository.delete(event);
        }
        return event;
    }

    @Override
    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    public List<Coupon> findAllOrderByStatusAsc() {
        return couponRepository.findByOrderByStatusAsc();
    }

    public List<Coupon> findByUserId(int userId) {
        return couponRepository.findByUserId(userId);
    }

    @Override
    public List<Coupon> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Coupon findById(Long id) {
        return couponRepository.findById(id);
    }

    @Override
    public Coupon update(Coupon coupon) {
        couponRepository.save(coupon);
        return coupon;
    }

    /////////////////////////////////////////////////////////////////////////////
    public void createCoupons() throws Exception {
        int totalCouponCount = 0;
        Map<Long, Event> events = eventCache.getCache().asMap();
        //for (int i = 0; i < events.size(); i++) {
        for (int i = 0; i < 3; i++) {
            int count = combineEvents(events.values().toArray(), i + 1);
            totalCouponCount = totalCouponCount + count;
            log.info("****************** Total count : " + totalCouponCount + "- for K : " + (i + 1) + " = " + count);
        }
    }

    public int combineEvents(Object[] elements, int K) {
        int N = elements.length;

        if (K > N) {
            System.out.println("Invalid input, K > N");
            return 0;
        }
        // init combination index array
        int combination[] = new int[K];

        int r = 0; // index for event list
        int i = 0; // index for elements array
        int count = 0; // combination count for current params
        while (r >= 0) {

            // forward step if i < (N + (r-K))
            if (i <= (N + (r - K))) {
                combination[r] = i;

                // if combination array is full, control rules and save to DB and cache then increment i;
                if (r == K - 1) {
                    controlAndSaveCoupon(elements, combination);
                    count++;
                    log.info("Combination : " + count + " for " + K + " events...");
                    i++;
                } else {
                    // if combination is not full, select next element
                    i = combination[r] + 1;
                    r++;
                }
            } else {
                // backward step
                r--;
                if (r >= 0)
                    i = combination[r] + 1;

            }
        }
        return count;
    }

    private void controlAndSaveCoupon(Object[] elements, int[] combinations) {
        boolean hasEventTypeFootball = false;
        boolean hasEventTypeTennis = false;
        int eventCount = elements.length;

        List<CouponSelection> couponSelections = new ArrayList<>();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (int eventId : combinations) {
            Event event = (Event) elements[eventId];

            // Control RULE : EventDate can't be bigger then now
            if (event.getEventDate().getTime() < now.getTime()) {
                return;
            }

            // Control RULE : mbs and coupon event count
            if (event.getMbs() > eventCount) {
                return;
            }

            // check if event is Football
            if (event.getType() == EventTypes.Football.getValue()) {
                hasEventTypeFootball = true;
            }

            // check if event is Tennis
            if (event.getType() == EventTypes.Tennis.getValue()) {
                hasEventTypeTennis = true;
            }

            // Control RULE : Football and Tennis event can't be at the same coupon
            if (hasEventTypeFootball && hasEventTypeTennis) {
                return;
            }

            // Create valid couponSelection
            CouponSelection couponSelection = new CouponSelection();
            couponSelection.setEventId(event.getId());
            couponSelection.setEventStatus(EventStatus.Open.getValue());
            couponSelection.setCreateDate(now);

            // add selection to list
            couponSelections.add(couponSelection);
        }

        // Create coupon
        Coupon coupon = new Coupon();
        coupon.setCost(5);
        coupon.setCreateDate(now);
        coupon.setStatus(CouponStatus.Open.getValue());

        // Save to Coupon to DB
        coupon = couponRepository.save(coupon);

        // Save to CouponSelections to DB
        for (CouponSelection cs : couponSelections) {
            cs.setCouponId(coupon.getId());
            cs = couponSelectionService.create(cs);
        }

        coupon.setCouponSelections(couponSelections);

        // add to cache
        couponCache.getCache().put(coupon.getId(), coupon);
    }

}
