package com.example.socialApp.category.web;

import com.example.socialApp.category.model_repo.Category;
import com.example.socialApp.category.model_repo.CategoryRepository;
import com.example.socialApp.event.web.EventServiceImplementation;
import com.example.socialApp.shared.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService, BaseService<Category> {

    private final CategoryRepository categoryRepository;

    private EventServiceImplementation eventService;

    @Autowired
    public CategoryServiceImplementation(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setEventService(EventServiceImplementation eventService) {
        this.eventService = eventService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> getAll(int page, int size, String column, Sort.Direction direction) {
        Sort sort = Sort.by(new Sort.Order(direction, column));
        return this.categoryRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean update(Long id, Category entity) {
        if (isExists(id)) {
            Category category = getById(id);
            category.setName(entity.getName());
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean delete(Long id) {
        if (isExists(id)) {
            Category category = getById(id);
            this.eventService.removeCategoryForAllEvents(category);
            this.categoryRepository.delete(category);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Category save(Category entity) {
        return this.categoryRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Category getById(Long id) {
        if (isExists(id)) {
            return this.categoryRepository.getById(id);
        } else {
            return null;
        }
    }

    @Override
    public boolean isExists(Long id) {
        return this.categoryRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllWithFilter(String filter) {
        return this.categoryRepository.getAllByNameContains(filter);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isExistByName(String name) {
        List<Category> categories = this.categoryRepository.findAll();
        for (Category category : categories) {
            if (category.getName().toUpperCase().equals(name.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
