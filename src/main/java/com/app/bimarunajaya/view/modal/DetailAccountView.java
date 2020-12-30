package com.app.bimarunajaya.view.modal;

import com.app.bimarunajaya.entity.PackageEntity;
import com.app.bimarunajaya.entity.ParameterEntity;
import com.app.bimarunajaya.entity.TypeEntity;
import com.app.bimarunajaya.entity.UserEntity;
import com.app.bimarunajaya.layout.ContainerLayout;
import com.app.bimarunajaya.repo.UserRepo;
import com.app.bimarunajaya.service.PackageService;
import com.app.bimarunajaya.service.ParameterService;
import com.app.bimarunajaya.service.TypeService;
import com.app.bimarunajaya.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class DetailAccountView extends ContainerLayout {

    private Integer id;
    private boolean isEdit;

    public DetailAccountView(boolean isEdit, Integer id) {
        super();
        setMargin(false);
        setPadding(false);
        setSpacing(false);

        this.id = id;
        this.isEdit = isEdit;
        init();
    }

    private void init() {
        initComponent();
        initData();
    }

    private HorizontalLayout contentLayout;
    private TextField txUsername;
    private PasswordField txPassword;
    private PasswordField txRePassword;
    private TextField txEmail;
    private TextField txPhone;
    private TextField txFullName;
    private ComboBox<TypeEntity> cbRole;
    private ComboBox<UserEntity> cbStation;

    private Button btSimpan;
    private Button btApprove;
    private Button btDelete;
    private Button btBatal;

    private void initComponent() {
        contentLayout = new HorizontalLayout();
        contentLayout.setMargin(false);
        contentLayout.setPadding(false);
        contentLayout.setWidthFull();

        txUsername = new TextField("Username");
        txUsername.setWidthFull();
        txPassword = new PasswordField("Password");
        txPassword.setWidthFull();
        txRePassword = new PasswordField("Ulangi Password");
        txRePassword.setWidthFull();
        txEmail = new TextField("Email");
        txEmail.setWidthFull();
        txPhone = new TextField("Telepon");
        txPhone.setWidthFull();
        txFullName = new TextField("Nama Lengkap");
        txFullName.setWidthFull();
        cbRole = new ComboBox<>("Jabatan");
        cbRole.setWidthFull();
        cbStation = new ComboBox<>("Lokasi");
        cbStation.setWidthFull();

        VerticalLayout ly1 = new VerticalLayout(txUsername, txFullName, txEmail, txPhone);
        ly1.setMargin(false);
        ly1.setPadding(false);
        ly1.setSpacing(false);
        ly1.setWidthFull();

        VerticalLayout ly2;
        ly2 = new VerticalLayout(cbRole, cbStation, txPassword, txRePassword);
        ly2.setMargin(false);
        ly2.setPadding(false);
        ly2.setSpacing(false);
        ly2.setWidthFull();

        btSimpan = new Button("Simpan");
        btSimpan.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btApprove = new Button("Aktifkan", new Icon(VaadinIcon.CHECK));
        btApprove.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btDelete = new Button("Nonaktif", new Icon(VaadinIcon.CLOSE));
        btDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btDelete.addClickListener(e -> onNotActive());
        btBatal = new Button("Batal");
        btBatal.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        btSimpan.addClickListener(e -> {
            if (validateForm()) doSave();
        });
        btApprove.addClickListener(e -> onConfirm());

        HorizontalLayout ly4 = new HorizontalLayout();
        ly4.setMargin(false);
        ly4.setPadding(false);
        ly4.setWidthFull();
        ly4.setJustifyContentMode(JustifyContentMode.END);
        ly4.add(btSimpan, btApprove, btDelete, btBatal);

        contentLayout.add(ly1, ly2);
        add(contentLayout, ly4);
    }

    @Autowired
    private TypeService typeService;
    @Autowired
    private UserService userService;

    private void initData() {
        userService = getBean(UserService.class);
        typeService = getBean(TypeService.class);

        List<TypeEntity> roles = typeService.getByRole();
        cbRole.setItems(roles);
        cbRole.setItemLabelGenerator(e -> e.getName().toUpperCase());

        List<UserEntity> stations = userService.getStations("");
        cbStation.setItems(stations);
        cbStation.setItemLabelGenerator(e -> e.getFullName().toUpperCase());

        btSimpan.setVisible(false);
        txPassword.setVisible(false);
        txRePassword.setVisible(false);
        btApprove.setVisible(false);
        btDelete.setVisible(false);

        UserEntity s = userService.getById(id);
        txUsername.setValue(s.getUsername());
//            txPassword.setValue();
//            txRePassword.setValue();
        txEmail.setValue(s.getEmail());
        txPhone.setValue(s.getPhone());
        txFullName.setValue(s.getFullName());
        cbRole.setValue(s.getRole());
        cbStation.setValue(s.getStation());

        if (isEdit) {
            btSimpan.setVisible(true);
            txPassword.setVisible(true);
            txRePassword.setVisible(true);

            if (s.getStatus() == null ||
                    !s.getStatus().getId().equals(TypeService.approveAccount)) btApprove.setVisible(true);
            if (s.getStatus() != null &&
                    s.getStatus().getId().equals(TypeService.approveAccount)) btDelete.setVisible(true);
        }
    }

    private boolean validateForm() {
        if (txUsername.isEmpty() || txPassword.isEmpty() || txRePassword.isEmpty() ||
                txEmail.isEmpty() || txPhone.isEmpty() || txFullName.isEmpty() ||
                cbRole.getValue() == null || cbStation.getValue() == null) {
            Notification.show("Lengkapi data");
            return false;
        }
        if (!txPassword.getValue().equals(txRePassword.getValue())) {
            Notification.show("Password tidak sama");
            return false;
        }
        return true;
    }

    private void doSave() {
        UserEntity s = new UserEntity();
        s.setId(id);
        s.setUsername(txUsername.getValue());
        s.setPassword(txPassword.getValue());
        s.setEmail(txEmail.getValue());
        s.setPhone(txPhone.getValue());
        s.setFullName(txFullName.getValue());
        s.setRole(cbRole.getValue());
        s.setStation(cbStation.getValue());

        s = userService.saveUser(s);
        Notification.show("Berhasil Tersimpan");
        eventClose.res(s);
    }

    private void onConfirm() {
        UserEntity s = userService.approveUser(id);
        Notification.show("Berhasil Tersimpan");
        eventClose.res(s);
    }

    private void onNotActive() {
        UserEntity s = userService.notActiveUser(id);
        Notification.show("Berhasil Tersimpan");
        eventClose.res(s);
    }

    private ResPackage eventClose;

    public void onClose(ResPackage eventClose) {
        this.eventClose = eventClose;
        btBatal.addClickListener(e -> this.eventClose.res(null));
    }

    @FunctionalInterface
    public interface ResPackage {
        void res(UserEntity r);
    }

}
