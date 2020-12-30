package com.app.bimarunajaya.layout;

import com.app.bimarunajaya.config.ContextConfig;
import com.app.bimarunajaya.entity.UserEntity;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@StyleSheet("/main.css")
public abstract class ContainerLayout extends VerticalLayout {

    private ContextConfig ctx;

    public ContainerLayout() {
        setMargin(false);
        setSizeFull();

        ctx = new ContextConfig();
    }

    protected UserEntity getUser() {
        return (UserEntity) UI.getCurrent().getSession().getAttribute("user");
    }

    protected <T> T getBean(Class<T> clazz) {
        return ctx.context.getBean(clazz);
    }

}
