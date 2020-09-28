package com.bilyoner.coupon.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "coupon_selection")
@Getter
@Setter
@EqualsAndHashCode
public class CouponSelection {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Basic
    @Column(name = "coupon_id", nullable = false)
    private long couponId;

    @Basic
    @Column(name = "event_id", nullable = false)
    private long eventId;

    @Basic
    @Column(name = "event_status", nullable = false)
    private int eventStatus;

    @Basic
    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

}
