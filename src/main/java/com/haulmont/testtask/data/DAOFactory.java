package com.haulmont.testtask.data;

import com.haulmont.testtask.data.dao.ClientDAO;
import com.haulmont.testtask.data.dao.MechanicDAO;
import com.haulmont.testtask.data.dao.OrderDAO;
import com.haulmont.testtask.data.dao.daoimpl.ClientDAOImpl;
import com.haulmont.testtask.data.dao.daoimpl.MechanicDAOImpl;
import com.haulmont.testtask.data.dao.daoimpl.OrderDAOImpl;

public class DAOFactory {
    private static DAOFactory ourInstance = new DAOFactory();

    public static DAOFactory getInstance() {
        return ourInstance;
    }

    private DAOFactory() {
    }

    public static ClientDAO getClientDAO() {
        return new ClientDAOImpl();
    }

    public static MechanicDAO getMechanicDAO() {
        return new MechanicDAOImpl();
    }

    public static OrderDAO getOrderDAO() {
        return new OrderDAOImpl();
    }
}
