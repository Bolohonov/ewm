package dev.bolohonov.controllers.privateAPI.category;

import dev.bolohonov.server.model.Category;
import dev.bolohonov.server.dto.CategoryDto;
import dev.bolohonov.server.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PatchMapping
    @ResponseStatus(OK)
    public Optional<CategoryDto> updateCategory(@RequestBody CategoryDto category) {
        return categoryService.updateCategoryByAdmin(category);
    }

    @PostMapping
    @ResponseStatus(OK)
    public Optional<CategoryDto> addCategory(@RequestBody @Valid Category category) {
        return categoryService.addCategoryByAdmin(category);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(OK)
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategoryByAdmin(catId);
    }
}
