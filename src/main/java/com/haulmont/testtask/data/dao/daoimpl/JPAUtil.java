package com.haulmont.testtask.data.dao.daoimpl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "mainPU";
    private static EntityManagerFactory factory;

    static EntityManagerFactory getEntityManagerFactory() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return factory;
    }
}
