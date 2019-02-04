package com.haulmont.testtask.data.dao.daoimpl;

import com.haulmont.testtask.data.dao.ClientDAO;
import com.haulmont.testtask.data.entity.Client;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Collection;

public class ClientDAOImpl implements ClientDAO {
    private Transactional<Client> transactional;
    private static final Logger LOGGER = LogManager.getLogger(ClientDAOImpl.class);

    public ClientDAOImpl() {
        this.transactional = new Transactional<>(LOGGER);
    }

    @Override
    public void insert(Client client) {
        String message = "Merge client " + client;

        this.transactional.transaction((x -> {
            this.transactional.getEntityManager().merge(x);
            LOGGER.info(message);
        }), client, message);
    }

    @Override
    public void delete(Client client) {
        String message = "Delete client " + client;

        this.transactional.transaction((x -> {
            Client removeClient = this.transactional.getEntityManager().find(Client.class, x.getId());
            if (removeClient != null)
                this.transactional.getEntityManager().remove(removeClient);
            this.transactional.getEntityManager().flush();
            LOGGER.info(message);
        }), client, message);
    }

    @Override
    public Client findByID(Long id) {
        String message = "Find client by id: " + id;

        return this.transactional.transaction(() -> {
            Client result = this.transactional.getEntityManager().createNamedQuery("Client.findById", Client.class)
                    .setParameter("id", id).getSingleResult();
            LOGGER.info(message);
            return result;
        }, message);
    }

    @Override
    public void update(Client client) {
        String message = "Update client: " + client;

        this.transactional.transaction((x -> {
            this.transactional.getEntityManager().merge(x);
            LOGGER.info(message);
        }), client, message);
    }

    @Override
    public void saveOrUpdate(Client client) {
        String message = "Save or update client: " + client;

        this.transactional.transaction((x -> {
            if (x.getId() == null)
                this.transactional.getEntityManager().persist(x);
            else
                this.transactional.getEntityManager().merge(x);
            LOGGER.info(message);
        }), client, message);
    }

    @Override
    public Collection<Client> getAll() {
        String message = "Find all clients";

        return this.transactional.transaction(() -> {
            Collection<Client> result =
                    this.transactional.getEntityManager()
                            .createNamedQuery("Client.findAll", Client.class).getResultList();
            LOGGER.info(message);
            return result;
        }, message);
    }
}
