package com.example.socialApp.shared;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface BaseService<E extends BaseEntity> {

    Page<E> getAll(int page, int size, String column, Sort.Direction direction);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    boolean update(Long id, E entity);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    boolean delete(Long id);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    E save(E entity);

    Optional<E> findById(Long id);

    E getById(Long id);
}
