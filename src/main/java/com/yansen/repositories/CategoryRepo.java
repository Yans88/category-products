package com.yansen.repositories;

import com.yansen.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

    Page<Category> findAll(Pageable pageable);

    Page<Category> findByCategoryNameContainingIgnoreCase(String keyword, Pageable pageable);
}