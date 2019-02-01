package com.haulmont.testtask.data.dao;

import com.haulmont.testtask.data.entity.Mechanic;

import java.util.Collection;

public interface MechanicDAO {
    void insert(Mechanic mechanic);

    void delete(Mechanic mechanic);

    Mechanic findByID(Long id);

    void update(Mechanic mechanic);

    void saveOrUpdate(Mechanic mechanic);

    Collection<Mechanic> getAll();
}
