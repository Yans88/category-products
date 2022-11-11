package com.yansen.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {

    private Long id;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("image_url")
    private String img;

    @JsonProperty("id_category")
    private Long idCategory;

    @JsonProperty("category_name")
    private String categoryName;

    private String description;

    private Integer qty;

}
