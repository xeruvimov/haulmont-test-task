package com.haulmont.testtask.data.dao.daoimpl;

import com.haulmont.testtask.data.dao.ClientDAO;
import com.haulmont.testtask.data.entity.Client;

import java.util.Collection;

public class ClientDAOImpl extends Transactional<Client> implements ClientDAO {
    @Override
    public void insert(Client client) {
        String message = "Merge client " + client;

        this.transaction((x -> {
            this.entityManager.merge(x);
            LOGGER.info(message);
        }), client, message);
    }

    @Override
    public void delete(Client client) {
        String message = "Delete client " + client;

        this.transaction((x -> {
            Client removeClient = entityManager.find(Client.class, x);
            if (removeClient != null)
                entityManager.remove(removeClient);
            LOGGER.info(message);
        }), client, message);
    }

    @Override
    public Client findByID(Long id) {
        String message = "Find client by id: " + id;

        return this.transaction(() -> {
            Client result = entityManager.createNamedQuery("Client.findById", Client.class)
                    .setParameter("id", id).getSingleResult();
            LOGGER.info(message);
            return result;
        }, message);
    }

    @Override
    public void update(Client client) {
        String message = "Update client: " + client;

        this.transaction((x -> {
            entityManager.merge(x);
            LOGGER.info(message);
        }), client, message);
    }

    @Override
    public void saveOrUpdate(Client client) {
        String message = "Save or update client: " + client;

        this.transaction((x -> {
            if (x.getId() == null)
                entityManager.persist(x);
            else
                entityManager.merge(x);
            LOGGER.info(message);
        }), client, message);
    }

    @Override
    public Collection<Client> getAll() {
        String message = "Find all clients";

        return this.transaction(() -> {
            Collection<Client> result =
                    entityManager.createNamedQuery("Client.findAll", Client.class).getResultList();
            LOGGER.info(message);
            return result;
        }, message);
    }
}
