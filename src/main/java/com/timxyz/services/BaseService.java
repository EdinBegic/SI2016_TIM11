package com.timxyz.services;

import com.timxyz.models.BaseModel;
import com.timxyz.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public class BaseService<M extends BaseModel, R extends PagingAndSortingRepository<M, Long> > {
    protected R repository;

    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }

    public Iterable<M> all() {
        return repository.findAll();
    }

    public M get(Long id) throws ServiceException {
        if (id == null) {
            return null;
        }

        M model = repository.findById(id).get();

        if(model == null) {
            throw new ServiceException("Nije pronađen zapis sa tim ID brojem.");
        }

        return model;
    }

    public M save(M model) throws ServiceException{
        return repository.save(model);
    }

    public void delete(Long id) throws ServiceException {
        try {
            repository.deleteById(id);
        }
        catch (Exception e) {
            throw new ServiceException("Zapis se ne može obrisati jer je povezan sa drugim zapisima.");
        }
    }

    public Boolean exists(Long id) { return repository.existsById(id); }

    public Long count() { return repository.count(); }

    public Page<M> listAllByPage(Pageable p) {
        return repository.findAll(p);
    }
}
