package com.luna.warmteaandhonestreviews.dto;

import java.util.List;

public record GetReviewsRespDto(List<ReviewDto> reviews,
                                Long total,
                                Integer page,
                                Integer offset) {

}
