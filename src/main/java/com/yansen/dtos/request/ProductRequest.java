package com.yansen.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {


    @ApiModelProperty(notes = "product name", example = "Java")
    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("id_category")
    private Long idCategory;

    private String description;

    private Integer qty;

}
