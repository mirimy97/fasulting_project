package com.fasulting.domain.user.review.dto.reqDto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@ToString
public class ReviewReqDto {

    private Long consultingSeq;

    private Long userSeq;

    private String reviewContent;

    private BigDecimal point;

}
