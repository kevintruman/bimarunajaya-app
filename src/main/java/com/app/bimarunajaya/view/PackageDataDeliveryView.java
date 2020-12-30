package com.app.bimarunajaya.view;

import com.app.bimarunajaya.entity.PackageEntity;
import com.app.bimarunajaya.layout.ContainerLayout;
import com.app.bimarunajaya.model.CbFilter;
import com.app.bimarunajaya.service.PackageService;
import com.app.bimarunajaya.service.TypeService;
import com.app.bimarunajaya.view.modal.AddPackageView;
import com.app.bimarunajaya.view.modal.ConfirmPackageView;
import com.app.bimarunajaya.view.modal.DetailPackageView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackageDataDeliveryView extends ContainerLayout {

    public PackageDataDeliveryView() {
        super();
        init();
    }

    private void init() {
        initComponent();
        initData();
    }

    private ComboBox<CbFilter> cbFilter;
    private TextField txSearch;
    private Button btSearch;

    private Button btDetail;
    private Button btConfirm;

    private Grid<PackageEntity> grid;
    private SimpleDateFormat dateFormat;

    private Dialog dialog;
    private DetailPackageView addPackageView;

    private void initComponent() {
        cbFilter = new ComboBox<>();
        cbFilter.setItemLabelGenerator(e -> e.getName().toUpperCase());
        List<CbFilter> s = Arrays.asList(new CbFilter(TypeService.statusTransit, "Transit"),
                new CbFilter(TypeService.statusProcess, "Proses"));
        cbFilter.setItems(s);
        cbFilter.setValue(s.get(0));
        txSearch = new TextField();
        btSearch = new Button("Cari", new Icon(VaadinIcon.SEARCH));
        btSearch.addClickListener(e -> {
            initData();
        });

        btDetail = new Button("Detail", new Icon(VaadinIcon.INFO_CIRCLE_O));
        btDetail.addClickListener(e -> onDetail());

        btConfirm = new Button("Konfirmasi", new Icon(VaadinIcon.CHECK));
        btConfirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btConfirm.addClickListener(e -> onConfirm());

        HorizontalLayout search = new HorizontalLayout(cbFilter, txSearch, btSearch);
        search.setJustifyContentMode(JustifyContentMode.START);
        search.setWidthFull();
        HorizontalLayout edit = new HorizontalLayout(btDetail, btConfirm);
        edit.setJustifyContentMode(JustifyContentMode.END);
        edit.setWidthFull();
        HorizontalLayout hr = new HorizontalLayout(search, edit);
        hr.setWidthFull();


        dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        grid = new Grid<>();
        grid.addColumn(e -> dateFormat.format(e.getCreateDate())).setHeader("Tanggal Buat");
        grid.addColumn(PackageEntity::getResiNumber).setHeader("Nomor Resi");
        grid.addColumn(e -> e.getCreateBy().getFullName()).setHeader("Dibuat Oleh");
        grid.addColumn(PackageEntity::getSendName).setHeader("Nama Pengirim");
        grid.addColumn(PackageEntity::getReceiveName).setHeader("Nama Penerima");
        grid.addColumn(PackageEntity::getWeight).setHeader("Berat (Kg)");
        grid.addColumn(PackageEntity::getPrice).setHeader("Biaya Kirim (Rp)");
        grid.addColumn(e -> e.getLastDeliveryStatus().getName().toUpperCase()).setHeader("Status Pengiriman");
        grid.setSizeFull();
        grid.getColumns().forEach(e -> e.setResizable(true));

        add(hr, grid);
    }

    private void initData() {
        btDetail.setEnabled(false);
        btConfirm.setEnabled(false);

        setItemsPackages();

        grid.asSingleSelect().addValueChangeListener(e -> {
            btDetail.setEnabled(false);
            btConfirm.setEnabled(false);
            if (e.getValue() != null) {
                btDetail.setEnabled(true);
                btConfirm.setEnabled(true);
            }
        });
    }

    private void onDetail() {
        addPackageView = new DetailPackageView(grid.asSingleSelect().getValue().getId());
        dialog = new Dialog(addPackageView);
        dialog.setWidth("900px");
        dialog.open();
        dialog.setCloseOnOutsideClick(false);
        addPackageView.onClose((e1) -> {
            dialog.close();
            initData();
        });
    }

    private ConfirmPackageView confirmPackageView;

    private void onConfirm() {
        if (grid.asSingleSelect().getValue().getLastDeliveryStatus().getId().equals(TypeService.statusTransit)) {
            packageService.pickPackage(getUser(), grid.asSingleSelect().getValue().getId());
            initData();
            Notification.show("Berhasil diproses");
        } else {
            confirmPackageView = new ConfirmPackageView(grid.asSingleSelect().getValue().getId());
            dialog = new Dialog(confirmPackageView);
            dialog.setWidth("400px");
            dialog.open();
            dialog.setCloseOnOutsideClick(false);
            confirmPackageView.onClose((e1) -> {
                dialog.close();
                initData();
            });
        }
    }

    @Autowired
    private PackageService packageService;

    private void setItemsPackages() {
        packageService = getBean(PackageService.class);
        List<PackageEntity> r = new ArrayList<>();
        if (cbFilter.getValue().getId().equals(TypeService.statusTransit))
            r = packageService.getForDeliveryTransit(getUser(), txSearch.getValue());
        if (cbFilter.getValue().getId().equals(TypeService.statusProcess))
            r = packageService.getForDeliveryProcess(getUser(), txSearch.getValue());

        grid.setItems(r);
    }

}
