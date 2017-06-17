package com.timxyz.controllers;

import com.timxyz.models.Role;
import com.timxyz.services.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController extends ReadOnlyController<Role, RoleService>  {

    @Override
    @ResponseBody
    @GetMapping("/roles/all")
    public Iterable<Role> all() {
        return super.all();
    }
}
