package com.example.socialApp.category.model_repo;

import com.example.socialApp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Category> {

    List<Category> getAllByNameContains(String filter);

    Optional<Category> findByName(String name);
}
