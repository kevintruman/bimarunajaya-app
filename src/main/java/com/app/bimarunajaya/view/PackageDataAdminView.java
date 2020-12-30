package com.app.bimarunajaya.view;

import com.app.bimarunajaya.entity.PackageEntity;
import com.app.bimarunajaya.layout.ContainerLayout;
import com.app.bimarunajaya.service.BaseService;
import com.app.bimarunajaya.service.PackageService;
import com.app.bimarunajaya.service.TypeService;
import com.app.bimarunajaya.view.modal.AddPackageView;
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
public class PackageDataAdminView extends ContainerLayout {

    public PackageDataAdminView() {
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

    private Grid<PackageEntity> grid;
    private SimpleDateFormat dateFormat;

    private Dialog dialog;
    private AddPackageView addPackageView;
    private DetailPackageView detailPackageView;
    private EditPackageView editPackageView;

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

        btDelete = new Button("Hapus", new Icon(VaadinIcon.TRASH));
        btDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btDelete.addClickListener(e -> onDelete());

        HorizontalLayout search = new HorizontalLayout(txSearch, btSearch);
        search.setJustifyContentMode(JustifyContentMode.START);
        search.setWidthFull();
        HorizontalLayout edit = new HorizontalLayout(btAdd, btDetail, btEdit, btDelete);
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
        grid.getColumns().forEach(s -> s.setResizable(true));

        add(hr, grid);
    }

    private void initData() {
        btDetail.setEnabled(false);
        btEdit.setEnabled(false);
        btDelete.setEnabled(false);

        setItemsPackages();

        grid.asSingleSelect().addValueChangeListener(e -> {
            btDetail.setEnabled(false);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);

            if (e.getValue() != null) {
                btDetail.setEnabled(true);
                if (e.getValue().getLastDeliveryStatus().getId().equals(TypeService.statusTransit)) {
                    btEdit.setEnabled(true);
                    btDelete.setEnabled(true);
                }
            }
        });
    }

    private void onAdd() {
        addPackageView = new AddPackageView(null);
        dialog = new Dialog(addPackageView);
        dialog.setWidth("700px");
        dialog.open();
        dialog.setCloseOnOutsideClick(false);
        addPackageView.onClose((e1) -> {
            dialog.close();
            initData();
            if (e1 != null) {
                try {
                    generateResi(e1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onDetail() {
        detailPackageView = new DetailPackageView(grid.asSingleSelect().getValue().getId());
        dialog = new Dialog(detailPackageView);
        dialog.setWidth("900px");
        dialog.open();
        dialog.setCloseOnOutsideClick(false);
        detailPackageView.onClose((e1) -> {
            dialog.close();
            initData();
        });
    }

    private void onEdit() {
        editPackageView = new EditPackageView(grid.asSingleSelect().getValue().getId());
        dialog = new Dialog(editPackageView);
        dialog.setWidth("700px");
        dialog.open();
        dialog.setCloseOnOutsideClick(false);
        editPackageView.onClose((e1) -> {
            dialog.close();
            initData();
        });
    }

    private void onDelete() {
        packageService.delete(grid.asSingleSelect().getValue().getId());
        Notification.show("Data terhapus");
        initData();
    }

    private void generateResi(PackageEntity packageEntity) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("blank.png");
        if (is == null) {
            log.info("file not found");
        } else {
            BufferedImage image = ImageIO.read(is);
            Graphics g = image.getGraphics();

//            String s = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
            VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
            HttpServletRequest httpServletRequest = ((VaadinServletRequest)vaadinRequest).getHttpServletRequest();
            String requestUrl = httpServletRequest.getRequestURL().toString();

            String path = new BaseService().getPathCode(requestUrl + "lacak?code=" + packageEntity.getResiNumber(),
                    packageEntity.getResiNumber());
            BufferedImage qrCode = ImageIO.read(new File(path));
            g.drawImage(qrCode, -20, -20, null);

            Font font = new Font("Arial", Font.PLAIN, 24);
            g.setFont(font);
            g.setColor(Color.BLACK);

            g.drawString("From :", 30, 240);
            g.drawString(packageEntity.getSendName(), 30, 265);
            g.drawString(packageEntity.getSendPhone(), 30, 290);
            g.drawString(packageEntity.getAddressFrom(), 30, 315);

            g.drawString("To :", 30, 345);
            g.drawString(packageEntity.getReceiveName(), 30, 370);
            g.drawString(packageEntity.getReceivePhone(), 30, 395);
            g.drawString(packageEntity.getAddressTo(), 30, 420);

            g.drawString("No. Resi : " + packageEntity.getResiNumber(), 240, 70);
            g.drawString("Deskripsi : " + packageEntity.getPackageDescription(), 240, 100);
            g.drawString("Berat : " + packageEntity.getWeight() + " Kg", 240, 130);

            ImageIO.write(image, "png",
                    new File(BaseService.pathQRAddr + packageEntity.getResiNumber() + ".PNG"));

            UI.getCurrent().getPage().executeJavaScript("window.open('/download?code=" +
                    packageEntity.getResiNumber() + "', \"_blank\", \"\");");
        }
    }

    @Autowired
    private PackageService packageService;

    private void setItemsPackages() {
        packageService = getBean(PackageService.class);
        List<PackageEntity> r = packageService.getForAdmin(txSearch.getValue());
        grid.setItems(r);
    }

}
