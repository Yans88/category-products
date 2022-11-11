package com.yansen.controllers;

import com.yansen.dtos.request.CategoryRequest;
import com.yansen.dtos.response.CategoryResponse;
import com.yansen.dtos.response.CommonResponse;
import com.yansen.dtos.response.PageableResponse;
import com.yansen.dtos.response.ResponseGenerator;
import com.yansen.services.CategoryService;
import com.yansen.services.ProductsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

@RestController
@RequestMapping(value = "/api/master_category")
@Api(description = "Test-Dayalima", tags = "Data Category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductsService productService;


    @GetMapping
    @ApiOperation(value = "Get all Category", notes = "Get all Category", nickname = "Get all Category")
    public ResponseEntity<PageableResponse<CategoryResponse>> getAllData(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "categoryName", required = false) String sort_column,
            @RequestParam(defaultValue = "ASC", required = false) String sort_order
    ) {
        PageableResponse response = categoryService.getData(page, size, keyword, sort_column, sort_order);
        response.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Add data Category", notes = "Add data Category", nickname = "Add data Category")
    public ResponseEntity<CommonResponse<CategoryResponse>> addData(
            @RequestParam(required = true) String category_name,
            @RequestPart(value = "", required = false) MultipartFile img) throws FileNotFoundException {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setCategoryName(category_name);

        CategoryResponse response = categoryService.saveData(img, categoryRequest, 0L);
        ResponseGenerator responseGenerator = new ResponseGenerator();
        return new ResponseEntity<>(responseGenerator.responseData(HttpStatus.CREATED.value(), "ok", response), HttpStatus.CREATED);
    }


    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Edit data Category", notes = "Edit data Category", nickname = "Edit data Category")
    public ResponseEntity<CommonResponse<CategoryResponse>> editData(
            @RequestParam(required = true) String id,
            @RequestParam(required = true) String category_name,
            @RequestPart(required = false, name = "img") MultipartFile img) throws FileNotFoundException {

        //cek type data id is numeric true or false
        //id for update data
        productService.isNumeric(id, "id");

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setCategoryName(category_name);

        CategoryResponse response = categoryService.saveData(img, categoryRequest, Long.valueOf(id));
        ResponseGenerator responseGenerator = new ResponseGenerator();
        return new ResponseEntity<>(responseGenerator.responseData(HttpStatus.ACCEPTED.value(), "ok", response), HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete data Category", notes = "Delete data Category", nickname = "Delete data Category")
    public ResponseEntity<CommonResponse> removeOne(@PathVariable String id) {
        productService.isNumeric(id, "id");
        categoryService.removeOne(Long.valueOf(id));
        ResponseGenerator responseGenerator = new ResponseGenerator();
        return new ResponseEntity<>(responseGenerator.responseData(HttpStatus.OK.value(), "ok", id), HttpStatus.OK);
    }

    @DeleteMapping("/delete-img/{id}")
    @ApiOperation(value = "Delete Image Category", notes = "Delete Image Category", nickname = "Delete Image Category")
    public ResponseEntity<CommonResponse<CategoryResponse>> removeIMG(@PathVariable String id) {
        productService.isNumeric(id, "id");
        CategoryResponse response = categoryService.removeImg(Long.valueOf(id));
        ResponseGenerator responseGenerator = new ResponseGenerator();
        return new ResponseEntity<>(responseGenerator.responseData(HttpStatus.OK.value(), "ok", response), HttpStatus.OK);
    }

}
