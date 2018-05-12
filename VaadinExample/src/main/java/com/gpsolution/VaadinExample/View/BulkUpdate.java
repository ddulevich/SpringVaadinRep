package com.gpsolution.VaadinExample.View;

import java.util.Arrays;
import java.util.Set;

import com.gpsolution.VaadinExample.Entity.Category;
import com.gpsolution.VaadinExample.Entity.Hotel;
import com.gpsolution.VaadinExample.Service.CategoryService;
import com.gpsolution.VaadinExample.Service.HotelService;
import com.gpsolution.VaadinExample.View.converters.DateLongConverter;
import com.gpsolution.VaadinExample.View.converters.RatingConverter;
import com.gpsolution.VaadinExample.View.validators.DateValidator;
import com.gpsolution.VaadinExample.View.validators.RatingValidator;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.*;

public class BulkUpdate extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final VerticalLayout popupContent = new VerticalLayout();
	private final HorizontalLayout buttons = new HorizontalLayout();
	
	private final CategoryService categoryService = CategoryService.getInstance();
	private final HotelService hotelService = HotelService.getInstance();

	private ComboBox<String> selectFields = new ComboBox<>();
	private TextField value = new TextField();
	
	private final Button update = new Button("Update");
	private final Button cancel = new Button("Cancel");
	private final PopupView popupview = new PopupView("", popupContent);

	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating = new TextField("Rating");
	private DateField operatesFrom = new DateField("Date");
	private ComboBox<Category> category = new ComboBox<>("Category");
	private TextField url = new TextField("URL");
	private TextArea description = new TextArea("Description");
	
	private HotelsView hotelsView;
	
	@SuppressWarnings("rawtypes")
	private Binder<PopUpBean> binder = new Binder<>(PopUpBean.class);
	
	@SuppressWarnings("rawtypes")
	private PopUpBean bean; 
	
	{
		selectFields.setItems(Arrays.asList("Name","Address","Category","OperatesFrom","Rating","Url","Description"));
		category.setItems(categoryService.findAll());
	}
	
	public void setBean() {
		selectFields.setValue("");
		bean = new PopUpBean<>();
		binder = new Binder<>(PopUpBean.class);
		binder.readBean(bean);
	}
	
	public PopupView getPopupView() {
		return popupview;
	}
	
	public BulkUpdate(HotelsView hotelsView) {

		this.hotelsView = hotelsView;
		
		binder.forField(selectFields)
			.bind(PopUpBean<String>::getSelectFields, PopUpBean<String>::setSelectFields);
				
		selectFields.setPlaceholder("Please select field");
		value.setPlaceholder("Field value");
		
		buttons.addComponents(update, cancel);
		popupContent.addComponents(selectFields,value,buttons);
		addComponent(popupContent);

		selectFields.addSelectionListener(e -> {
			
			switch (selectFields.getValue()) {
			
			case "Category":
				changeField(category);
				popupContent.addComponent(category, 1);
				binder.forField(category).asRequired().bind(PopUpBean<Category>::getValue, PopUpBean<Category>::setValue);
				break;
				
			case "OperatesFrom":
				changeField(operatesFrom);
				popupContent.addComponent(operatesFrom, 1);
				binder.forField(operatesFrom).asRequired()
						.withConverter(new DateLongConverter())
						.withValidator(new DateValidator())
						.bind(PopUpBean<Long>::getValue,PopUpBean<Long>::setValue);
				break;
				
			case "Name":
				changeField(name);
				popupContent.addComponent(name, 1);
				binder.forField(name).asRequired().bind(PopUpBean<String>::getValue,PopUpBean<String>::setValue);
				break;
				
			case "Address":
				changeField(address);
				popupContent.addComponent(address, 1);
				binder.forField(address).asRequired().bind(PopUpBean<String>::getValue,PopUpBean<String>::setValue);
				break;
				
			case "Rating":	
				changeField(rating);
				popupContent.addComponent(rating, 1);
				binder.forField(rating).asRequired()
						.withConverter(new RatingConverter())
						.withValidator(new RatingValidator())
						.bind(PopUpBean<Integer>::getValue,PopUpBean<Integer>::setValue);
				break;
				
			case "Url":
				changeField(url);
				popupContent.addComponent(url, 1);
				binder.forField(url).asRequired().bind(PopUpBean<String>::getValue,PopUpBean<String>::setValue);
				break;
				
			case "Description":
				changeField(description);
				popupContent.addComponent(description, 1);
				binder.forField(description).asRequired().bind(PopUpBean<String>::getValue,PopUpBean<String>::setValue);
				break;
				
			default:
				popupContent.removeComponent(popupContent.getComponent(1));
				popupContent.addComponent(value, 1);
			}
		});

		cancel.addClickListener(e -> exit());

		update.addClickListener(e -> update());
	}
	
	@SuppressWarnings("rawtypes")
	private void changeField(AbstractComponent component) {
		bean.setSelectFields(selectFields.getValue());
		popupContent.removeComponent(popupContent.getComponent(1));
		if (component instanceof ComboBox) {
			ComboBox<?> combobox = (ComboBox) component;
			combobox.setValue(null);
		} else if (component instanceof DateField) {
			DateField dateField = (DateField) component;
			dateField.setValue(null);
		} else if (component instanceof TextArea) {
			TextArea textArea = (TextArea) component;
			textArea.setValue("");
		} else {
			TextField textField = (TextField) component;
			textField.setValue("");
		}
		category.setValue(null);;
	}
	
	private void update() {
		if (binder.isValid()) {
			Set<Hotel> hotels = hotelsView.selection.getSelectedItems();
			try {
				binder.writeBean(bean);
			} catch (ValidationException e1) {
				e1.printStackTrace();
				Notification.show("Validation Exeption.",
						Notification.Type.ERROR_MESSAGE);
			}
			hotelService.bulkUpdate(hotels, bean);
			exit();
		} else {
			setBean();
			Notification.show("Unable to save! Please review errors and fix them.",
					Notification.Type.ERROR_MESSAGE);
		}
	}

	private void exit() {
		binder.removeBean();
		hotelsView.updateList();
		hotelsView.popupView.setPopupVisible(false);
		hotelsView.deleteHotel.setEnabled(false);
	}
}
