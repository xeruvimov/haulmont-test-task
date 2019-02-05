package com.haulmont.testtask.view;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(value = ValoTheme.THEME_NAME)
public class MainUI extends UI {
    private Button buttonClient = new Button("Clients");
    private Button buttonMechanic = new Button("Mechanics");
    private Button buttonOrder = new Button("Orders");

    private Button buttonAdd = new Button("Add new object");
    private Button buttonChange = new Button("Change selected");
    private Button buttonDelete = new Button("Delete selected");

    private Label label = new Label("Test label");
    private Grid grid = new Grid();

    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);

        buttonClient.setWidth("150px");
        buttonMechanic.setWidth("150px");
        buttonOrder.setWidth("150px");

        VerticalLayout leftMenu = new VerticalLayout();
        leftMenu.setSpacing(true);

        leftMenu.addComponent(buttonClient);
        leftMenu.addComponent(buttonMechanic);
        leftMenu.addComponent(buttonOrder);
        leftMenu.setWidth("200px");

        mainLayout.addComponent(leftMenu);

        VerticalLayout rightData = new VerticalLayout();
        rightData.setSpacing(true);


        rightData.addComponent(label);
        rightData.addComponent(grid);

        HorizontalLayout controlButtons = new HorizontalLayout();
        controlButtons.setSpacing(true);

        controlButtons.addComponent(buttonAdd);
        controlButtons.addComponent(buttonChange);
        controlButtons.addComponent(buttonDelete);

        rightData.addComponent(controlButtons);

        mainLayout.addComponent(rightData);

        setContent(mainLayout);
    }
}