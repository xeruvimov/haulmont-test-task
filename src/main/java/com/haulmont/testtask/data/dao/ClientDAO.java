package com.haulmont.testtask.data.dao;

import com.haulmont.testtask.data.entity.Client;

import java.util.Collection;

public interface ClientDAO {
    void insert(Client client);

    void delete(Client client);

    Client findByID(Long id);

    void update(Client client);

    void saveOrUpdate(Client client);

    Collection<Client> getAll();
}
