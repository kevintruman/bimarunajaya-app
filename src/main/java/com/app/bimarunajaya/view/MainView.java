package com.app.bimarunajaya.view;

import com.app.bimarunajaya.entity.UserEntity;
import com.app.bimarunajaya.model.MenuName;
import com.app.bimarunajaya.service.TypeService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.*;

@Route("")
@PageTitle("Bimaruna Jaya")
@StyleSheet("/main.css")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    private UserEntity session;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        session = (UserEntity) UI.getCurrent().getSession().getAttribute("user");
        if (session == null) {
            UI.getCurrent().navigate("login");
            event.rerouteTo("login");
            Notification.show("Anda belum Login");
        } else {
            init();
        }
    }

    private VerticalLayout mainLayout;

    private void init() {
        setMargin(false);
        setPadding(false);
        setSpacing(false);
        setSizeFull();
        addClassNames("bg-main2view");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        mainLayout = new VerticalLayout();
        mainLayout.setMargin(false);
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setHeight("95%");
        mainLayout.setWidth("90%");
        mainLayout.addClassNames("main2view", "selected");
        mainLayout.setAlignItems(Alignment.CENTER);

        add(mainLayout);
        addHeader();
        addTabs();
    }

    private HorizontalLayout headerLayout;

    private void addHeader() {
        headerLayout = new HorizontalLayout();

        Image logo = new Image("/images/bima-full.jpg", "logo");
        logo.setHeight("50px");
        logo.addClassNames("no-space", "lf-mrgn");

        VerticalLayout expanded = new VerticalLayout();
        expanded.setWidthFull();

        Image pp = new Image("/images/user.png", "fp");
        pp.setHeight("30px");
        pp.setWidth("30px");
        pp.addClassNames("no-space");

        Label username = new Label(session.getFullName());
//        username.setMinWidth("120px");
        username.addClassNames("lf-mrgn", "rg-mrgn");

        Button btLogout = new Button("Logout", new Icon(VaadinIcon.SIGN_OUT));
        btLogout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btLogout.addClassNames("rg-mrgn");
        btLogout.addClickListener(e -> {
            UI.getCurrent().getSession().setAttribute("user", null);
            UI.getCurrent().navigate("login");
        });

        HorizontalLayout rg = new HorizontalLayout();
        rg.setMargin(false);
        rg.setPadding(false);
        rg.setSpacing(false);
        rg.setWidthFull();
        rg.setAlignItems(Alignment.CENTER);
        rg.setJustifyContentMode(JustifyContentMode.END);
        rg.add(pp, username, btLogout);

        headerLayout.add(logo, rg);
        headerLayout.setAlignItems(Alignment.CENTER);
        headerLayout.setMargin(false);
        headerLayout.setPadding(false);
        headerLayout.setSpacing(false);
        headerLayout.setHeight("70px");
        headerLayout.setWidthFull();
        headerLayout.addClassNames("br-btm");

        mainLayout.add(headerLayout);
    }

    private Tabs tabs;
    private Map<Tab, String> tabsToPages;
    private VerticalLayout pages;

    private void addTabs() {
        List<MenuName> menu = new ArrayList<>();
        menu.add(new MenuName("Dashboard", "dashboard"));

        if (session.getRole().getId().equals(TypeService.roleCashier)) {
            menu.add(new MenuName("Logistik", "logistic-cashier"));
        }
        if (session.getRole().getId().equals(TypeService.roleDelivery)) {
            menu.add(new MenuName("Logistik", "logistic-delivery"));
        }
        if (session.getRole().getId().equals(TypeService.roleAccounting)) {
            menu.add(new MenuName("Logistik", "logistic-accounting"));
        }
        if (session.getRole().getId().equals(TypeService.roleHead)) {
            menu.add(new MenuName("Logistik", "logistic-head"));
        }
        if (session.getRole().getId().equals(TypeService.roleAdmin)) {
            menu.clear();
            menu.add(new MenuName("Logistik", "logistic-admin"));
            menu.add(new MenuName("User", "user-account"));
            menu.add(new MenuName("Lokasi", "station"));
        }

        tabs = new Tabs();
        tabsToPages = new HashMap<>();
        pages = new VerticalLayout();
        pages.setMargin(false);
        pages.setPadding(false);
        pages.setSpacing(false);
        pages.setSizeFull();
        pages.addClassNames("br-top");

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

        mainLayout.add(tabs, pages);
    }

    private Component addPage(String menuName) {
//        Optional<Class<? extends Component>> s = UI.getCurrent().getRouter().getRegistry().getNavigationTarget("");
        Component page;
        if (menuName.equalsIgnoreCase("dashboard"))
            page = new DashboardView();
        else if (menuName.equalsIgnoreCase("logistic-cashier"))
            page = new PackageDataCashierView();
        else if (menuName.equalsIgnoreCase("logistic-delivery"))
            page = new PackageDataDeliveryView();
        else if (menuName.equalsIgnoreCase("logistic-accounting"))
            page = new PackageDataAccountingView();
        else if (menuName.equalsIgnoreCase("logistic-head"))
            page = new PackageDataHeadView();
        else if (menuName.equalsIgnoreCase("user-account"))
            page = new AccountView();
        else if (menuName.equalsIgnoreCase("logistic-admin"))
            page = new PackageDataAdminView();
        else if (menuName.equalsIgnoreCase("station"))
            page = new StationView();
        else page = new VerticalLayout();
        return page;
    }

}
