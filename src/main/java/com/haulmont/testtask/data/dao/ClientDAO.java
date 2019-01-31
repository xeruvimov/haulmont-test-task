package com.haulmont.testtask.data.dao;

import com.haulmont.testtask.data.entity.Client;

import java.util.Collection;

public interface ClientDAO {
    void insert(Client item);

    void delete(Client item);

    Client findByID(Long id);

    void update(Client item);

    void saveOrUpdate(Client item);

    Collection<Client> getAll();
}
