package com.example.socialApp.category.converter;

import com.example.socialApp.category.dto.CategoryDto;
import com.example.socialApp.category.model_repo.Category;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CategoryConverter extends BaseConverter<Category, CategoryDto> {

    @Override
    public Function<CategoryDto, Category> toEntity() {
        return dto -> {
            if (dto == null)
                return null;

            Category category = new Category();
            category.setName(dto.getName());
            category.setId(dto.getId());
            return category;
        };
    }

    @Override
    public Function<Category, CategoryDto> toDto() {
        return category -> {
            if (category == null)
                return null;

            CategoryDto dto = new CategoryDto();
            dto.setName(category.getName());
            dto.setId(category.getId());
            return dto;
        };
    }
}
