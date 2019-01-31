package com.haulmont.testtask.data.daoimpl;

import com.haulmont.testtask.data.dao.ClientDAO;
import com.haulmont.testtask.data.entity.Client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Collection;

public class ClientDAOImpl implements ClientDAO {
    private EntityManagerFactory entityManagerFactory;

    public ClientDAOImpl() {
        this.entityManagerFactory = JPAUtil.getEntityManagerFactory();
    }

    @Override
    public void insert(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.persist(client);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Persist client: " + client + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    @Override
    public void delete(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.remove(client);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Delete client: " + client + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    @Override
    public Client findByID(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        Client result = null;
        try {
            entityTransaction.begin();
            result = entityManager.createNamedQuery("Client.findById", Client.class)
                    .setParameter("id", id).getSingleResult();
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Find client by id: " + id + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
        return result;
    }

    @Override
    public void update(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.merge(client);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Update client: " + client + " fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    @Override
    public void saveOrUpdate(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            if (client.getId() == null)
                entityManager.persist(client);
            else
                entityManager.merge(client);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Save or update client: " + client + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    @Override
    public Collection<Client> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        Collection<Client> result = null;
        try {
            entityTransaction.begin();
            result = entityManager.createNamedQuery("Client.findAll", Client.class).getResultList();
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Find all clients is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
        return result;
    }
}
