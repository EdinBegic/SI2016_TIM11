package com.timxyz.services;

import com.timxyz.models.Role;
import com.timxyz.repositories.RoleRepository;
import com.timxyz.services.exceptions.ServiceException;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends ReadOnlyService<Role, RoleRepository> {

    public Role find(String roleName) {
        try {
            return repository.findByName(roleName);
        } catch (Exception e) {
            throw new ServiceException("Could not find role with given name", e);
        }
    }
}
