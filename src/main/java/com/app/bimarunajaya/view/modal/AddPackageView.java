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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
public class AddPackageView extends ContainerLayout {

    private Integer id;

    public AddPackageView(Integer id) {
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
    private Button btHitung;

    private Label lbBerat;
    private Label lbDimensi;
    private Label lbPajak;
    private Label lbTotal;

    private Button btSimpan;
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

        btHitung = new Button("Kalkulasi");
        btHitung.setWidthFull();
        btHitung.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


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

        VerticalLayout ly2;
        ly2 = new VerticalLayout(cbPulau, cbKota, txBerat, cbDimensi, new VerticalLayout(),
                btHitung, new VerticalLayout(), lbBerat, lbDimensi, lbPajak, lbTotal);

        ly2.setMargin(false);
        ly2.setPadding(false);
        ly2.setSpacing(false);
        ly2.setWidthFull();

        btSimpan = new Button("Simpan");
        btSimpan.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btBatal = new Button("Batal");
        btBatal.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout ly4 = new HorizontalLayout();
        ly4.setMargin(false);
        ly4.setPadding(false);
        ly4.setWidthFull();
        ly4.setJustifyContentMode(JustifyContentMode.END);
        ly4.add(btSimpan, btBatal);

        contentLayout.add(ly1, ly2);
        add(contentLayout, ly4);
    }

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private PackageService packageService;

    private boolean isCalculate = false;

    double total = .0;

    private void initData() {
        isCalculate = false;
        btSimpan.setEnabled(false);

        lbBerat.setVisible(false);
        lbDimensi.setVisible(false);
        lbPajak.setVisible(false);
        lbTotal.setVisible(false);

        parameterService = getBean(ParameterService.class);
        packageService = getBean(PackageService.class);

        List<ParameterEntity> dimension = parameterService.getDimension();
        cbDimensi.setItems(dimension);
        cbDimensi.setItemLabelGenerator(e -> e.getName().toUpperCase() + " (" + e.getDescription() + ")");

        List<ParameterEntity> pulau = parameterService.getPulau();
        cbPulau.setItems(pulau);
        cbPulau.setItemLabelGenerator(e -> e.getName().toUpperCase());
        cbPulau.addValueChangeListener(e -> {
            cbKota.clear();
            isCalculate = false;
            if (e.getValue() != null) {
                List<ParameterEntity> kota = parameterService.getKota(e.getValue().getId());
                cbKota.setItems(kota);
            }
        });
        cbKota.setItemLabelGenerator(e -> e.getName().toUpperCase());
        cbKota.addValueChangeListener(e -> isCalculate = false);

        btHitung.addClickListener(e -> {
            if (validateForm()) {
                String berat;
                ParameterEntity paramBerat;
                int hargaBerat;
                ParameterEntity paramPajak;
                int hargaBeratDimensi;
                double hargaPajak;
                try {
                    berat = txBerat.getValue();
                    paramBerat = cbKota.getValue();
                    hargaBerat = Integer.valueOf(berat) * Integer.valueOf(paramBerat.getValue());

                    paramPajak = parameterService.getTax();
                    hargaBeratDimensi = hargaBerat + Integer.valueOf(cbDimensi.getValue().getValue());
                    hargaPajak =  hargaBeratDimensi * Double.valueOf(paramPajak.getValue());

                    total = hargaBeratDimensi + hargaPajak;
                } catch (Exception ex) {
                    Notification.show("Masukkan Angka pada field Berat");
                    return;
                }

                lbBerat.setText("Berat: " + berat + " Kg x Rp. " + paramBerat.getValue() + " = Rp. " + hargaBerat);
                lbDimensi.setText("Dimensi: " + cbDimensi.getValue().getName().toUpperCase() +
                        " = Rp. " + cbDimensi.getValue().getValue());
                lbPajak.setText("Pajak: " + (Double.valueOf(paramPajak.getValue()) * 100) + "% = Rp. " + hargaPajak);
                lbTotal.setText("Total: Rp. " + total);

                lbBerat.setVisible(true);
                lbDimensi.setVisible(true);
                lbPajak.setVisible(true);
                lbTotal.setVisible(true);
                isCalculate = true;
                btSimpan.setEnabled(true);
            }
        });

        btSimpan.addClickListener(e -> {
            if (validateForm() && isCalculate) {
                doSave();
            } else {
                Notification.show("Biaya belum dikalkulasi");
            }
        });
    }

    private boolean validateForm() {
        ParameterEntity min = parameterService.getMinWeight();
        if (txNamaPengirim.isEmpty() || txTeleponPengirim.isEmpty() || txAlamatPengirim.isEmpty() ||
                txNamaPenerima.isEmpty() || txTeleponPenerima.isEmpty() || txAlamatPenerima.isEmpty() ||
                txDeskripsiPaket.isEmpty() || txBerat.isEmpty() || cbDimensi.getValue() == null ||
                cbPulau.getValue() == null || cbKota.getValue() == null) {
            Notification.show("Lengkapi data");
            return false;
        }
        if (Double.valueOf(txBerat.getValue()) < Double.valueOf(min.getValue())) {
            Notification.show("Berat tidak boleh kurang dari " + min.getValue() + " kg");
            return false;
        }
        return true;
    }

    private void doSave() {
        PackageEntity s = new PackageEntity();
        s.setLastPosition(getUser());
        s.setLastStation(getUser().getStation());
        s.setSendName(txNamaPengirim.getValue());
        s.setSendPhone(txTeleponPengirim.getValue());
        s.setAddressFrom(txAlamatPengirim.getValue());
        s.setReceiveName(txNamaPenerima.getValue());
        s.setReceivePhone(txTeleponPenerima.getValue());
        s.setAddressTo(txAlamatPenerima.getValue());
        s.setPackageDescription(txDeskripsiPaket.getValue());
        s.setWeight(Double.parseDouble(txBerat.getValue()));
        s.setKota(cbKota.getValue());
        s.setDimension(cbDimensi.getValue());
        s.setPrice(total);
        s.setPriceDescription(lbBerat.getText() + "\n" + lbDimensi.getText() + "\n" +
                lbPajak.getText() + "\n" + lbTotal.getText());
        s.setCreateBy(getUser());
        s.setStationBy(getUser().getStation());

        s = packageService.save(s);
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
        void res(PackageEntity r);
    }

}
