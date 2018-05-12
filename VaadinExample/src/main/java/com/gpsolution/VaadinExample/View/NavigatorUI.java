package com.gpsolution.VaadinExample.View;


import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.*;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@SpringUI
public class NavigatorUI extends UI {
    /**
	 * 
	 */

    @Configuration
    @EnableVaadin
    public static class MyConfiguration {
    }
    
	private static final long serialVersionUID = 8702590670822155713L;
	Navigator navigator;
    protected static final String CATEGORIES = "categories";

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Navigation Example");

        // Create a navigator to control the views
        navigator = new Navigator(this, this);

        // Create and register the views
        HotelsView hotelsView = new HotelsView(navigator);
        CategoriesView categoriesView = new CategoriesView(navigator,hotelsView);
        navigator.addView("", hotelsView);
        navigator.addView(CATEGORIES, categoriesView);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    public static class MyUIServlet extends SpringVaadinServlet{

		/**
		 * 
		 */
		private static final long serialVersionUID = -4268958196057167834L;
    }
}
