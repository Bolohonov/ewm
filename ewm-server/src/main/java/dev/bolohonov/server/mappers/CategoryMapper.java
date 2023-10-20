package dev.bolohonov.server.mappers;

import dev.bolohonov.server.model.Category;
import dev.bolohonov.server.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Collection<CategoryDto> toCategoryDto(Iterable<Category> categories) {
        Collection<CategoryDto> dtos = new ArrayList<>();
        for (Category category : categories) {
            dtos.add(toCategoryDto(category));
        }
        return dtos;
    }
}
