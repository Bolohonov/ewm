package dev.bolohonov.repository.category;

import dev.bolohonov.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Найти категории с названием name
     */
    Category findAllByName(String name);
}
