package com.evsyukoov.module.access;

import java.util.List;

public interface DAO<T> {

    void addEntity(T entity);

    List<T> getAllEntities();

    List<T> getAllEntitiesSorted();

    List<T> getAllEntitiesGrouping();


}
