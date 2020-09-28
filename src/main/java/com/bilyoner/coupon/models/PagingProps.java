package com.bilyoner.coupon.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PagingProps {
    public int page;
    public int size;
    public String sort;
    public boolean isAsc;
}
