package com.bilyoner.coupon.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "event")
@Getter
@Setter
@EqualsAndHashCode
public class Event {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Basic
    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Basic
    @Column(name = "mbs", nullable = false)
    private int mbs;

    @Basic
    @Column(name = "type", nullable = false)
    private int type;

    @Basic
    @Column(name = "event_date", nullable = false)
    private Timestamp eventDate;

}
