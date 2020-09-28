package com.bilyoner.coupon.cache;

import com.bilyoner.coupon.entities.Event;
import com.bilyoner.coupon.repo.EventRepository;
import jersey.repackaged.com.google.common.cache.Cache;
import jersey.repackaged.com.google.common.cache.CacheBuilder;
import jersey.repackaged.com.google.common.cache.CacheLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class EventCache {

    @Autowired
    private EventRepository eventRepository;

    private Cache<Long, Event> cache;

    public EventCache() {
        cache = CacheBuilder.newBuilder()
                .recordStats()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<Long, Event>() {
                    @Override
                    public Event load(Long id) throws Exception {
                        return load(id);
                    }
                });
    }

    private synchronized Event load(Long id) {
        return eventRepository.findById(id);
    }

    public Cache<Long, Event> getCache() {
        return cache;
    }

    @PostConstruct
    private void fillEventCache() {
        for (Event event : eventRepository.findAll()) {
            cache.put(event.getId(), event);
        }
    }

}