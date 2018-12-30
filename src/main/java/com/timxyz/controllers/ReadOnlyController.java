package com.timxyz.controllers;

import com.timxyz.models.AccessLog;
import com.timxyz.models.BaseModel;
import com.timxyz.services.ReadOnlyService;
import com.timxyz.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

public abstract class ReadOnlyController<M extends BaseModel, S extends ReadOnlyService<M, ? > > {
    protected S service;

    @Autowired
    public void setService(S service) {
        this.service = service;
    }

    public Iterable<M> all() {
        return service.all();
    }

    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(service.get(id));
        } catch(ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public ResponseEntity getPage(@PathVariable("pageNumber") int pageNumber) {
        return ResponseEntity.ok(service.listAllByPage(PageRequest.of(pageNumber - 1, 5)));
    }
}
