package com.yansen.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableResponse<T> {

    private String message;

    private Integer status;

    private T data;

    @JsonProperty("current_page")
    private Integer currentPage;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("page_size")
    private Integer pageSize;

    private Boolean next;

    private Boolean previous;

    @JsonProperty("total_data")
    private Long totalData;


}
