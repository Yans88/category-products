package com.yansen.controllers;

import com.yansen.dtos.request.ProductRequest;
import com.yansen.dtos.response.CommonResponse;
import com.yansen.dtos.response.PageableResponse;
import com.yansen.dtos.response.ProductResponse;
import com.yansen.dtos.response.ResponseGenerator;
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
@RequestMapping(value = "/api/product")
@Api(description = "Test-Dayalima", tags = "Data Product")
public class ProductController {

    @Autowired
    private ProductsService productService;


    @GetMapping
    @ApiOperation(value = "Get Product by Id Category", notes = "Get Product by Id Category", nickname = "Get Product by Id Category")
    public ResponseEntity<PageableResponse<ProductResponse>> getAllData(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productName") String sort_column,
            @RequestParam(defaultValue = "ASC") String sort_order,
            @RequestParam(defaultValue = "") String id_category
    ) {

        //cek type data id category, is numeric true or false
        productService.isNumeric(id_category, "id_category");

        PageableResponse response = productService.getData(id_category, page, size, keyword, sort_column, sort_order);
        response.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Product Detail by Id Product", notes = "Get Product Detail by Id Product", nickname = "Get Product Detail by Id Product")
    public ResponseEntity<CommonResponse<ProductResponse>> getDetail(@PathVariable String id) {
        productService.isNumeric(id, "id");
        ProductResponse response = productService.getDetail(Long.valueOf(id));
        ResponseGenerator responseGenerator = new ResponseGenerator();
        return new ResponseEntity<>(responseGenerator.responseData(HttpStatus.OK.value(), "ok", response), HttpStatus.OK);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Add Product", notes = "Add Product", nickname = "Add Product")
    public ResponseEntity<CommonResponse<ProductResponse>> addData(
            @RequestParam(required = true) String id_category,
            @RequestParam(required = true) String product_name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") String qty,
            @RequestPart(value = "img", required = false) MultipartFile img) throws FileNotFoundException {

        //cek type data id category dan qty, numeric true or false
        productService.isNumeric(id_category, "id_category");
        productService.isNumeric(qty, "qty");
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName(product_name);
        productRequest.setDescription(description);
        productRequest.setQty(Integer.valueOf(qty));
        productRequest.setIdCategory(Long.valueOf(id_category));

        ProductResponse response = productService.saveData(img, productRequest, 0L);
        ResponseGenerator responseGenerator = new ResponseGenerator();
        return new ResponseEntity<>(responseGenerator.responseData(HttpStatus.CREATED.value(), "ok", response), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update Product without Image", notes = "Update Product without Image", nickname = "Update Product without Image")
    public ResponseEntity<CommonResponse<ProductResponse>> updateData(@RequestBody ProductRequest request, @PathVariable("id") String id) throws FileNotFoundException {
        productService.isNumeric(id, "id");
        ProductResponse response = productService.saveData(null, request, Long.valueOf(id));
        ResponseGenerator responseGenerator = new ResponseGenerator();
        return new ResponseEntity<>(responseGenerator.responseData(HttpStatus.ACCEPTED.value(), "ok", response), HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Edit Product with Image", notes = "Edit Product with Image", nickname = "Edit Product with Image")
    public ResponseEntity<CommonResponse<ProductResponse>> editData(
            @RequestParam(required = true) String id,
            @RequestParam(required = true) String id_category,
            @RequestParam(required = true) String product_name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") String qty,
            @RequestPart(value = "img", required = false) MultipartFile img) throws FileNotFoundException {

        //cek type data id, id category dan qty, is numeric true or false
        productService.isNumeric(id, "id");
        productService.isNumeric(id_category, "id_category");
        productService.isNumeric(qty, "qty");

        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName(product_name);
        productRequest.setDescription(description);
        productRequest.setQty(Integer.valueOf(qty));
        productRequest.setIdCategory(Long.valueOf(id_category));

        ProductResponse response = productService.saveData(img, productRequest, Long.valueOf(id));
        ResponseGenerator responseGenerator = new ResponseGenerator();
        return new ResponseEntity<>(responseGenerator.responseData(HttpStatus.ACCEPTED.value(), "ok", response), HttpStatus.ACCEPTED);
    }


    @PostMapping(value = "/update-img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Update Image Product", notes = "Update Image Product", nickname = "Update Image Product")
    public ResponseEntity<CommonResponse<ProductResponse>> updateIMG(
            @RequestParam(required = true) String id,
            @RequestPart(value = "img", required = true) MultipartFile img) throws FileNotFoundException {

        //cek type data id
        productService.isNumeric(id, "id");

        ProductResponse response = productService.updateImg(img, Long.valueOf(id));
        ResponseGenerator responseGenerator = new ResponseGenerator();
        return new ResponseEntity<>(responseGenerator.responseData(HttpStatus.ACCEPTED.value(), "ok", response), HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete Product", notes = "Delete Product", nickname = "Delete Product")
    public ResponseEntity<CommonResponse> removeOne(@PathVariable String id) {
        productService.isNumeric(id, "id");
        productService.removeOne(Long.valueOf(id));
        ResponseGenerator responseGenerator = new ResponseGenerator();
        return new ResponseEntity<>(responseGenerator.responseData(HttpStatus.OK.value(), "ok", id), HttpStatus.OK);
    }

    @DeleteMapping("/delete-img/{id}")
    @ApiOperation(value = "Delete Image Product", notes = "Delete Image Product", nickname = "Delete Image Product")
    public ResponseEntity<CommonResponse<ProductResponse>> removeIMG(@PathVariable String id) {
        productService.isNumeric(id, "id");
        ProductResponse response = productService.removeImg(Long.valueOf(id));
        ResponseGenerator responseGenerator = new ResponseGenerator();
        return new ResponseEntity<>(responseGenerator.responseData(HttpStatus.OK.value(), "ok", response), HttpStatus.OK);
    }

}
