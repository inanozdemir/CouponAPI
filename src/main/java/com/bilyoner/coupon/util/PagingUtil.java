package com.bilyoner.coupon.util;

import com.bilyoner.coupon.models.PagingProps;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

public class PagingUtil {

    public static Pageable generatePageableObj(@RequestBody PagingProps pagingProps) throws Exception {
        if(Objects.isNull(pagingProps) || Objects.isNull(pagingProps.page) || Objects.isNull(pagingProps.size) || pagingProps.size > 50){
            throw new Exception("Paging Property Error...");
        }

        Sort sort;
        if (pagingProps.isAsc) {
            sort = Sort.by(pagingProps.sort).ascending();
        } else {
            sort = Sort.by(pagingProps.sort).descending();
        }

        return PageRequest.of(pagingProps.page, pagingProps.size, sort);
    }
}
