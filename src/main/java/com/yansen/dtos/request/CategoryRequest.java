package com.yansen.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @ApiModelProperty(notes = "id, jika ingin update silahkan diisi dengan id data yang akan di update", example = "1")
    private Long id;

    @ApiModelProperty(notes = "category set", example = "Java")
    @JsonProperty("category_name")
    private String categoryName;

}
