package com.gpsolution.VaadinExample.View;

import com.gpsolution.VaadinExample.Service.HotelService;
import com.gpsolution.VaadinExample.Entity.Hotel;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;
import java.util.Set;

import static com.gpsolution.VaadinExample.View.NavigatorUI.CATEGORIES;


public class HotelsView extends VerticalLayout implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2964594007630703611L;

	final HotelService hotelService = HotelService.getInstance();

    final VerticalLayout layout = new VerticalLayout();
    final HorizontalLayout controls = new HorizontalLayout();
    final HorizontalLayout content = new HorizontalLayout();
    final Grid<Hotel> hotelGrid = new Grid<>();
    final HotelEditForm form = new HotelEditForm(this);
    final TextField filterName = new TextField();
    final TextField filterAddress = new TextField();
    final Button addHotel = new Button("Add hotel");
    final Button editHotel = new Button("Edit hotel");
    final Button deleteHotel = new Button("Delete hotel");
    final Button bulkUpdate = new Button("Bulk update");
    final MenuBar menu = new MenuBar();
    final BulkUpdate bulkUpdateView = new BulkUpdate(this);
    final PopupView popupView = bulkUpdateView.getPopupView();;
    Navigator navigator;
    
    final MultiSelectionModel<Hotel> selection;

    public HotelsView(Navigator navigator) {
    		
        setSizeFull();
        this.navigator = navigator;

        addComponents(layout);
        controls.addComponents(filterName, filterAddress, addHotel, editHotel, bulkUpdate, deleteHotel);
        content.addComponents(hotelGrid, form);
        
        content.setWidth("100%");
        content.setHeight(500,Unit.PIXELS);
        content.setExpandRatio(hotelGrid,0.7f);

        hotelGrid.setWidth(100, Unit.PERCENTAGE);
        hotelGrid.setHeight("100%");
        
        content.setWidth("100%");
        content.setHeight(500,Unit.PIXELS);
        content.setExpandRatio(hotelGrid,0.7f);
        content.setExpandRatio(form,0.3f);
        content.setComponentAlignment(form,Alignment.MIDDLE_CENTER);

        form.setWidth("80%");
        form.setHeight("100%");
        form.setVisible(false);
         
        layout.addComponents(menu, controls,content);
        layout.addComponent(popupView);

        MenuBar.Command hotelsPage = new MenuBar.Command() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -7824753418816046217L;

			@Override
            public void menuSelected(MenuBar.MenuItem menuItem) {

                navigator.navigateTo("");
            }
        };

        MenuBar.Command categoriesPage = new MenuBar.Command() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -4114756094876026273L;

			@Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                navigator.navigateTo(CATEGORIES);
            }
        };

        menu.addItem("Hotel", VaadinIcons.BUILDING, hotelsPage);
        menu.addItem("Category", VaadinIcons.ACADEMY_CAP, categoriesPage);
        menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);

        //data
        hotelGrid.addColumn(Hotel::getName).setCaption("Name");
        hotelGrid.addColumn(Hotel::getAddress).setCaption("Address");
        hotelGrid.addColumn(Hotel::getRating).setCaption("Rating");
        hotelGrid.addColumn(Hotel::getCategoryView).setCaption("Category");
        hotelGrid.addColumn(Hotel::getDate).setCaption("Operates From");
        hotelGrid.addColumn(Hotel::isPersisted).setCaption("Persisted");
        hotelGrid.addColumn(Hotel::getId).setCaption("ID");
        hotelGrid.addColumn(Hotel::getDescription).setCaption("Description");
        Grid.Column<Hotel, String> htmlColumn = hotelGrid.addColumn(hotel ->
                        "<a href='" + hotel.getUrl() + "' target='_blank'>" + hotel.getUrl() + "</a>",
                new HtmlRenderer());
        htmlColumn.setCaption("Url");

        filterName.setPlaceholder("filter by name");
        filterName.addValueChangeListener(e -> updateListByFilter());
        filterName.setValueChangeMode(ValueChangeMode.LAZY);

        filterAddress.setPlaceholder("filter by address");
        filterAddress.addValueChangeListener(e -> updateListByFilter());
        filterAddress.setValueChangeMode(ValueChangeMode.LAZY);

        deleteHotel.setEnabled(false);
        editHotel.setEnabled(false);
        bulkUpdate.setEnabled(false);

        hotelGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        selection = (MultiSelectionModel<Hotel>) hotelGrid.getSelectionModel();

        hotelGrid.asMultiSelect().addValueChangeListener(e -> {
            if (selection.getSelectedItems().size() == 0){
                deleteHotel.setEnabled(false);
                editHotel.setEnabled(false);
                bulkUpdate.setEnabled(false);
            }
            if (selection.getSelectedItems().size() == 1){
                deleteHotel.setEnabled(true);
                editHotel.setEnabled(true);
                bulkUpdate.setEnabled(false);
            }
            if (selection.getSelectedItems().size() > 1){
                deleteHotel.setEnabled(true);
                bulkUpdate.setEnabled(true);
                editHotel.setEnabled(false);
            }
            form.setVisible(false);
        });

        addHotel.addClickListener(e -> setHotel());
        
        deleteHotel.addClickListener(e -> {
            Set<Hotel> delete = selection.getSelectedItems();
            hotelService.delete(delete);
            deleteHotel.setEnabled(false);
            editHotel.setEnabled(false);
            form.setVisible(false);
            updateList();
        });

        editHotel.addClickListener(e -> {
            Hotel editHotel = hotelGrid.getSelectedItems().iterator().next();
            form.setHotel(editHotel);
        });
        
        bulkUpdate.addClickListener(e -> {
        	bulkUpdateView.setBean();
        	popupView.setPopupVisible(true);
        	popupView.setHideOnMouseOut(false);
        });
        updateList();
    }

    public void setHotel(){
        Hotel hotel = new Hotel();
        form.setHotel(hotel);
    }

    public void updateList(){
        List<Hotel> hotelList = hotelService.findAll();
        hotelGrid.setItems(hotelList);
    }

    public void updateListByFilter(){
        List<Hotel> hotelList = hotelService.findByFilter(filterName.getValue(), filterAddress.getValue());
        hotelGrid.setItems(hotelList);
        deleteHotel.setEnabled(false);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}


