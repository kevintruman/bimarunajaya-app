package com.app.bimarunajaya.view;

import com.app.bimarunajaya.entity.PackageEntity;
import com.app.bimarunajaya.layout.ContainerLayout;
import com.app.bimarunajaya.model.CbFilter;
import com.app.bimarunajaya.service.BaseService;
import com.app.bimarunajaya.service.PackageService;
import com.app.bimarunajaya.service.TypeService;
import com.app.bimarunajaya.view.modal.DetailPackageView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
public class PackageDataHeadView extends ContainerLayout {

    public PackageDataHeadView() {
        super();
        init();
    }

    private void init() {
        initComponent();
        initData();
    }

    private ComboBox<CbFilter> cbFilter;
    private ComboBox<CbFilter> cbBulan;
    private ComboBox<CbFilter> cbtahun;
    private TextField txSearch;
    private Button btSearch;

    private Button btDetail;
    private Button btConfirm;
    private Button btConfirmAll;
    private Button btExcel;

    private Label lbPengiriman;
    private Label lbTotal;

    private Grid<PackageEntity> grid;
    private SimpleDateFormat dateFormat;

    private Dialog dialog;
    private DetailPackageView addPackageView;

    private BaseService baseService;

    private void initComponent() {
        Date now = new Date();
        baseService = new BaseService();
        cbBulan = new ComboBox<>();
        cbBulan.setItemLabelGenerator(e -> e.getName().toUpperCase());
        cbBulan.setWidth("150px");
        List<CbFilter> b = baseService.listBulan();
        cbBulan.setItems(b);
        cbBulan.setValue(b.stream().filter(e -> e.getId().equals(now.getMonth() + 1)).findFirst().get());

        cbtahun = new ComboBox<>();
        cbtahun.setItemLabelGenerator(e -> e.getName().toUpperCase());
        cbtahun.setWidth("100px");
        List<CbFilter> y = baseService.listTahun();
        cbtahun.setItems(y);
        cbtahun.setValue(y.stream().filter(e -> e.getId().equals(now.getYear() + 1900)).findFirst().get());

        cbFilter = new ComboBox<>();
        cbFilter.setItemLabelGenerator(e -> e.getName().toUpperCase());
        List<CbFilter> s = Arrays.asList(new CbFilter(TypeService.approveAccounting, "Menunggu Konfirmasi"),
                new CbFilter(TypeService.approveHead, "Telah Konfirmasi"));
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
        btConfirmAll = new Button("Konfirmasi Semua", new Icon(VaadinIcon.CHECK));
        btConfirmAll.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btConfirmAll.addClickListener(e -> onConfirmAll());

        btExcel = new Button("Excel", new Icon(VaadinIcon.DOWNLOAD));
        btExcel.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        btExcel.addClickListener(e -> {
            try {
                onDownload();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        HorizontalLayout search = new HorizontalLayout(cbFilter, cbBulan, cbtahun, txSearch, btSearch);
        search.setJustifyContentMode(JustifyContentMode.END);
        search.setWidthFull();
        HorizontalLayout edit = new HorizontalLayout(btDetail, btConfirm, btConfirmAll, btExcel);
        edit.setJustifyContentMode(JustifyContentMode.END);
        edit.setWidthFull();
        VerticalLayout hr = new VerticalLayout(search, edit);
        hr.setWidthFull();

        lbPengiriman = new Label();
        lbTotal = new Label();
        VerticalLayout lyTotal = new VerticalLayout(lbPengiriman, lbTotal);
//        lyTotal.setMargin(false);
//        lyTotal.setPadding(false);
        lyTotal.setAlignItems(Alignment.START);

        dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        grid = new Grid<>();
        grid.addColumn(e -> dateFormat.format(e.getCreateDate())).setHeader("Tanggal Buat");
        grid.addColumn(PackageEntity::getResiNumber).setHeader("Nomor Resi");
        grid.addColumn(e -> e.getCreateBy().getFullName()).setHeader("Dibuat Oleh");
        grid.addColumn(PackageEntity::getWeight).setHeader("Berat (Kg)");
        grid.addColumn(PackageEntity::getPrice).setHeader("Biaya Kirim (Rp)");
        grid.addColumn(e -> e.getLastDeliveryStatus().getName().toUpperCase()).setHeader("Status Pengiriman");
        grid.addColumn(e -> {
            if (e.getApproveStatus() != null)
                return e.getApproveStatus().getName().toUpperCase();
            return "";
        }).setHeader("Status Konfirmasi");
        grid.setSizeFull();
        grid.getColumns().forEach(e -> e.setResizable(true));

        HorizontalLayout h = new HorizontalLayout(lyTotal, hr);
        h.setWidthFull();

        add(h, grid);
        setAlignItems(Alignment.END);
    }

    private void initData() {
        btDetail.setEnabled(false);
        btConfirm.setEnabled(false);
        btExcel.setEnabled(true);

        setItemsPackages();
        if (r.size() == 0) {
            btConfirmAll.setEnabled(false);
            btExcel.setEnabled(false);
        } else {
            if (cbFilter.getValue() != null && cbFilter.getValue().getId().equals(TypeService.approveAccounting))
                btConfirmAll.setEnabled(true);
            btExcel.setEnabled(true);
        }

        lbPengiriman.setText("Jumlah Pengiriman = " + r.size());
        lbTotal.setText("Total Biaya = Rp. " + r.stream().mapToDouble(PackageEntity::getPrice).sum());

        grid.asSingleSelect().addValueChangeListener(e -> {
            btDetail.setEnabled(false);
            btConfirm.setEnabled(false);
            if (e.getValue() != null) {
                btDetail.setEnabled(true);
                if (e.getValue().getApproveStatus().getId().equals(TypeService.approveAccounting))
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

    private void onConfirmAll() {
        for (PackageEntity packageEntity : r) {
            packageService.approveHead(packageEntity.getId());
        }
        initData();
        Notification.show("Berhasil diproses");
    }

    private void onConfirm() {
        packageService.approveHead(grid.asSingleSelect().getValue().getId());
        initData();
        Notification.show("Berhasil diproses");
    }

    private void onDownload() throws IOException {
        String s = baseService.generateExcelService(r, cbBulan.getValue().getName(), cbtahun.getValue().getName());
        UI.getCurrent().getPage().executeJavaScript("window.open('/download/exc?code=" +
                s + "', \"_blank\", \"\");");
    }

    @Autowired
    private PackageService packageService;

    private List<PackageEntity> r = new ArrayList<>();

    private void setItemsPackages() {
        packageService = getBean(PackageService.class);
        CbFilter t = cbFilter.getValue();
        Integer i = null;
        if (t != null) i = t.getId();
        r = packageService.getForAccountingOrHead(txSearch.getValue(), i, cbBulan.getValue().getId(), cbtahun.getValue().getId());
        grid.setItems(r);
    }

}
