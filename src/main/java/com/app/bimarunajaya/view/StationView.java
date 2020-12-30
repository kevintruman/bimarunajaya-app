package com.app.bimarunajaya.view;

import com.app.bimarunajaya.entity.UserEntity;
import com.app.bimarunajaya.layout.ContainerLayout;
import com.app.bimarunajaya.service.TypeService;
import com.app.bimarunajaya.service.UserService;
import com.app.bimarunajaya.view.modal.DetailAccountView;
import com.app.bimarunajaya.view.modal.DetailStationView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
public class StationView extends ContainerLayout {

    public StationView() {
        super();
        init();
    }

    private void init() {
        initComponent();
        initData();
    }

    private TextField txSearch;
    private Button btSearch;

    private Button btAdd;
    private Button btDetail;
    private Button btEdit;
    private Button btDelete;
    private Button btApprove;

    private Grid<UserEntity> grid;
    private SimpleDateFormat dateFormat;

    private Dialog dialog;
    private DetailStationView detailStationView;

    private void initComponent() {
        txSearch = new TextField();
        btSearch = new Button("Cari", new Icon(VaadinIcon.SEARCH));
        btSearch.addClickListener(e -> {
            initData();
        });

        btAdd = new Button("Tambah", new Icon(VaadinIcon.PLUS));
        btAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btAdd.addClickListener(e -> onAdd());

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
        HorizontalLayout edit = new HorizontalLayout(btAdd, btDetail, btEdit, btApprove, btDelete);
        edit.setJustifyContentMode(JustifyContentMode.END);
        edit.setWidthFull();
        HorizontalLayout hr = new HorizontalLayout(search, edit);
        hr.setWidthFull();

        dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        grid = new Grid<>();
        grid.addColumn(e -> dateFormat.format(e.getCreateDate())).setHeader("Tanggal Buat");
        grid.addColumn(UserEntity::getFullName).setHeader("Nama");
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

    private void onAdd() {
        detailStationView = new DetailStationView("add", null);
        dialog = new Dialog(detailStationView);
        dialog.setWidth("700px");
        dialog.open();
        dialog.setCloseOnOutsideClick(false);
        detailStationView.onClose((e1) -> {
            dialog.close();
            initData();
        });
    }

    private void onDetail() {
        detailStationView = new DetailStationView("detail", grid.asSingleSelect().getValue().getId());
        dialog = new Dialog(detailStationView);
        dialog.setWidth("700px");
        dialog.open();
        dialog.setCloseOnOutsideClick(false);
        detailStationView.onClose((e1) -> {
            dialog.close();
            initData();
        });
    }

    private void onEdit() {
        detailStationView = new DetailStationView("edit", grid.asSingleSelect().getValue().getId());
        dialog = new Dialog(detailStationView);
        dialog.setWidth("700px");
        dialog.open();
        dialog.setCloseOnOutsideClick(false);
        detailStationView.onClose((e1) -> {
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
        List<UserEntity> r = userService.getStations(txSearch.getValue());
        grid.setItems(r);
    }

}
