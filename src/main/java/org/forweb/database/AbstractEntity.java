package org.forweb.database;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity extends AbstractPersistable<Integer> {

}