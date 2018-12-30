package com.timxyz.controllers;

import com.timxyz.models.AccessLog;
import com.timxyz.services.AccessLogService;
import com.timxyz.services.exceptions.ServiceException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class AccessLogController extends ReadOnlyController<AccessLog, AccessLogService> {

    @ResponseBody
    @GetMapping("/access-logs/search-by/filter/{val}")
    public ResponseEntity filterByAccountUsernameOrFullName(@PathVariable("val") String filter) {
        return ResponseEntity.ok(service.getAllByFilter(filter));
    }

    @Override
    @ResponseBody
    @GetMapping("/access-logs/page/{pageNumber}")
    public ResponseEntity getPage(@PathVariable("pageNumber") int pageNumber) {
        return ResponseEntity.ok(service.listAllByPage(PageRequest.of(pageNumber-1, 20, Sort.Direction.DESC, "date")));
    }

    @Override
    @ResponseBody
    @GetMapping("/access-logs/all")
    public Iterable<AccessLog> all() {
        return super.all();
    }
}