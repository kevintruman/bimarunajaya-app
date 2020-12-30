package com.app.bimarunajaya.view;

import com.app.bimarunajaya.entity.UserEntity;
import com.app.bimarunajaya.service.PackageService;
import com.app.bimarunajaya.service.TypeService;
import com.app.bimarunajaya.service.UserService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
@PageTitle("Login")
@StyleSheet("/main.css")
public class LoginView extends VerticalLayout {

    private VerticalLayout mainLayout;

    public LoginView() {
        setMargin(false);
        setPadding(false);
        setSpacing(false);
        setSizeFull();
        addClassNames("bg-main2view");

        mainLayout = new VerticalLayout();
        mainLayout.setMargin(false);
//        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setWidth("300px");
        mainLayout.addClassNames("main2view", "selected");
        mainLayout.setAlignItems(Alignment.CENTER);

        add(mainLayout);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        addForm();
    }

    private Image logo;
    private TextField txUsername;
    private PasswordField txPassword;

    private void addForm() {
        logo = new Image("/images/bima-full.jpg", "logo");
        logo.setHeight("50px");
//        logo.setWidthFull();
        logo.addClassNames("no-space");

        txUsername = new TextField("Username");
        txUsername.setWidthFull();

        txPassword = new PasswordField("Password");
        txPassword.setWidthFull();

        Button btLogin = new Button("Login");
        btLogin.setWidthFull();
        btLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btLogin.addClickShortcut(Key.ENTER);
        btLogin.addClickListener(e -> onClickLogin());

        Button btReg = new Button("Register");
        btReg.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btReg.addClickListener(e -> UI.getCurrent().navigate("register"));

        mainLayout.add(logo, txUsername, txPassword, btLogin, btReg);
        txUsername.setValue("kasir");
        txPassword.setValue("123");
    }

    @Autowired
    private UserService userService;

    @Autowired
    PackageService packageService;

    private void onClickLogin() {
        if (txUsername.isEmpty() || txPassword.isEmpty()) {
            Notification.show("Username atau Password salah");
            return;
        }
        UserEntity r = userService.login(txUsername.getValue(), txPassword.getValue());
        if (r == null) {
            Notification.show("Username atau Password salah");
            return;
        }
        if (r.getStatus() == null || !r.getStatus().getId().equals(TypeService.approveAccount)) {
            Notification.show("User belum Aktif");
            return;
        }
        UI.getCurrent().getSession().setAttribute("user", r);
        UI.getCurrent().navigate("");
    }

}
