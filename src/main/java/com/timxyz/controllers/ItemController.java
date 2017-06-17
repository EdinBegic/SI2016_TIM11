package com.timxyz.controllers;

import com.timxyz.controllers.forms.Item.ItemCreateForm;
import com.timxyz.controllers.forms.Item.ItemUpdateForm;
import com.timxyz.models.*;
import com.timxyz.services.CategoryService;
import com.timxyz.services.ItemService;
import com.timxyz.services.LocationService;
import com.timxyz.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@RestController
public class ItemController extends BaseController<Item, ItemService> {
    private CategoryService categoryService;
    private LocationService locationService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    @Transactional
    @ResponseBody
    @PostMapping("/items")
    public ResponseEntity create(@RequestBody @Valid ItemCreateForm newItem, @RequestHeader("Authorization") String token) throws ServiceException {
        Item item = service.save(new Item(
                newItem.getSkuNumber(),
                newItem.getName(),
                newItem.getUnitOfMeasurement(),
                newItem.getPurchasedBy(),
                newItem.getPersonResponsible(),
                newItem.getDateOfPurchase(),
                newItem.getValue(),
                categoryService.get(newItem.getCategoryID()),
                locationService.get(newItem.getLocationID())
        ));

        logForCreate(token, item);

        return ResponseEntity.ok(item);
    }

    public Collection<Item> getAllByItemName(@RequestParam("name") String name){
        return service.filterByName(name);
    }

    @Transactional
    @ResponseBody
    @PostMapping("/items/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody @Valid ItemUpdateForm updatedItem, @RequestHeader("Authorization") String token) throws ServiceException {
        Item item = service.get(id);

        item.setName(updatedItem.getName());
        item.setUnitOfMeasurement(updatedItem.getUnitOfMeasurement());
        item.setValue(updatedItem.getValue());
        item.setPersonResponsible(updatedItem.getPersonResponsible());
        item.setDateOfPurchase(updatedItem.getDateOfPurchase());
        item.setSkuNumber(updatedItem.getSkuNumber());
        item.setPurchasedBy(updatedItem.getPurchasedBy());

        item.setCategory(categoryService.get(updatedItem.getCategoryID()));
        item.setLocation(locationService.get(updatedItem.getLocationID()));

        item = service.save(item);

        logForUpdate(token, item);

        return ResponseEntity.ok(item);
    }

    @ResponseBody
    @GetMapping("/items/filter-by/name")
    public Collection<Item> filterByName(@RequestParam("name") String name) {
        return service.filterByName(name);
    }

    @Override
    @ResponseBody
    @GetMapping("/items/all")
    public Iterable<Item> all() {
        return super.all();
    }

    @Override
    @ResponseBody
    @GetMapping("/items/{id}")
    public ResponseEntity get(Long id) throws ServiceException {
        return super.get(id);
    }

    @Override
    @ResponseBody
    @DeleteMapping("/items/{id}")
    public ResponseEntity delete(Long id, String token) throws ServiceException {
        return super.delete(id, token);
    }

    @Override
    @ResponseBody
    @GetMapping("/items/page/{pageNumber}")
    public ResponseEntity getPage(int pageNumber) {
        return super.getPage(pageNumber);
    }
}
