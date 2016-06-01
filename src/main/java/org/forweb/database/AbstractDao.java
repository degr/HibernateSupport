package org.forweb.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface AbstractDao<T extends AbstractEntity>  extends JpaRepository<T, Integer>, JpaSpecificationExecutor<T> {

}