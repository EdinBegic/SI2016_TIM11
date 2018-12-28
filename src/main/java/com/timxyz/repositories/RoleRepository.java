package com.timxyz.repositories;

import com.timxyz.models.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
    public Role findByName(String name);

}