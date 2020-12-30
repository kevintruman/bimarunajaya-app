package com.app.bimarunajaya.view.modal;

import com.app.bimarunajaya.entity.TypeEntity;
import com.app.bimarunajaya.entity.UserEntity;
import com.app.bimarunajaya.layout.ContainerLayout;
import com.app.bimarunajaya.service.TypeService;
import com.app.bimarunajaya.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class DetailStationView extends ContainerLayout {

    private Integer id;
    private String ev;

    public DetailStationView(String ev, Integer id) {
        super();
        setMargin(false);
        setPadding(false);
        setSpacing(false);

        this.id = id;
        this.ev = ev;
        init();
    }

    private void init() {
        initComponent();
        initData();
    }

    private HorizontalLayout contentLayout;
    private TextField txEmail;
    private TextField txPhone;
    private TextField txFullName;

    private Button btSimpan;
    private Button btApprove;
    private Button btDelete;
    private Button btBatal;

    private void initComponent() {
        contentLayout = new HorizontalLayout();
        contentLayout.setMargin(false);
        contentLayout.setPadding(false);
        contentLayout.setWidthFull();

        txEmail = new TextField("Email");
        txEmail.setWidthFull();
        txPhone = new TextField("Telepon");
        txPhone.setWidthFull();
        txFullName = new TextField("Nama Lengkap");
        txFullName.setWidthFull();

        VerticalLayout ly1 = new VerticalLayout(txFullName, txEmail, txPhone);
        ly1.setMargin(false);
        ly1.setPadding(false);
        ly1.setSpacing(false);
        ly1.setWidthFull();

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

        contentLayout.add(ly1);
        add(contentLayout, ly4);
    }

    @Autowired
    private UserService userService;

    private void initData() {
        userService = getBean(UserService.class);

        btSimpan.setVisible(false);
        btApprove.setVisible(false);
        btDelete.setVisible(false);

        if (ev.equals("edit") || ev.equals("detail")) {
            UserEntity s = userService.getById(id);
            txEmail.setValue(s.getEmail());
            txPhone.setValue(s.getPhone());
            txFullName.setValue(s.getFullName());

            if (ev.equals("edit")) {
                btSimpan.setVisible(true);
                if (s.getStatus() == null ||
                        !s.getStatus().getId().equals(TypeService.approveAccount)) btApprove.setVisible(true);
                if (s.getStatus() != null &&
                        s.getStatus().getId().equals(TypeService.approveAccount)) btDelete.setVisible(true);
            }
        }
        if (ev.equals("add")) btSimpan.setVisible(true);
    }

    private boolean validateForm() {
        if (txEmail.isEmpty() || txPhone.isEmpty() || txFullName.isEmpty()) {
            Notification.show("Lengkapi data");
            return false;
        }
        return true;
    }

    private void doSave() {
        UserEntity s = new UserEntity();
        s.setId(id);
        s.setEmail(txEmail.getValue());
        s.setPhone(txPhone.getValue());
        s.setFullName(txFullName.getValue());

        s = userService.saveStation(s);
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
