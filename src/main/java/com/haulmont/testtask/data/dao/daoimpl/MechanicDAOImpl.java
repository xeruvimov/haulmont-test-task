package com.haulmont.testtask.data.dao.daoimpl;

import com.haulmont.testtask.data.dao.MechanicDAO;
import com.haulmont.testtask.data.entity.Mechanic;

import java.util.Collection;

public class MechanicDAOImpl extends Transactional<Mechanic> implements MechanicDAO {
    @Override
    public void insert(Mechanic mechanic) {
        String message = "Merge mechanic " + mechanic;

        this.transaction((x -> {
            this.entityManager.merge(x);
            LOGGER.info(message);
        }), mechanic, message);
    }

    @Override
    public void delete(Mechanic mechanic) {
        String message = "Delete client " + mechanic;

        this.transaction((x -> {
            Mechanic removeMechanic = entityManager.find(Mechanic.class, x);
            if (removeMechanic != null)
                entityManager.remove(removeMechanic);
            LOGGER.info(message);
        }), mechanic, message);
    }

    @Override
    public Mechanic findByID(Long id) {
        String message = "Find mechanic by id: " + id;

        return this.transaction(() -> {
            Mechanic result = entityManager.createNamedQuery("Mechanic.findById", Mechanic.class)
                    .setParameter("id", id).getSingleResult();
            LOGGER.info(message);
            return result;
        }, message);
    }

    @Override
    public void update(Mechanic mechanic) {
        String message = "Update mechanic: " + mechanic;

        this.transaction((x -> {
            entityManager.merge(x);
            LOGGER.info(message);
        }), mechanic, message);
    }

    @Override
    public void saveOrUpdate(Mechanic mechanic) {
        String message = "Save or update mechanic: " + mechanic;

        this.transaction((x -> {
            if (x.getId() == null)
                entityManager.persist(x);
            else
                entityManager.merge(x);
            LOGGER.info(message);
        }), mechanic, message);
    }

    @Override
    public Collection<Mechanic> getAll() {
        String message = "Find all mechanic";

        return this.transaction(() -> {
            Collection<Mechanic> result =
                    entityManager.createNamedQuery("Mechanic.findAll", Mechanic.class).getResultList();
            LOGGER.info(message);
            return result;
        }, message);
    }
}
