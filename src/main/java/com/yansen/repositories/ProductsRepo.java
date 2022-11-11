package com.yansen.repositories;

import com.yansen.entities.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductsRepo extends JpaRepository<Products, Long> {

    Page<Products> findAll(Pageable pageable);

    Page<Products> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);
}
