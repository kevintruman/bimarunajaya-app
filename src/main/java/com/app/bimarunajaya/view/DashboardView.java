package com.app.bimarunajaya.view;

import com.app.bimarunajaya.entity.PackageEntity;
import com.app.bimarunajaya.layout.ContainerLayout;
import com.app.bimarunajaya.model.CardData;
import com.app.bimarunajaya.model.MenuName;
import com.app.bimarunajaya.service.PackageService;
import com.app.bimarunajaya.service.TypeService;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DashboardView extends ContainerLayout {

    public DashboardView() {
        super();
        init();
    }

    private void init() {
        initComponent();
        initData();
    }

    private HorizontalLayout cardDashboardLayout;

    private void initComponent() {
        cardDashboardLayout = new HorizontalLayout();
        cardDashboardLayout.setWidthFull();

        add(cardDashboardLayout);
    }

    @Autowired
    private PackageService packageService;

    private void initData() {
        packageService = getBean(PackageService.class);
        if (getUser().getRole().getId().equals(TypeService.roleCashier)) {
            Integer transit = packageService.getCountCashier(TypeService.statusTransit);
            Integer process = packageService.getCountCashier(TypeService.statusProcess);
            Integer received = packageService.getCountCashier(TypeService.statusReceived);
            cardDashboardLayout.add(
                    cardComponent(new CardData("Transit", transit.toString())),
                    cardComponent(new CardData("Proses", process.toString())),
                    cardComponent(new CardData("Sampai", received.toString()))
            );
        }
        if (getUser().getRole().getId().equals(TypeService.roleDelivery)) {
            List<PackageEntity> transit = packageService.getForDeliveryTransit(getUser(), "");
            List<PackageEntity> process = packageService.getForDeliveryProcess(getUser(), "");
            cardDashboardLayout.add(
                    cardComponent(new CardData("Transit", transit.size()+"")),
                    cardComponent(new CardData("Proses", process.size()+""))
            );
        }
        if (getUser().getRole().getId().equals(TypeService.roleAccounting)) {
            List<PackageEntity> belum = packageService.getForAccountingOrHead("", null, null, null);
            List<PackageEntity> menunggu = packageService.getForAccountingOrHead("", TypeService.approveAccounting, null, null);
            List<PackageEntity> telah = packageService.getForAccountingOrHead("", TypeService.approveHead, null, null);
            cardDashboardLayout.add(
                    cardComponent(new CardData("Belum Konfirmasi", String.valueOf(belum.size()))),
                    cardComponent(
                            new CardData("Total Biaya Belum Konfirmasi",
                                    "Rp. " + belum.stream().mapToDouble(PackageEntity::getPrice).sum())
                    ),
                    cardComponent(new CardData("Menunggu Konfirmasi", String.valueOf(menunggu.size()))),
                    cardComponent(new CardData("Telah Konfirmasi", String.valueOf(telah.size())))
            );
        }
        if (getUser().getRole().getId().equals(TypeService.roleHead)) {
            List<PackageEntity> menunggu = packageService.getForAccountingOrHead("", TypeService.approveAccounting, null, null);
            List<PackageEntity> telah = packageService.getForAccountingOrHead("", TypeService.approveHead, null, null);
            cardDashboardLayout.add(
                    cardComponent(new CardData("Menunggu Konfirmasi", String.valueOf(menunggu.size()))),
                    cardComponent(
                            new CardData("Total Biaya Menunggu Konfirmasi",
                                    "Rp. " + menunggu.stream().mapToDouble(PackageEntity::getPrice).sum())
                    ),
                    cardComponent(new CardData("Telah Konfirmasi", String.valueOf(telah.size()))),
                    cardComponent(
                            new CardData("Total Biaya Telah Konfirmasi",
                                    "Rp. " + telah.stream().mapToDouble(PackageEntity::getPrice).sum())
                    )
            );
        }
    }

    private VerticalLayout cardComponent(CardData cardData) {
        HorizontalLayout titleComp = new HorizontalLayout();
        titleComp.setJustifyContentMode(JustifyContentMode.START);
        HorizontalLayout totalComp = new HorizontalLayout();
        totalComp.setJustifyContentMode(JustifyContentMode.END);
        totalComp.setWidthFull();

        Label lbTitle = new Label(cardData.getName());
        lbTitle.getStyle().set("font-weight", "bold");
        Label lbTotal = new Label(cardData.getCount());
        lbTotal.getStyle().set("font-size", "24px");

        titleComp.add(lbTitle);
        totalComp.add(lbTotal);

        VerticalLayout result = new VerticalLayout();
        result.setWidth("20%");
        result.setSpacing(false);
        result.addClassNames("card-dsb");
        result.add(titleComp, totalComp);
        return result;
    }


}
