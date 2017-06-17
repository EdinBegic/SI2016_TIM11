package com.timxyz.controllers;

import com.timxyz.controllers.forms.LocationType.LocationTypeCreateForm;
import com.timxyz.controllers.forms.LocationType.LocationTypeUpdateForm;
import com.timxyz.models.Location;
import com.timxyz.models.LocationType;
import com.timxyz.services.LocationTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.timxyz.services.exceptions.ServiceException;
import java.util.Collection;

import javax.validation.Valid;
import java.util.List;


@RestController
public class LocationTypeController extends BaseController<LocationType, LocationTypeService> {

    @Transactional
    @ResponseBody
    @PostMapping("/locationTypes")
    public ResponseEntity create(@RequestBody @Valid LocationTypeCreateForm newLocType, @RequestHeader("Authorization") String token) throws ServiceException {
        LocationType locationType = service.save(new LocationType(
                newLocType.getName(),
                newLocType.getDescription()
        ));

        logForCreate(token, locationType);

        return ResponseEntity.ok(locationType);
    }

    @Transactional
    @ResponseBody
    @PostMapping("/locationTypes/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody @Valid LocationTypeUpdateForm updatedLocationType, @RequestHeader("Authorization") String token) throws ServiceException {
        LocationType locationType = service.get(id);

        locationType.setName(updatedLocationType.getName());
        locationType.setDescription(updatedLocationType.getDescription());

        locationType = service.save(locationType);

        logForUpdate(token, locationType);

        return ResponseEntity.ok(locationType);
    }

    @ResponseBody
    @GetMapping("/locationTypes/filter-by/name")
    public Collection<LocationType> filterByName(@RequestParam("name") String name) {
        return service.filterByName(name);
    }

    @Override
    @ResponseBody
    @GetMapping("/locationTypes/all")
    public Iterable<LocationType> all() {
        return super.all();
    }

    @Override
    @ResponseBody
    @GetMapping("/locationTypes/{id}")
    public ResponseEntity get(Long id) throws ServiceException {
        return super.get(id);
    }

    @Override
    @ResponseBody
    @DeleteMapping("/locationTypes/{id}")
    public ResponseEntity delete(Long id, String token) throws ServiceException {
        return super.delete(id, token);
    }

    @Override
    @ResponseBody
    @GetMapping("/locationTypes/page/{pageNumber}")
    public ResponseEntity getPage(int pageNumber) {
        return super.getPage(pageNumber);
    }
}