package com.yansen.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class CategoryResponse {
    @Value("${my.hostname}")
    private String hostname;

    private Long id;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("image_url")
    private String img;

}
