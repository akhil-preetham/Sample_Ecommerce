package com.ecommerce.common.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    
    T save(T entity);
    
    Optional<T> findById(ID id);
    
    Page<T> findAll(Pageable pageable);
    
    List<T> findAll();
    
    T update(ID id, T entity);
    
    void delete(ID id);
}
