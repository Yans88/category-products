package com.yansen.services;

import com.yansen.dtos.request.ProductRequest;
import com.yansen.dtos.response.PageableResponse;
import com.yansen.dtos.response.ProductResponse;
import com.yansen.entities.Category;
import com.yansen.entities.Products;
import com.yansen.exceptions.DataNotFoundException;
import com.yansen.exceptions.ValidationErrorException;
import com.yansen.repositories.CategoryRepo;
import com.yansen.repositories.ProductsRepo;
import com.yansen.utils.FileUploadUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductsService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private ProductsRepo productsRepo;

    @Autowired
    private CategoryRepo categoryRepo;


    @Value("${my.hostname}")
    private String hostname;


    public ProductResponse saveData(MultipartFile img, ProductRequest request, Long id) throws FileNotFoundException {

        if (StringUtils.isEmpty(request.getProductName())) {
            throw new ValidationErrorException("product name cannot be empty");
        }
        if (request.getIdCategory() <= 0L) {
            throw new ValidationErrorException("id category must be greater than 0");
        }

        categoryRepo.findById(request.getIdCategory()).orElseThrow(() -> new DataNotFoundException("data id category : " + request.getIdCategory() + " not found"));

        Products productEntity = convertToEntity(request);
        if (id > 0) {
            Optional<Products> dt = productsRepo.findById(id);
            if (!dt.isPresent()) {
                throw new DataNotFoundException("Data with ID: " + id + " not found");
            }
            Products productEntity2 = dt.get();
            productEntity.setId(id);
            productEntity.setImg(productEntity2.getImg());
        }
        if (img != null && img.getSize() > 0) {
            String imageDir = ResourceUtils.getURL("classpath:").getPath() + "static/uploads/products/";
            String fileName = FileUploadUtil.uploadImage(img, imageDir);
            productEntity.setImg(hostname + "/uploads/products/" + fileName);
        }

        return convertToDto(productsRepo.save(productEntity));
    }

    public PageableResponse<ProductResponse> getData(String idCat, int page, int size, String keyword, String sortColumn, String sortOrder) {

        categoryRepo.findById(Long.valueOf(idCat)).orElseThrow(() -> new DataNotFoundException("data id category : " + idCat + " not found"));

        Sort sort = sortOrder.toLowerCase() == "desc" ? Sort.by(sortColumn).descending()
                : Sort.by(sortColumn).ascending();
        Pageable paging = PageRequest.of(page, size, sort);
        Page<Products> productEntities;
        if (keyword != null) {
            productEntities = productsRepo.findByProductNameContainingIgnoreCase(keyword, paging);
        } else {
            productEntities = productsRepo.findAll(paging);
        }
        List<Products> dataList = productEntities.getContent();
        PageableResponse response = new PageableResponse();
        if (!dataList.isEmpty()) {
            response.setMessage("ok");
            List<ProductResponse> dt = dataList.stream()
                    .map(productsData -> modelMapper.map(productsData, ProductResponse.class))
                    .collect(Collectors.toList());
            response.setData(dt);
        } else {
            throw new DataNotFoundException("Data not found");
        }
        response.setTotalData(productEntities.getTotalElements());
        response.setTotalPages(productEntities.getTotalPages());
        response.setCurrentPage(productEntities.getNumber() + 1);
        response.setNext(productEntities.hasNext());
        response.setPrevious(productEntities.hasPrevious());
        response.setPageSize(productEntities.getSize());
        return response;
    }

    public ProductResponse getDetail(Long id) {
        if (id == 0L) {
            throw new ValidationErrorException("id required");
        }
        Optional<Products> dt = productsRepo.findById(id);
        if (!dt.isPresent()) {
            throw new DataNotFoundException("Data with ID: " + id + " not found");
        }
        Products productsEntity = dt.get();
        return convertToDto(productsEntity);
    }

    public void removeOne(Long id) {
        productsRepo.findById(id).orElseThrow(() -> new DataNotFoundException("data id : " + id + " not found"));
        productsRepo.deleteById(id);
    }

    public ProductResponse updateImg(MultipartFile img, Long id) throws FileNotFoundException {
        if (id == 0L) {
            throw new ValidationErrorException("id required");
        }
        if (img == null || img.getSize() <= 0) {
            throw new ValidationErrorException("img required");
        }
        Optional<Products> dt = productsRepo.findById(id);
        if (!dt.isPresent()) {
            throw new DataNotFoundException("Data with ID: " + id + " not found");
        }
        Products productEntity = dt.get();
        String imageDir = ResourceUtils.getURL("classpath:").getPath() + "static/uploads/products/";
        String fileName = FileUploadUtil.uploadImage(img, imageDir);
        productEntity.setImg(hostname + "/uploads/products/" + fileName);
        return convertToDto(productsRepo.save(productEntity));
    }

    public ProductResponse removeImg(Long id) {
        if (id == 0L) {
            throw new ValidationErrorException("id required");
        }
        Optional<Products> dt = productsRepo.findById(id);
        if (!dt.isPresent()) {
            throw new DataNotFoundException("Data with ID: " + id + " not found");
        }
        Products productEntity = dt.get();

        productEntity.setImg("");
        return convertToDto(productsRepo.save(productEntity));
    }

    private Products convertToEntity(ProductRequest request) {
        return modelMapper.map(request, Products.class);
    }


    private ProductResponse convertToDto(Products products) {
        ProductResponse response = modelMapper.map(products, ProductResponse.class);
        Category category = products.getCategory();
        response.setCategoryName(category.getCategoryName());
        return response;
    }

    public void isNumeric(String strNum, String type) {
        try {
            Long d = Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            throw new ValidationErrorException(type + ", tipe data invalid!");
        }
    }

}
