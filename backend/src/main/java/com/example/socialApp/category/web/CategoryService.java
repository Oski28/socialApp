package com.example.socialApp.category.web;

import com.example.socialApp.category.model_repo.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    List<Category> getAllWithFilter(String filter);

    Boolean isExistByName(String name);
}
