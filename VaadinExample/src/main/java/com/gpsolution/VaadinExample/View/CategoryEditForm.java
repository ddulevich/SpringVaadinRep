package com.gpsolution.VaadinExample.View;

import com.gpsolution.VaadinExample.Entity.Category;
import com.gpsolution.VaadinExample.Service.CategoryService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

public class CategoryEditForm extends FormLayout{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CategoriesView ui;
    private CategoryService service = CategoryService.getInstance();
    private Category category;

    public void setHotelCategory(Category category) {
        this.category = category;
        binder.readBean(this.category);
        setVisible(true);
    }

    private Binder<Category> binder = new Binder<>(Category.class);

    private TextField type = new TextField("Category");

    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private HorizontalLayout buttons = new HorizontalLayout();

    public CategoryEditForm(CategoriesView ui) {

        this.ui = ui;

        buttons.addComponents(save,close);
        buttons.setWidth(100,Unit.PERCENTAGE);

        save.setWidth(100,Unit.PERCENTAGE);
        close.setWidth(100,Unit.PERCENTAGE);

        type.setWidth(100, Unit.PERCENTAGE);

        addComponents(type,buttons);

        binder.forField(type).asRequired("The category is required")
                .bind(Category::getType,Category::setType);

        save.addClickListener(e -> save());
        close.addClickListener(e -> exit());
    }
    private void save(){
        if (binder.isValid()){
            try {
                binder.writeBean(category);
            } catch (ValidationException e) {
                Notification.show("Unable to save!" + e.getMessage(), Notification.Type.HUMANIZED_MESSAGE);
            }
            service.save(category);
            exit();
        } else {
            Notification.show("Unable to save! Please review errors and fix them.", Notification.Type.ERROR_MESSAGE);
        }
    }
    private void exit(){
        ui.updateList();
        setVisible(false);
        ui.deleteCategory.setEnabled(false);
        ui.editCategory.setEnabled(false);
    }
}
