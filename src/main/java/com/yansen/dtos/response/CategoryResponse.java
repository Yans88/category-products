package com.yansen.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {

    private Long id;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("image_url")
    private String img;

}
