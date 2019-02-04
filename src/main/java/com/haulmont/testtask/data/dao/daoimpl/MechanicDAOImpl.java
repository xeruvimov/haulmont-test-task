package com.haulmont.testtask.data.dao.daoimpl;

import com.haulmont.testtask.data.dao.MechanicDAO;
import com.haulmont.testtask.data.entity.Mechanic;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Collection;

public class MechanicDAOImpl implements MechanicDAO {
    private Transactional<Mechanic> transactional;
    private static final Logger LOGGER = LogManager.getLogger(MechanicDAOImpl.class);

    public MechanicDAOImpl() {
        this.transactional = new Transactional<>(LOGGER);
    }

    @Override
    public void insert(Mechanic mechanic) {
        String message = "Merge mechanic " + mechanic;

        this.transactional.transaction((x -> {
            this.transactional.getEntityManager().merge(x);
            LOGGER.info(message);
        }), mechanic, message);
    }

    @Override
    public void delete(Mechanic mechanic) {
        String message = "Delete client " + mechanic;

        this.transactional.transaction((x -> {
            Mechanic removeMechanic = this.transactional.getEntityManager().find(Mechanic.class, x.getId());
            if (removeMechanic != null)
                this.transactional.getEntityManager().remove(removeMechanic);
            LOGGER.info(message);
        }), mechanic, message);
    }

    @Override
    public Mechanic findByID(Long id) {
        String message = "Find mechanic by id: " + id;

        return this.transactional.transaction(() -> {
            Mechanic result = this.transactional.getEntityManager()
                    .createNamedQuery("Mechanic.findById", Mechanic.class)
                    .setParameter("id", id).getSingleResult();
            LOGGER.info(message);
            return result;
        }, message);
    }

    @Override
    public void update(Mechanic mechanic) {
        String message = "Update mechanic: " + mechanic;

        this.transactional.transaction((x -> {
            this.transactional.getEntityManager().merge(x);
            LOGGER.info(message);
        }), mechanic, message);
    }

    @Override
    public void saveOrUpdate(Mechanic mechanic) {
        String message = "Save or update mechanic: " + mechanic;

        this.transactional.transaction((x -> {
            if (x.getId() == null)
                this.transactional.getEntityManager().persist(x);
            else
                this.transactional.getEntityManager().merge(x);
            LOGGER.info(message);
        }), mechanic, message);
    }

    @Override
    public Collection<Mechanic> getAll() {
        String message = "Find all mechanic";

        return this.transactional.transaction(() -> {
            Collection<Mechanic> result =
                    this.transactional.getEntityManager()
                            .createNamedQuery("Mechanic.findAll", Mechanic.class).getResultList();
            LOGGER.info(message);
            return result;
        }, message);
    }
}
