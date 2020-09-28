package com.bilyoner.coupon.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@EqualsAndHashCode
public class Coupon {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "status", nullable = false)
    private int status;

    @Basic
    @Column(name = "name", nullable = false)
    private int cost;

    @Basic
    @Column(name = "play_date")
    private Timestamp playdate;

    @Basic
    @Column(name = "create_date")
    private Timestamp createDate;

    @Basic
    @Column(name = "update_date")
    private Timestamp updateDate;

    @Transient
    private List<CouponSelection> couponSelections;

}
