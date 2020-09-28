package com.bilyoner.coupon.service;

import com.bilyoner.coupon.entities.Event;
import com.bilyoner.coupon.repo.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class EventService implements BaseService<Event> {

    @Autowired
    private EventRepository repository;

    @Override
    public Event create(Event event) {
        return repository.save(event);
    }

    @Override
    public Event delete(Long id) {
        Event event = findById(id);
        if (Objects.nonNull(event)) {
            repository.delete(event);
        }
        return event;
    }

    @Override
    public List<Event> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Event> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Event findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Event update(Event event) {
        Event event_check = findById(event.getId());
        if (Objects.nonNull(event_check)) {
            repository.save(event);
        }
        return event;
    }

}
