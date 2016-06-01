# HibernateSupport library allow easyly connect to database using hibernate

[![](https://jitpack.io/v/degr/HibernateSupport.svg)](https://jitpack.io/#degr/HibernateSupport)

Here is sample how start with it:

public class AppInitializer implements WebApplicationInitializer {

public static final String BASE_PACKAGE = "org.forweb.commandos";
  @Override
  public void onStartup(ServletContext container) {
    HibernateSupport.init("localhost", "databaseUser", "databaseUserPassword", "database_name", "org.forweb.entity");
    
    
    ...
package org.forweb.entity;

import org.forweb.database.AbstractEntity;
import javax.persistence.Entity;

@Entity
public class Map extends AbstractEntity {
    private String title;
    private Integer x;
    private Integer y;
    ...getters and setters
}
....

package org.forweb.commandos.dao;

import org.forweb.commandos.entity.Map;
import org.forweb.database.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public interface MapDao extends AbstractDao<Map> {
}

....

@Service
public class MapService {
  @Autowired
  MapDao mapDao;

  @PostConstruct
  public void postConstruct() {
      List<Map> maps = mapDao.findAll();
      System.out.println(maps);
  }
