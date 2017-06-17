package com.timxyz.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MiscController {

    @ResponseBody
    @RequestMapping(method = RequestMethod.OPTIONS, path = "/{<[0-9a-zA-Z/-]+>path}")
    public ResponseEntity catchAllOptions(String path) {
        return ResponseEntity.ok(true);
    }
}
