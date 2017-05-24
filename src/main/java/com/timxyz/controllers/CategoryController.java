package com.timxyz.controllers;

import com.timxyz.controllers.forms.Category.CategoryCreateForm;
import com.timxyz.controllers.forms.Category.CategoryUpdateForm;
import com.timxyz.models.Category;
import com.timxyz.services.CategoryService;
import com.timxyz.services.exceptions.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
public class CategoryController extends BaseController<Category, CategoryService> {

    @ResponseBody
    public ResponseEntity create(@RequestBody @Valid CategoryCreateForm newCategory, @RequestHeader("Authorization") String token) {
        try {
            Category category = service.save(new Category(
                    newCategory.getName(),
                    service.get(newCategory.getParentId())
            ));

            logForCreate(token, category);

            return ResponseEntity.ok(category);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ResponseBody
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody @Valid CategoryUpdateForm updatedCategory, @RequestHeader("Authorization") String token) {
        try {
            Category category = service.get(id);

            category.setName(updatedCategory.getName());

            category = service.save(category);

            logForUpdate(token, category);

            return ResponseEntity.ok(category);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Collection<Category> filterByName(@RequestParam("name") String name) {
        return service.filterByName(name);
    }
}

