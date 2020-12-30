package com.app.bimarunajaya.view.modal;

import com.app.bimarunajaya.entity.PackageEntity;
import com.app.bimarunajaya.entity.ParameterEntity;
import com.app.bimarunajaya.entity.TrackingEntity;
import com.app.bimarunajaya.layout.ContainerLayout;
import com.app.bimarunajaya.service.PackageService;
import com.app.bimarunajaya.service.ParameterService;
import com.app.bimarunajaya.service.TrackingService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class DetailPackageView extends ContainerLayout {

    private Integer id;

    public DetailPackageView(Integer id) {
        super();
        setMargin(false);
        setPadding(false);
        setSpacing(false);

        this.id = id;
        init();
    }

    private void init() {
        initComponent();
        initData();
    }

    private HorizontalLayout contentLayout;
    private TextField txNamaPengirim;
    private TextField txTeleponPengirim;
    private TextArea txAlamatPengirim;
    private TextField txNamaPenerima;
    private TextField txTeleponPenerima;
    private TextArea txAlamatPenerima;
    private TextArea txDeskripsiPaket;
    private TextField txBerat;
    private ComboBox<ParameterEntity> cbDimensi;
    private ComboBox<ParameterEntity> cbPulau;
    private ComboBox<ParameterEntity> cbKota;

    private Label lbBerat;
    private Label lbDimensi;
    private Label lbPajak;
    private Label lbTotal;

    private TextField txStatusDelivery;
    private Grid<TrackingEntity> grid;
    private SimpleDateFormat dateFormat;

    private Button btBatal;

    private void initComponent() {
        contentLayout = new HorizontalLayout();
        contentLayout.setMargin(false);
        contentLayout.setPadding(false);
        contentLayout.setWidthFull();

        txNamaPengirim = new TextField("Nama Pengirim");
        txNamaPengirim.setWidthFull();
        txTeleponPengirim = new TextField("Telepon Pengirim");
        txTeleponPengirim.setWidthFull();
        txAlamatPengirim = new TextArea("Alamat Pengirim");
        txAlamatPengirim.setWidthFull();
        txNamaPenerima = new TextField("Nama Penerima");
        txNamaPenerima.setWidthFull();
        txTeleponPenerima = new TextField("Telepon Penerima");
        txTeleponPenerima.setWidthFull();
        txAlamatPenerima = new TextArea("Alamat Penerima");
        txAlamatPenerima.setWidthFull();
        txDeskripsiPaket = new TextArea("Deskripsi Paket");
        txDeskripsiPaket.setWidthFull();

        cbPulau = new ComboBox<>("Pulau");
        cbPulau.setWidthFull();
        cbKota = new ComboBox<>("Kota");
        cbKota.setWidthFull();
        txBerat = new TextField("Berat (Kg)");
        txBerat.setWidthFull();
        cbDimensi = new ComboBox<>("Dimensi");
        cbDimensi.setWidthFull();

        lbBerat = new Label();
        lbBerat.addClassNames("no-space");
        lbDimensi = new Label();
        lbDimensi.addClassNames("no-space");
        lbPajak = new Label();
        lbPajak.addClassNames("no-space");
        lbTotal = new Label();
        lbTotal.addClassNames("no-space");
        lbTotal.getStyle().set("font-weight", "bold");

        VerticalLayout ly1 = new VerticalLayout(txNamaPengirim, txTeleponPengirim, txAlamatPengirim, txNamaPenerima,
                txTeleponPenerima, txAlamatPenerima, txDeskripsiPaket);
        ly1.setMargin(false);
        ly1.setPadding(false);
        ly1.setSpacing(false);
        ly1.setWidthFull();

        VerticalLayout ly2 = new VerticalLayout(cbPulau, cbKota, txBerat, cbDimensi, new VerticalLayout(),
                    lbBerat, lbDimensi, lbPajak, lbTotal);
        ly2.setMargin(false);
        ly2.setPadding(false);
        ly2.setSpacing(false);
        ly2.setWidthFull();

        VerticalLayout ly3 = new VerticalLayout();
        txStatusDelivery = new TextField("Status Pengiriman");
        txStatusDelivery.setWidthFull();

        dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        grid = new Grid<>();
        grid.addColumn(e -> dateFormat.format(e.getCreateDate())).setHeader("Tanggal");
        grid.addColumn(e -> {
            if (e.getDeliveryStatus() != null)
                return e.getDeliveryStatus().getName().toUpperCase();
            return null;
        }).setHeader("Status Pengiriman");
        grid.addColumn(e -> {
            if (e.getSendBy() != null)
                return e.getSendBy().getFullName().toUpperCase();
            return null;
        }).setHeader("Dikirim Oleh");
        grid.addColumn(e -> {
            if (e.getTransit() != null)
                return e.getTransit().getFullName().toUpperCase();
            return null;
        }).setHeader("Transit");
        grid.addColumn(TrackingEntity::getReceiveBy).setHeader("Diterima Oleh");
        grid.addColumn(e -> {
            if (e.getProgressStatus() != null)
                return e.getProgressStatus().getName().toUpperCase();
            return null;
        }).setHeader("Progress");
        grid.setMaxHeight("300px");
        grid.setWidthFull();
        grid.getColumns().forEach(s -> s.setResizable(true));

        ly3.add(txStatusDelivery, grid);
        ly3.setMargin(false);
        ly3.setPadding(false);
        ly3.setSpacing(false);
        ly3.setWidthFull();
        btBatal = new Button("Batal");
        btBatal.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout ly4 = new HorizontalLayout();
        ly4.setMargin(false);
        ly4.setPadding(false);
        ly4.setWidthFull();
        ly4.setJustifyContentMode(JustifyContentMode.END);
        ly4.add(btBatal);

        contentLayout.add(ly1, ly2, ly3);
        add(contentLayout, ly4);
    }

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private TrackingService trackingService;

    private void initData() {
        lbBerat.setVisible(false);
        lbDimensi.setVisible(false);
        lbPajak.setVisible(false);
        lbTotal.setVisible(false);

        parameterService = getBean(ParameterService.class);
        packageService = getBean(PackageService.class);
        trackingService = getBean(TrackingService.class);

        List<ParameterEntity> dimension = parameterService.getDimension();
        cbDimensi.setItems(dimension);
        cbDimensi.setItemLabelGenerator(e -> e.getName().toUpperCase() + " (" + e.getDescription() + ")");

        List<ParameterEntity> pulau = parameterService.getPulau();
        cbPulau.setItems(pulau);
        cbPulau.setItemLabelGenerator(e -> e.getName().toUpperCase());
        cbPulau.addValueChangeListener(e -> {
            cbKota.clear();
            if (e.getValue() != null) {
                List<ParameterEntity> kota = parameterService.getKota(e.getValue().getId());
                cbKota.setItems(kota);
            }
        });
        cbKota.setItemLabelGenerator(e -> e.getName().toUpperCase());

        PackageEntity packageData = packageService.getById(id);
        txNamaPengirim.setValue(packageData.getSendName());
        txTeleponPengirim.setValue(packageData.getSendPhone());
        txAlamatPengirim.setValue(packageData.getAddressFrom());
        txNamaPenerima.setValue(packageData.getReceiveName());
        txTeleponPenerima.setValue(packageData.getReceivePhone());
        txAlamatPenerima.setValue(packageData.getAddressTo());
        txDeskripsiPaket.setValue(packageData.getPackageDescription());
        txBerat.setValue(packageData.getWeight().toString());
        cbDimensi.setValue(packageData.getDimension());
        cbPulau.setValue(packageData.getKota().getParent());
        List<ParameterEntity> kota = parameterService.getKota(packageData.getKota().getParent().getId());
        cbKota.setItems(kota);
        cbKota.setValue(packageData.getKota());
        String[] p = packageData.getPriceDescription().split("\n");
        lbBerat.setText(p[0]);
        lbDimensi.setText(p[1]);
        lbPajak.setText(p[2]);
        lbTotal.setText(p[3]);

        lbBerat.setVisible(true);
        lbDimensi.setVisible(true);
        lbPajak.setVisible(true);
        lbTotal.setVisible(true);

        txStatusDelivery.setValue(packageData.getLastDeliveryStatus().getName().toUpperCase());

        List<TrackingEntity> l = trackingService.getByPackageId(id);
        grid.setItems(l);
    }

    private Consumer eventClose;

    public void onClose(Consumer eventClose) {
        this.eventClose = eventClose;
        btBatal.addClickListener(e -> eventClose.accept(true));
    }

}
