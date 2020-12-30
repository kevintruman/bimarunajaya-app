package com.app.bimarunajaya.view;

import com.app.bimarunajaya.entity.TypeEntity;
import com.app.bimarunajaya.entity.UserEntity;
import com.app.bimarunajaya.layout.ContainerLayout;
import com.app.bimarunajaya.service.TypeService;
import com.app.bimarunajaya.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("register")
@PageTitle("Register")
@StyleSheet("/main.css")
public class RegisterView extends ContainerLayout {

    private VerticalLayout mainLayout;

    public RegisterView() {
        super();
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

        initComponent();
        initData();
        addFormUser();
    }

    private Image logo;
    private TextField txUsername;
    private  PasswordField txPassword;
    private PasswordField txRePassword;

    private Button btNext;
    private Button btLogin;

    private TextField txFullName;
    private TextField txEmail;
    private TextField txPhone;
    private ComboBox<UserEntity> cbStation;
    private ComboBox<TypeEntity> cbRole;

    private Button btReg;
    private Button btBack;

    private void initComponent() {
        logo = new Image("/images/bima-full.jpg", "logo");
        logo.setHeight("50px");
        logo.addClassNames("no-space");

        txUsername = new TextField("Username");
        txUsername.setWidthFull();

        txPassword = new PasswordField("Password");
        txPassword.setWidthFull();

        txRePassword = new PasswordField("Ulangi Password");
        txRePassword.setWidthFull();

        btNext = new Button("Selanjutnya");
        btNext.setWidthFull();
        btNext.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btNext.addClickListener(e -> {
            if (checkFormUser()) addFormProfile();
        });

        btLogin = new Button("Login");
        btLogin.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btLogin.addClickListener(e -> UI.getCurrent().navigate("login"));

        txFullName = new TextField("Nama Lengkap");
        txFullName.setWidthFull();

        txEmail = new TextField("Email");
        txEmail.setWidthFull();

        txPhone = new TextField("No. Telepon");
        txPhone.setWidthFull();

        cbStation = new ComboBox<>();
//        cbStation.setItems("Kantor Pusat", "Cabang Setiabudi");
        cbStation.setLabel("Cabang");
        cbStation.setWidthFull();

        cbRole = new ComboBox<>();
//        cbRole.setItems("Kantor Pusat", "Cabang Setiabudi");
        cbRole.setLabel("Tipe");
        cbRole.setWidthFull();

        btReg = new Button("Register");
        btReg.setWidthFull();
        btReg.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btReg.addClickListener(e -> {
            if (checkFormProfile()) {
                doRegister();
                UI.getCurrent().navigate("login");
            }
        });

        btBack = new Button("Kembali");
        btBack.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btBack.addClickListener(e -> addFormUser());
    }

    @Autowired
    private UserService userService;

    @Autowired
    private TypeService typeService;

    private void initData() {
        userService = getBean(UserService.class);
        List<UserEntity> station = userService.getStations("");
        station.removeIf(e -> e.getStatus() == null || !e.getStatus().getId().equals(TypeService.approveAccount));
        cbStation.setItems(station);
        cbStation.setItemLabelGenerator(e -> e.getFullName().toUpperCase());

        typeService = getBean(TypeService.class);
        List<TypeEntity> role = typeService.getByRole();
        cbRole.setItems(role);
        cbRole.setItemLabelGenerator(e -> e.getName().toUpperCase());
    }

    private void addFormUser() {
        mainLayout.removeAll();
        mainLayout.add(logo, txUsername, txPassword, txRePassword, btNext, btLogin);
    }

    private void addFormProfile() {
        mainLayout.removeAll();
        mainLayout.add(logo, txFullName, txEmail, txPhone, cbStation, cbRole, btReg, btBack);
    }

    private boolean checkFormUser() {
        if (txUsername.isEmpty() || txPassword.isEmpty() || txRePassword.isEmpty()) {
            Notification.show("Lengkapi data");
            return false;
        }

        if (!txPassword.getValue().equals(txRePassword.getValue())) {
            Notification.show("Password tidak sama");
            return false;
        }

        UserEntity r = userService.getByUsername(txUsername.getValue());
        if (r != null) {
            Notification.show("Username sudah ada");
            return false;
        }
        return true;
    }

    private boolean checkFormProfile() {
        if (txFullName.isEmpty() || txEmail.isEmpty() || txPhone.isEmpty() ||
                cbStation.getValue() == null || cbRole.getValue() == null) {
            Notification.show("Lengkapi data");
            return false;
        }
        return true;
    }

    private void doRegister() {
        UserEntity s = new UserEntity();
        s.setUsername(txUsername.getValue());
        s.setPassword(txPassword.getValue());
        s.setFullName(txFullName.getValue());
        s.setEmail(txEmail.getValue());
        s.setPhone(txPhone.getValue());
        s.setStation(cbStation.getValue());
        s.setRole(cbRole.getValue());
        userService.register(s);
        Notification.show("Registrasi berhasil, silahkan Login");
    }

}
