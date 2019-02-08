package com.haulmont.testtask.view;

import com.haulmont.testtask.data.entity.Client;
import com.vaadin.ui.Window;

public class ClientChangeWindow extends Window {
    private Client client;

    public ClientChangeWindow(Client client) {
        this.client = client;


    }
}
