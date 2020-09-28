package com.bilyoner.coupon.repo;

import com.bilyoner.coupon.entities.Event;
import org.springframework.data.repository.Repository;

import java.util.List;


public interface EventRepository extends Repository<Event, Long> {

    void delete(Event event);

    List<Event> findAll();

    Event findById(Long id);

    Event save(Event event);

    //List<Event> findByOrderByMbsAscAndTypeAsc();

}
