package com.app.bimarunajaya.view.modal;

import com.app.bimarunajaya.entity.UserEntity;
import com.app.bimarunajaya.layout.ContainerLayout;
import com.app.bimarunajaya.model.MenuName;
import com.app.bimarunajaya.service.PackageService;
import com.app.bimarunajaya.service.TypeService;
import com.app.bimarunajaya.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class ConfirmPackageView extends ContainerLayout {

    private Integer packageId;

    public ConfirmPackageView(Integer packageId) {
        super();
        this.packageId = packageId;
        init();
    }

    private void init() {
        setMargin(false);
        setPadding(false);
        setSpacing(false);

        initComponent();
        initData();
    }

    private Tabs tabs;
    private Map<Tab, String> tabsToPages;
    private VerticalLayout pages;

    private Button btConfirm;
    private Button btBatal;

    private void initComponent() {
        List<MenuName> menu = Arrays.asList(
                new MenuName("Transit", "transit"),
                new MenuName("Sampai", "sampai")
        );

        tabs = new Tabs();
        tabsToPages = new HashMap<>();
        pages = new VerticalLayout();
        pages.setMargin(false);
        pages.setPadding(false);
        pages.setSpacing(false);
        pages.setSizeFull();
        pages.addClassNames("br-top");

        btBatal = new Button("Batal");
        btBatal.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btBatal.setWidthFull();

        menu.forEach(e -> {
            Tab tab = new Tab(e.getName());
            tabs.add(tab);
            tabsToPages.put(tab, e.getPath());
        });

        tabs.addSelectedChangeListener(e -> {
            String selectedPage = tabsToPages.get(tabs.getSelectedTab());
            pages.removeAll();
            pages.add(addPage(selectedPage));
        });

        String selectedPage = tabsToPages.get(tabs.getSelectedTab());
        pages.add(addPage(selectedPage));

//        btConfirm = new Button("Simpan");
//        btConfirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        btConfirm.setWidthFull();

//        HorizontalLayout hl = new HorizontalLayout();
//        hl.setMargin(false);
//        hl.setPadding(false);
//        hl.setSpacing(false);
//        hl.setWidthFull();
//        hl.add(btConfirm, btBatal);

        add(tabs, pages);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private PackageService packageService;

    private void initData() {
        userService = getBean(UserService.class);
        packageService = getBean(PackageService.class);

        List<UserEntity> station = userService.getStations("");
        station.removeIf(e -> e.getStatus() == null || !e.getStatus().getId().equals(TypeService.approveAccount));
        cbTransit.setItems(station);
    }

    private Component addPage(String menuName) {
        Component page;
        if (menuName.equalsIgnoreCase("transit"))
            page = transitView();
        else if (menuName.equalsIgnoreCase("sampai"))
            page = sampaiView();
        else page = new VerticalLayout();
        return page;
    }

    private VerticalLayout transitLy;
    private ComboBox<UserEntity> cbTransit;

    private Component transitView() {
        if (cbTransit == null) cbTransit = new ComboBox<>("Transit");
        cbTransit.setItemLabelGenerator(e -> e.getFullName().toUpperCase());
        cbTransit.setWidthFull();

        if (transitLy == null) transitLy = new VerticalLayout();
        transitLy.setMargin(false);
        transitLy.setPadding(false);
        transitLy.setSpacing(false);
        transitLy.setSizeFull();

        btConfirm = new Button("Simpan");
        btConfirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btConfirm.setWidthFull();
        btConfirm.addClickListener(e -> {
            if (cbTransit.getValue() != null) {
                packageService.transitPackage(getUser(), cbTransit.getValue(), packageId);
                eventClose.accept(true);
                Notification.show("Data tersimpan");
            } else {
                Notification.show("Lengkapi data");
            }
        });

        HorizontalLayout hl = new HorizontalLayout();
        hl.setMargin(false);
        hl.setPadding(false);
        hl.setSpacing(false);
        hl.setWidthFull();
        hl.add(btConfirm, btBatal);

        transitLy.removeAll();
        transitLy.add(cbTransit, hl);

        return transitLy;
    }

    private VerticalLayout sampaiLy;
    private TextField txPenerima;
    byte[] imageBytes = new byte[0];
    private InputStream inputStream;

    private Component sampaiView() {
        if (txPenerima == null) txPenerima = new TextField("Diterima Oleh");
        txPenerima.setWidthFull();

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpg", "image/jpeg", "image/png", "image/gif", "image/jfif");
        upload.setMaxWidth("350px");
        upload.setMaxFileSize(5000000);

        if (sampaiLy == null) sampaiLy = new VerticalLayout();
        sampaiLy.setMargin(false);
        sampaiLy.setPadding(false);
        sampaiLy.setSpacing(false);
        sampaiLy.setSizeFull();

        VerticalLayout cImg = new VerticalLayout();
        cImg.setMargin(false);
        cImg.setPadding(false);
        cImg.setSpacing(false);
        cImg.setWidthFull();

        btConfirm = new Button("Simpan");
        btConfirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btConfirm.setWidthFull();
        btConfirm.addClickListener(e -> {
            if (!txPenerima.isEmpty()) {
                packageService.receivedPackage(txPenerima.getValue(), packageId);
                eventClose.accept(true);
                Notification.show("Data tersimpan");
            } else {
                Notification.show("Lengkapi data");
            }
        });

        HorizontalLayout hl = new HorizontalLayout();
        hl.setMargin(false);
        hl.setPadding(false);
        hl.setSpacing(false);
        hl.setWidthFull();
        hl.add(btConfirm, btBatal);

        sampaiLy.removeAll();
        sampaiLy.add(txPenerima, upload, cImg, hl);

        upload.addSucceededListener(e -> {
            try {
                inputStream = buffer.getInputStream();
                imageBytes = IOUtils.toByteArray(inputStream);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            byte[] finalImageBytes = imageBytes;
            StreamResource resource = new StreamResource("foto.jpg", () -> new ByteArrayInputStream(finalImageBytes));
            Image img = new Image(resource, "foto");
            img.setWidthFull();
            img.addClassNames("no-space");

            cImg.removeAll();
            cImg.add(img);
        });

        return sampaiLy;
    }

    private Consumer eventClose;
    public void onClose(Consumer event) {
        eventClose = event;
        btBatal.addClickListener(e -> event.accept(true));
    }

}
