package com.haulmont.testtask;

import com.haulmont.testtask.data.DAOFactory;
import com.haulmont.testtask.data.dao.ClientDAO;
import com.haulmont.testtask.data.dao.OrderDAO;

public class Test {
    public static void main(String[] args) {
        ClientDAO test = DAOFactory.getClientDAO();

        System.out.println(test.getAll());
//        System.out.println(test.findByID(0L));
//        test.delete(test.findByID(0L));
//        System.out.println(test.getAll());

        OrderDAO orderDAO = DAOFactory.getOrderDAO();

        System.out.println(orderDAO.getAll());
        orderDAO.delete(orderDAO.findByID(0L));
        System.out.println(orderDAO.getAll());
    }
}
