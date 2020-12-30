package com.app.bimarunajaya.view;

import com.app.bimarunajaya.entity.PackageEntity;
import com.app.bimarunajaya.entity.TrackingEntity;
import com.app.bimarunajaya.layout.ContainerLayout;
import com.app.bimarunajaya.service.PackageService;
import com.app.bimarunajaya.service.TrackingService;
import com.app.bimarunajaya.service.TypeService;
import com.app.bimarunajaya.view.modal.ConfirmPackageView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

@Route("lacak")
@Slf4j
public class LacakView extends ContainerLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        List<String> l = event.getLocation().getQueryParameters().getParameters().get("code");
        if (l != null && l.size() > 0) {
            init(l.get(0));
        }
    }

    private SimpleDateFormat dateFormat;
    private Grid<TrackingEntity> grid;
    private Button btConfirm;

    private void init(String code) {
        dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        grid = new Grid<>();
        grid.addColumn(e -> dateFormat.format(e.getCreateDate())).setHeader("Tanggal");
        grid.addColumn(e -> {
            if (e.getDeliveryStatus().getId().equals(TypeService.statusTransit))
                return "(Proses) Transit di " + e.getTransit().getFullName();
            if (e.getDeliveryStatus().getId().equals(TypeService.statusProcess))
                return "(Proses) Dikirim oleh " + e.getSendBy().getFullName();
            if (e.getDeliveryStatus().getId().equals(TypeService.statusReceived))
                return "(Selesai) Diterima oleh " + e.getReceiveBy();
            return "";
        }).setHeader("Keterangan");
        grid.setWidthFull();

        btConfirm = new Button("Konfirmasi", new Icon(VaadinIcon.CHECK));
        btConfirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btConfirm.setMaxWidth("30%");

        initData(code);
    }

    @Autowired
    private TrackingService trackingService;

    private void initData(String code) {
        removeAll();

        List<TrackingEntity> l = trackingService.getByCode(code);
        l.removeIf(e -> e.getProgressStatus() != null &&
                (e.getProgressStatus().getId().equals(TypeService.approveAccounting) ||
                        e.getProgressStatus().getId().equals(TypeService.approveHead)));
        if (l.size() > 0) {
            add(grid);
            grid.setItems(l);

            TrackingEntity u = trackingService.getLastByPackageId(l.get(0).getId());
            if (u != null && u.getSendBy() != null && getUser() != null &&
                    getUser().getId().equals(u.getSendBy().getId())) {
                btConfirm.addClickListener(e -> onConfirm(u.getPackageData(), code));
                removeAll();
                add(btConfirm, grid);
            }
        } else {
            add(new Label("Data tidak ditemukan"));
        }
    }

    @Autowired
    private PackageService packageService;

    private Dialog dialog;
    private ConfirmPackageView confirmPackageView;

    private void onConfirm(PackageEntity packageEntity, String code) {
        if (packageEntity.getLastDeliveryStatus().getId().equals(TypeService.statusTransit)) {
            packageService.pickPackage(getUser(), grid.asSingleSelect().getValue().getId());
            initData(code);
            Notification.show("Berhasil diproses");
        } else {
            confirmPackageView = new ConfirmPackageView(packageEntity.getId());
            dialog = new Dialog(confirmPackageView);
            dialog.setWidth("400px");
            dialog.open();
            dialog.setCloseOnOutsideClick(false);
            confirmPackageView.onClose((e1) -> {
                dialog.close();
                initData(code);
            });
        }
    }

}
