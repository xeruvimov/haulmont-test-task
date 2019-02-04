package com.haulmont.testtask.data.dao.daoimpl;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.function.Consumer;
import java.util.function.Supplier;

class Transactional<T> {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    private Logger logger;

    Transactional(Logger logger) {
        this.entityManagerFactory = JPAUtil.getEntityManagerFactory();
        this.logger = logger;
    }

    EntityManager getEntityManager() {
        return entityManager;
    }

    void transaction(Consumer<T> action, T object, String message) {
        initTransaction();
        try {
            this.entityTransaction.begin();
            action.accept(object);
            this.entityTransaction.commit();
        } catch (Exception e) {
            logger.error(message + " is fail");
        } finally {
            closeTransaction();
        }
    }

    //TODO delete this?
//    protected <R> T transaction(Function<R, T> action, R object, String message) {
//        initTransaction();
//        T result = null;
//        try {
//            this.entityTransaction.begin();
//            result = action.apply(object);
//            this.entityTransaction.commit();
//        } catch (Exception e) {
//            logger.error(message + " is fail");
//        } finally {
//            closeTransaction();
//        }
//        return result;
//    }

    <R> R transaction(Supplier<R> action, String message) {
        initTransaction();
        R result = null;
        try {
            this.entityTransaction.begin();
            result = action.get();
            this.entityTransaction.commit();
        } catch (Exception e) {
            logger.error(message + " is fail");
        } finally {
            closeTransaction();
        }
        return result;
    }

    private void initTransaction() {
        this.entityManager = entityManagerFactory.createEntityManager();
        this.entityTransaction = this.entityManager.getTransaction();
    }

    private void closeTransaction() {
        if (this.entityTransaction.isActive()) this.entityTransaction.rollback();
        this.entityManager.close();
    }
}
