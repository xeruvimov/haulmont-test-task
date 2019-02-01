package com.haulmont.testtask.data.dao;

import com.haulmont.testtask.data.entity.Order;

import java.util.Collection;
import java.util.Map;

public interface OrderDAO {
    void insert(Order order);

    void delete(Order order);

    Order findByID(Long id);

    void update(Order order);

    void saveOrUpdate(Order order);

    Collection<Order> getAll();

    Collection<Order> findByDescriptionAndStatusAndClient(Map<String, Object> criteria);
}
