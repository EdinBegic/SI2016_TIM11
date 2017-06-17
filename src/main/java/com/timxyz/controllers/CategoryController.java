package com.timxyz.controllers;

import com.timxyz.controllers.forms.Category.CategoryCreateForm;
import com.timxyz.controllers.forms.Category.CategoryUpdateForm;
import com.timxyz.models.Category;
import com.timxyz.services.CategoryService;
import com.timxyz.services.exceptions.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
public class CategoryController extends BaseController<Category, CategoryService> {

    @Transactional
    @ResponseBody
    @PostMapping("/categories")
    public ResponseEntity create(@RequestBody @Valid CategoryCreateForm newCategory, @RequestHeader("Authorization") String token) throws ServiceException {
        Category category = service.save(new Category(
                newCategory.getName(),
                service.get(newCategory.getParentId())
        ));

        logForCreate(token, category);

        return ResponseEntity.ok(category);
    }

    @Transactional
    @ResponseBody
    @PostMapping("/categories/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody @Valid CategoryUpdateForm updatedCategory, @RequestHeader("Authorization") String token) throws ServiceException {
        Category category = service.get(id);

        category.setName(updatedCategory.getName());
        category.setParent(service.get(updatedCategory.getParentId()));

        category = service.save(category);

        logForUpdate(token, category);

        return ResponseEntity.ok(category);
    }

    @ResponseBody
    @GetMapping("/categories/filter-by/name")
    public Collection<Category> filterByName(@RequestParam("name") String name) {
        return service.filterByName(name);
    }

    @Override
    @ResponseBody
    @GetMapping("/categories/all")
    public Iterable<Category> all() {
        return super.all();
    }

    @Override
    @ResponseBody
    @GetMapping("/categories/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) throws ServiceException {
        return super.get(id);
    }

    @Override
    @ResponseBody
    @DeleteMapping("/categories/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id, String token) throws ServiceException {
        return super.delete(id, token);
    }

    @Override
    @ResponseBody
    @GetMapping("/categories/page/{pageNumber}")
    public ResponseEntity getPage(@PathVariable("pageNumber") int pageNumber) {
        return super.getPage(pageNumber);
    }
}

