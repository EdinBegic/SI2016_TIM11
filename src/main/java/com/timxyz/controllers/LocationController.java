package com.timxyz.controllers;


import com.timxyz.controllers.forms.Location.LocationCreateForm;
import com.timxyz.controllers.forms.Location.LocationUpdateForm;
import com.timxyz.models.Location;
import com.timxyz.models.LocationType;
import com.timxyz.services.LocationService;
import com.timxyz.services.LocationTypeService;
import com.timxyz.services.exceptions.ServiceException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class LocationController extends BaseController<Location, LocationService> {
    private LocationTypeService locationTypeService;

    @Autowired
    public void setLocationTypeService(LocationTypeService locationTypeService) {
        this.locationTypeService = locationTypeService;
    }

    @Transactional
    @ResponseBody
    @PostMapping("/locations")
    public ResponseEntity create(@RequestBody @Valid LocationCreateForm newLocation, @RequestHeader("Authorization") String token) throws ServiceException {
        Location location = service.save(new Location(
                newLocation.getName(),
                service.get(newLocation.getParentId()),
                locationTypeService.get(newLocation.getTypeId())
        ));

        logForCreate(token, location);

        return ResponseEntity.ok(location);
    }

    @Transactional
    @ResponseBody
    @PostMapping("/locations/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody @Valid LocationUpdateForm updatedLocation, @RequestHeader("Authorization") String token) throws ServiceException {
        Location location = service.get(id);

        location.setName(updatedLocation.getName());
        location.setParent(service.get(updatedLocation.getParentId()));
        location.setType(locationTypeService.get(updatedLocation.getTypeId()));

        location = service.save(location);

        logForUpdate(token, location);

        return ResponseEntity.ok(location);
    }

    @ResponseBody
    @GetMapping("/locations/filter-by/name/{name}")
    public Collection<Location> filterByName(@PathVariable("name") String name) {
        return service.filterByName(name);
    }

    @Override
    @ResponseBody
    @GetMapping("/locations/all")
    public Iterable<Location> all() {
        return super.all();
    }

    @Override
    @ResponseBody
    @GetMapping("/locations/{id}")
    public ResponseEntity get(Long id) throws ServiceException {
        return super.get(id);
    }

    @Override
    @ResponseBody
    @DeleteMapping("/locations/{id}")
    public ResponseEntity delete(Long id, String token) throws ServiceException {
        return super.delete(id, token);
    }

    @Override
    @ResponseBody
    @GetMapping("/locations/page/{pageNumber}")
    public ResponseEntity getPage(int pageNumber) {
        return super.getPage(pageNumber);
    }
}