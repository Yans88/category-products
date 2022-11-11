package com.yansen.services;

import com.yansen.dtos.request.CategoryRequest;
import com.yansen.dtos.response.CategoryResponse;
import com.yansen.dtos.response.PageableResponse;
import com.yansen.entities.Category;
import com.yansen.exceptions.DataNotFoundException;
import com.yansen.exceptions.ValidationErrorException;
import com.yansen.repositories.CategoryRepo;
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
public class CategoryService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private CategoryRepo categoryRepo;

    @Value("${my.hostname}")
    private String hostname;

    public CategoryResponse saveData(MultipartFile img, CategoryRequest request, Long id) throws FileNotFoundException {

        if (StringUtils.isEmpty(request.getCategoryName())) {
            throw new ValidationErrorException("category name cannot be empty");
        }
        Category categoryEntity = convertToEntity(request);
        if (id > 0) {
            Optional<Category> dt = categoryRepo.findById(id);
            if (!dt.isPresent()) {
                throw new DataNotFoundException("Data with ID: " + id + " not found");
            }
            Category categoryEntity2 = dt.get();
            categoryEntity.setImg(categoryEntity2.getImg());
            categoryEntity.setId(id);
        }
        if (img != null && img.getSize() > 0) {
            String imageDir = ResourceUtils.getURL("classpath:").getPath() + "static/uploads/categories/";
            String fileName = FileUploadUtil.uploadImage(img, imageDir);
            categoryEntity.setImg(hostname + "/uploads/categories/" + fileName);
        }
        System.out.println(categoryEntity.getImg());
        return convertToDto(categoryRepo.save(categoryEntity));
    }


    public PageableResponse<CategoryResponse> getData(int page, int size, String keyword, String sortColumn, String sortOrder) {
        Sort sort = sortOrder.toLowerCase() == "desc" ? Sort.by(sortColumn).descending()
                : Sort.by(sortColumn).ascending();
        Pageable paging = PageRequest.of(page, size, sort);
        Page<Category> categoryEntities;
        if (keyword != null) {
            categoryEntities = categoryRepo.findByCategoryNameContainingIgnoreCase(keyword, paging);
        } else {
            categoryEntities = categoryRepo.findAll(paging);
        }
        List<Category> dataList = categoryEntities.getContent();
        PageableResponse response = new PageableResponse();
        if (!dataList.isEmpty()) {
            response.setMessage("ok");
            List<CategoryResponse> dt = dataList.stream()
                    .map(categoryData -> modelMapper.map(categoryData, CategoryResponse.class))
                    .collect(Collectors.toList());
            response.setData(dt);
        } else {
            throw new DataNotFoundException("Data not found");
        }
        response.setTotalData(categoryEntities.getTotalElements());
        response.setTotalPages(categoryEntities.getTotalPages());
        response.setCurrentPage(categoryEntities.getNumber() + 1);
        response.setNext(categoryEntities.hasNext());
        response.setPrevious(categoryEntities.hasPrevious());
        response.setPageSize(categoryEntities.getSize());
        return response;
    }


    public void removeOne(Long id) {
        categoryRepo.findById(id).orElseThrow(() -> new DataNotFoundException("data id : " + id + " not found"));
        categoryRepo.deleteById(id);
    }

    public CategoryResponse removeImg(Long id) {
        if (id == 0L) {
            throw new ValidationErrorException("id required");
        }
        Optional<Category> dt = categoryRepo.findById(id);
        if (!dt.isPresent()) {
            throw new DataNotFoundException("Data with ID: " + id + " not found");
        }
        Category categoryEntity = dt.get();
        categoryEntity.setImg("");
        return convertToDto(categoryRepo.save(categoryEntity));
    }


    private Category convertToEntity(CategoryRequest request) {
        return modelMapper.map(request, Category.class);
    }


    private CategoryResponse convertToDto(Category category) {
        return modelMapper.map(category, CategoryResponse.class);
    }

}
