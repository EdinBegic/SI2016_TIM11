package com.timxyz.controllers;

import com.timxyz.controllers.forms.Status.StatusCreateForm;
import com.timxyz.controllers.forms.Status.StatusUpdateForm;
import com.timxyz.models.Status;
import com.timxyz.services.StatusService;
import com.timxyz.services.exceptions.ServiceException;
import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class StatusController extends BaseController<Status, StatusService> {

    @Transactional
    @ResponseBody
    @PostMapping("/status")
    public ResponseEntity create(@RequestBody @Valid StatusCreateForm newStatus, @RequestHeader("Authorization") String token) throws ServiceException {
        Status model = service.save(new Status(newStatus.getName()));

        logForCreate(token, model);

        return ResponseEntity.ok(model);
    }

    @Transactional
    @ResponseBody
    @PostMapping("/status/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody @Valid StatusUpdateForm updatedStatus, @RequestHeader("Authorization") String token) throws ServiceException {
        Status status = service.get(id);

        status.setName(updatedStatus.getName());

        status = service.save(status);

        logForUpdate(token, status);

        return ResponseEntity.ok(status);
    }

    @ResponseBody
    @GetMapping("/status/filter-by/name")
    public Collection<Status> filterByName(@RequestParam("name") String name) {
        return service.filterByName(name);
    }

    @Override
    @ResponseBody
    @GetMapping("/status/all")
    public Iterable<Status> all() {
        return super.all();
    }

    @Override
    @ResponseBody
    @GetMapping("/status/{id}")
    public ResponseEntity get(Long id) throws ServiceException {
        return super.get(id);
    }

    @Override
    @ResponseBody
    @DeleteMapping("/status/{id}")
    public ResponseEntity delete(Long id, String token) throws ServiceException {
        return super.delete(id, token);
    }

    @Override
    @ResponseBody
    @GetMapping("/status/page/{pageNumber}")
    public ResponseEntity getPage(int pageNumber) {
        return super.getPage(pageNumber);
    }
}
