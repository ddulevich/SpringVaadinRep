package com.gpsolution.VaadinExample.View;

import com.gpsolution.VaadinExample.Entity.Category;
import com.gpsolution.VaadinExample.Entity.Hotel;
import com.gpsolution.VaadinExample.Service.CategoryService;
import com.gpsolution.VaadinExample.Service.HotelService;
import com.gpsolution.VaadinExample.View.converters.DateLongConverter;
import com.gpsolution.VaadinExample.View.converters.RatingConverter;
import com.gpsolution.VaadinExample.View.validators.AddressValidator;
import com.gpsolution.VaadinExample.View.validators.CategoryValidator;
import com.gpsolution.VaadinExample.View.validators.DateValidator;
import com.gpsolution.VaadinExample.View.validators.RatingValidator;
import com.vaadin.data.*;
import com.vaadin.ui.*;

import java.time.LocalDate;
import java.util.Date;

public class HotelEditForm extends FormLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5736155492153790860L;
	
	private HotelService hotelService = HotelService.getInstance();
	private HotelsView ui;
    private Hotel hotel;

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        binder.readBean(this.hotel);
        setVisible(true);
    }

    private Binder<Hotel> binder = new Binder<>(Hotel.class);
    private TextField name = new TextField("Name");
    private TextField address = new TextField("Address");
    private TextField rating = new TextField("Rating");
    private DateField operatesFrom = new DateField("Date");
    private static NativeSelect<Category> category = new NativeSelect<>("Category");
    private TextField url = new TextField("URL");
    private TextArea description = new TextArea("Description");

    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private HorizontalLayout buttons = new HorizontalLayout();

    private static CategoryService categoryService = CategoryService.getInstance();
    
    public HotelEditForm(HotelsView hotelUI){
        this.ui = hotelUI;
 
        setItems();
        
        buttons.addComponents(save,close);
        buttons.setWidth(100,Unit.PERCENTAGE);

        save.setWidth(100,Unit.PERCENTAGE);
        close.setWidth(100,Unit.PERCENTAGE);

        name.setDescription("The name is required");
        name.setWidth(100, Unit.PERCENTAGE);

        address.setDescription("The address must be at least 5 characters");
        address.setWidth(100, Unit.PERCENTAGE);

        rating.setDescription("Rating should be from 0 to 5");
        rating.setWidth(100, Unit.PERCENTAGE);

        operatesFrom.setDescription("The date must be in the past.");
        operatesFrom.setWidth(100, Unit.PERCENTAGE);

        category.setDescription("The category is required");
        category.setWidth(100, Unit.PERCENTAGE);

        url.setDescription("The url is required");
        url.setWidth(100, Unit.PERCENTAGE);


        description.setWidth(100, Unit.PERCENTAGE);

        addComponents(name,address,rating,operatesFrom,category,description,url,buttons);

        binder.forField(name).asRequired("The name is required")
                .bind(Hotel::getName,Hotel::setName);

        binder.forField(address).asRequired("The address is required")
                .withValidator(new AddressValidator())
                .bind(Hotel::getAddress,Hotel::setAddress);

        binder.forField(rating).asRequired("The rating is required")
                .withConverter(new RatingConverter())
                .withValidator(new RatingValidator())
                .bind(Hotel::getRating,Hotel::setRating);

        binder.forField(operatesFrom).asRequired("The date is required")
                .withConverter(new DateLongConverter())
                .withValidator(new DateValidator())
                .bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);

        binder.forField(category).asRequired("The category is required")
        		.withValidator(new CategoryValidator())
                .bind(Hotel::getCategory,Hotel::setCategory);

        binder.forField(url).asRequired("The url is required")
                .bind(Hotel::getUrl,Hotel::setUrl);

        binder.forField(description)
                .bind(Hotel::getDescription,Hotel::setDescription);

        setItems();
        ui.updateList();
        
        save.addClickListener(e -> save());
        close.addClickListener(e -> exit());
    }
    
    public static void setItems() {
    	category.setItems(categoryService.findAll());
    }
    
    private void save(){
        if (binder.isValid()){
            try {
                binder.writeBean(hotel);
            } catch (ValidationException e) {
                Notification.show("Unable to save!" + e.getMessage(), Notification.Type.HUMANIZED_MESSAGE);
            }
            hotelService.save(hotel);
            exit();
        } else {
            Notification.show("Unable to save! Please review errors and fix them.", Notification.Type.ERROR_MESSAGE);
       }
    }

    private void exit(){
        binder.removeBean();
        ui.updateList();
        setVisible(false);
        ui.deleteHotel.setEnabled(false);
    }
}
