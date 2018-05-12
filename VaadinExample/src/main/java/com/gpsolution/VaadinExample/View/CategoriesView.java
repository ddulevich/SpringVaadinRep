package com.gpsolution.VaadinExample.View;

import java.util.List;
import java.util.Set;

import com.gpsolution.VaadinExample.Entity.Category;
import com.gpsolution.VaadinExample.Service.CategoryService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.themes.ValoTheme;

import static com.gpsolution.VaadinExample.View.NavigatorUI.CATEGORIES;

public class CategoriesView extends VerticalLayout implements View{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final CategoryService categoryService = CategoryService.getInstance();

    final VerticalLayout layout = new VerticalLayout();
    final HorizontalLayout controls = new HorizontalLayout();
    final HorizontalLayout content = new HorizontalLayout();
    final Grid<Category> hotelCategoryGrid = new Grid<>();
    final CategoryEditForm form = new CategoryEditForm(this);
    final TextField filter = new TextField();
    final Button addCategory = new Button("Add category");
    final Button editCategory = new Button("Edit category");
    final Button deleteCategory = new Button("Delete category");

    final MenuBar menu = new MenuBar();

    Navigator navigator;
    HotelsView hotelsView;

    public CategoriesView(Navigator navigator, HotelsView hotelsView) {
        setSizeFull();
        this.navigator = navigator;
        this.hotelsView = hotelsView;

        hotelCategoryGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        MultiSelectionModel<Category> selection = (MultiSelectionModel) hotelCategoryGrid.getSelectionModel();

        MenuBar.Command hotelsPage = new MenuBar.Command() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                hotelsView.updateList();
                navigator.navigateTo("");
            }
        };

        MenuBar.Command categoriesPage = new MenuBar.Command() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                navigator.navigateTo(CATEGORIES);
            }
        };

        menu.addItem("Hotel", VaadinIcons.BUILDING, hotelsPage);
        menu.addItem("Category", VaadinIcons.ACADEMY_CAP, categoriesPage);
        menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);

        controls.addComponents(filter, addCategory, editCategory,deleteCategory);
        content.addComponents(hotelCategoryGrid,form);

        content.setWidth("100%");
        content.setHeight(500,Unit.PIXELS);
        content.setExpandRatio(hotelCategoryGrid,0.5f);
        content.setExpandRatio(form,0.5f);
        content.setComponentAlignment(form,Alignment.MIDDLE_LEFT);

        form.setWidth("50%");
        form.setHeight("100%");
        form.setVisible(false);

        hotelCategoryGrid.setWidth(100, Unit.PERCENTAGE);
        hotelCategoryGrid.setHeight("50%");

        layout.addComponents(menu,controls,content);

        addComponents(layout);

        //data
        hotelCategoryGrid.addColumn(Category::getType).setId("type");
        hotelCategoryGrid.getColumn("type").setCaption("CATEGORY");

        deleteCategory.setEnabled(false);
        editCategory.setEnabled(false);

        filter.setPlaceholder("filter");
        filter.addValueChangeListener(e -> updateListByFilter());
        filter.setValueChangeMode(ValueChangeMode.LAZY);

        addCategory.addClickListener(e -> setHotelCategory());

        editCategory.addClickListener(e -> {
            Category editCandidate = hotelCategoryGrid.getSelectedItems().iterator().next();
            form.setHotelCategory(editCandidate);
        });

        deleteCategory.addClickListener(e -> {
            Set<Category> delete = selection.getSelectedItems();
            categoryService.delete(delete);
            deleteCategory.setEnabled(false);
            editCategory.setEnabled(false);
            form.setVisible(false);
            updateList();
        });

        hotelCategoryGrid.asMultiSelect().addValueChangeListener(e -> {
            if (selection.getSelectedItems().size() == 0){
                deleteCategory.setEnabled(false);
                editCategory.setEnabled(false);
            }
            if (selection.getSelectedItems().size() == 1){
                deleteCategory.setEnabled(true);
                editCategory.setEnabled(true);
            }
            if (selection.getSelectedItems().size() > 1){
                deleteCategory.setEnabled(true);
                editCategory.setEnabled(false);
            }
           form.setVisible(false);
        });

        updateList();
    }

    public void setHotelCategory(){
        Category category = new Category();
        form.setHotelCategory(category);
    }

    public void updateList(){
        List<Category> categoryList = categoryService.findAll();
        hotelCategoryGrid.setItems(categoryList);
    }

    public void updateListByFilter(){
        List<Category> hotelCategoriesList = categoryService.findByFilter(filter.getValue());
        hotelCategoryGrid.setItems(hotelCategoriesList);
        deleteCategory.setEnabled(false);
        editCategory.setEnabled(false);
    }

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
    
}
