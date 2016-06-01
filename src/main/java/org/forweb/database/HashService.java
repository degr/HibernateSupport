package org.forweb.database;

import java.util.*;
import java.util.stream.Collectors;

public class HashService<T extends AbstractEntity, D extends AbstractDao<T>> extends AbstractService<T, D>{

    Map<Integer, T> map;


    public T findOne(Integer id){
        return getMap().containsKey(id) ? getMap().get(id) : null;
    }
    public boolean exists(Integer id){
        return getMap().containsKey(id);
    }

    public Iterable<T> save(Iterable<T> list){
        Iterable<T> out = super.save(list);
        for(T item : out) {
            getMap().put(item.getId(), item);
        }
        return out;
    }
    public T save(T entity){
        T out = super.save(entity);
        getMap().put(out.getId(), out);
        return out;
    }
    public void delete(Iterable<T> list){
        super.delete(list);
        for(T item : list) {
            if(getMap().containsKey(item.getId())) {
                getMap().remove(item.getId());
            }
        }
    }
    public void delete(T entity){
        super.delete(entity);
        if(getMap().containsKey(entity.getId())) {
            getMap().remove(entity.getId());
        }
    }
    public void delete(Integer id){
        dao.delete(id);
        if(getMap().containsKey(id)) {
            getMap().remove(id);
        }
    }

    public List<T> findAll(){
        return getMap().values().stream().collect(Collectors.toList());
    }
    public List<T> findAll(Iterable<Integer> ids){
        List<T> out = new ArrayList<>();
        for(Integer id : ids) {
            if(getMap().containsKey(id)) {
                out.add(getMap().get(id));
            }
        }
        return out;
    }

    private Map<Integer, T> getMap(){
        if(map == null) {
            map = new LinkedHashMap<>();
            for(T item : dao.findAll()) {
                map.put(item.getId(), item);
            }
        }
        return map;
    }
}