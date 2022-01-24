package com.example.socialApp.category.web;

import com.example.socialApp.category.converter.CategoryConverter;
import com.example.socialApp.category.converter.CategoryShowConverter;
import com.example.socialApp.category.dto.CategoryDto;
import com.example.socialApp.category.dto.CategoryShowDto;
import com.example.socialApp.category.model_repo.Category;
import com.example.socialApp.exceptions.DuplicateObjectException;
import com.example.socialApp.shared.BaseController;
import com.example.socialApp.shared.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/categories")
@CrossOrigin
public class CategoryController extends BaseController<Category> {

    private final CategoryServiceImplementation categoryService;

    private final CategoryShowConverter categoryShowConverter;

    private final CategoryConverter categoryConverter;

    @Autowired
    public CategoryController(BaseService<Category> service, CategoryServiceImplementation categoryService,
                              CategoryShowConverter categoryShowConverter, CategoryConverter categoryConverter) {
        super(service);
        this.categoryService = categoryService;
        this.categoryShowConverter = categoryShowConverter;
        this.categoryConverter = categoryConverter;
    }

    /* GET */

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CategoryShowDto> getOne(@PathVariable final Long id) {
        return super.getOne(id, this.categoryShowConverter.toDto());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CategoryShowDto>> getAll(@RequestParam(defaultValue = "") final String filter) {
        if (filter.equals("")) {
            return ResponseEntity.ok(this.categoryService.getAll().stream()
                    .map(this.categoryShowConverter.toDto()).collect(Collectors.toList()));
        } else {
            return ResponseEntity.ok(this.categoryService.getAllWithFilter(filter).stream()
                    .map(this.categoryShowConverter.toDto()).collect(Collectors.toList()));
        }
    }

    /* POST */

    @PostMapping("")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> create(@RequestBody @Valid final CategoryDto dto) {
        if (this.categoryService.isExistByName(dto.getName())) {
            throw new DuplicateObjectException("Kategoria o podanej nazwie istnieje");
        }
        return super.create(this.categoryConverter.toEntity().apply(dto));
    }

    /* PUT */

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> update(@PathVariable final Long id, @RequestBody @Valid final CategoryDto dto) {
        if (this.categoryService.isExistByName(dto.getName())) {
            throw new DuplicateObjectException("Kategoria o podanej nazwie istnieje");
        }
        return super.update(id, this.categoryConverter.toEntity().apply(dto));
    }

    /* DELETE */

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        if (this.categoryService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
