package com.bilyoner.coupon.controllers;

import com.bilyoner.coupon.models.PagingProps;
import com.bilyoner.coupon.entities.Event;
import com.bilyoner.coupon.service.EventService;
import com.bilyoner.coupon.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping({"/api/event"})
public class EventController {

    @Autowired
    private EventService eventService;

    @RequestMapping(value = "/getAll", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getAll(@RequestBody PagingProps pagingProps) throws Exception {
        Pageable pageable = PagingUtil.generatePageableObj(pagingProps);
        return eventService.findAll(pageable);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Event findOne(@PathVariable("id") Long id) {
        return eventService.findById(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Event create(@RequestBody Event event) {
        return eventService.create(event);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Event update(@RequestBody Event event) {
        return eventService.update(event);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Event delete(@PathVariable("id") Long id) {
        return eventService.delete(id);
    }

}
