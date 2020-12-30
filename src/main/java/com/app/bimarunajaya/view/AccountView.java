package com.app.bimarunajaya.view;

import com.app.bimarunajaya.entity.PackageEntity;
import com.app.bimarunajaya.entity.UserEntity;
import com.app.bimarunajaya.layout.ContainerLayout;
import com.app.bimarunajaya.service.BaseService;
import com.app.bimarunajaya.service.PackageService;
import com.app.bimarunajaya.service.TypeService;
import com.app.bimarunajaya.service.UserService;
import com.app.bimarunajaya.view.modal.AddPackageView;
import com.app.bimarunajaya.view.modal.DetailAccountView;
import com.app.bimarunajaya.view.modal.DetailPackageView;
import com.app.bimarunajaya.view.modal.EditPackageView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
public class AccountView extends ContainerLayout {

    public AccountView() {
        super();
        init();
    }

    private void init() {
        initComponent();
        initData();
    }

    private TextField txSearch;
    private Button btSearch;

    private Button btDetail;
    private Button btEdit;
    private Button btDelete;
    private Button btApprove;

    private Grid<UserEntity> grid;
    private SimpleDateFormat dateFormat;

    private Dialog dialog;
    private DetailAccountView detailAccountView;

    private void initComponent() {
        txSearch = new TextField();
        btSearch = new Button("Cari", new Icon(VaadinIcon.SEARCH));
        btSearch.addClickListener(e -> {
            initData();
        });

        btDetail = new Button("Detail", new Icon(VaadinIcon.INFO_CIRCLE_O));
        btDetail.addClickListener(e -> onDetail());

        btEdit = new Button("Edit", new Icon(VaadinIcon.EDIT));
        btEdit.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        btEdit.addClickListener(e -> onEdit());

        btDelete = new Button("Nonaktif", new Icon(VaadinIcon.CLOSE));
        btDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btDelete.addClickListener(e -> onDelete());

        btApprove = new Button("Aktifkan", new Icon(VaadinIcon.CHECK));
        btApprove.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btApprove.addClickListener(e -> onConfirm());

        HorizontalLayout search = new HorizontalLayout(txSearch, btSearch);
        search.setJustifyContentMode(JustifyContentMode.START);
        search.setWidthFull();
        HorizontalLayout edit = new HorizontalLayout(btDetail, btEdit, btApprove, btDelete);
        edit.setJustifyContentMode(JustifyContentMode.END);
        edit.setWidthFull();
        HorizontalLayout hr = new HorizontalLayout(search, edit);
        hr.setWidthFull();

        dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        grid = new Grid<>();
        grid.addColumn(e -> dateFormat.format(e.getCreateDate())).setHeader("Tanggal Buat");
        grid.addColumn(UserEntity::getFullName).setHeader("Nama");
        grid.addColumn(e -> e.getRole().getName().toUpperCase()).setHeader("Jabatan");
        grid.addColumn(e -> e.getStation().getFullName().toUpperCase()).setHeader("Lokasi");
        grid.addColumn(e -> {
            if (e.getStatus() != null) return e.getStatus().getName().toUpperCase();
            return "";
        }).setHeader("Status");
        grid.setSizeFull();
        grid.getColumns().forEach(s -> s.setResizable(true));

        add(hr, grid);
    }

    private void initData() {
        btDetail.setEnabled(false);
        btEdit.setEnabled(false);
        btDelete.setEnabled(false);
        btApprove.setEnabled(false);

        setItemsPackages();

        grid.asSingleSelect().addValueChangeListener(e -> {
            btDetail.setEnabled(false);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);
            btApprove.setEnabled(false);

            if (e.getValue() != null) {
                btDetail.setEnabled(true);
                btEdit.setEnabled(true);
                if (e.getValue().getStatus() == null ||
                        !e.getValue().getStatus().getId().equals(TypeService.approveAccount)) btApprove.setEnabled(true);
                if (e.getValue().getStatus() != null &&
                        e.getValue().getStatus().getId().equals(TypeService.approveAccount)) btDelete.setEnabled(true);
            }
        });
    }

    private void onDetail() {
        detailAccountView = new DetailAccountView(false, grid.asSingleSelect().getValue().getId());
        dialog = new Dialog(detailAccountView);
        dialog.setWidth("700px");
        dialog.open();
        dialog.setCloseOnOutsideClick(false);
        detailAccountView.onClose((e1) -> {
            dialog.close();
            initData();
        });
    }

    private void onEdit() {
        detailAccountView = new DetailAccountView(true, grid.asSingleSelect().getValue().getId());
        dialog = new Dialog(detailAccountView);
        dialog.setWidth("700px");
        dialog.open();
        dialog.setCloseOnOutsideClick(false);
        detailAccountView.onClose((e1) -> {
            dialog.close();
            initData();
        });
    }

    private void onDelete() {
        userService.notActiveUser(grid.asSingleSelect().getValue().getId());
        Notification.show("Berhasil tersimpan");
        initData();
    }

    private void onConfirm() {
        userService.approveUser(grid.asSingleSelect().getValue().getId());
        Notification.show("Berhasil tersimpan");
        initData();
    }

    @Autowired
    private UserService userService;

    private void setItemsPackages() {
        userService = getBean(UserService.class);
        List<UserEntity> r = userService.getUsers(txSearch.getValue());
        grid.setItems(r);
    }

}
