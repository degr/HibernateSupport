package org.forweb.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class AbstractService<T extends AbstractEntity, D extends AbstractDao<T>> {
    @Autowired
    protected D dao;

    public T findOne(Integer id){
        return dao.findOne(id);
    }
    public T findOne(Specification<T> specificaion){
        return dao.findOne(specificaion);
    }

    public boolean exists(Integer id){
        return dao.exists(id);
    }

    public Iterable<T> save(Iterable<T> list){
        return dao.save(list);
    }
    public T save(T entity){
        return dao.save(entity);
    }
    public void delete(Iterable<T> list){
        dao.delete(list);
    }
    public void delete(T entity){
        dao.delete(entity);
    }
    public void delete(Integer id){
        dao.delete(id);
    }
    public int count(){
        return (int)dao.count();
    }

    public List<T> findAll(){
        return dao.findAll();
    }
    public List<T> findAll(Iterable<Integer> ids){
        return dao.findAll(ids);
    }
    public List<T> findAll(Sort sort) {
        return dao.findAll(sort);
    }

    public Page<T> findAll(Pageable pageable) {
        return dao.findAll (pageable);
    }
}