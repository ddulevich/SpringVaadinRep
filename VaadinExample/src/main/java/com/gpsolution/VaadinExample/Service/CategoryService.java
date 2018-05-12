package com.gpsolution.VaadinExample.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import com.gpsolution.VaadinExample.Entity.Category;
import com.gpsolution.VaadinExample.Entity.Hotel;
import com.gpsolution.VaadinExample.View.HotelEditForm;

public class CategoryService {
	
    private static CategoryService instance;
    private final HotelService hotelService = HotelService.getInstance();
    
    private CategoryService(){ }

    public static CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryService();
        }
        return instance;
    }

    public synchronized List<Category> findAll() {
        ArrayList<Category> arrayList = new ArrayList<>();
        	
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		
		CriteriaQuery<Category> criteria = builder.createQuery(Category.class);
		Root<Category> root = criteria.from(Category.class);
		criteria.select(root);
		TypedQuery<Category> query = em.createQuery(criteria);
		arrayList = (ArrayList<Category>) query.getResultList();
        
        return arrayList;
    }

    public synchronized List<Category> findByFilter(String filter) {
        ArrayList<Category> categories = (ArrayList<Category>) findAll();

		ArrayList<Category> arrayList = new ArrayList<>();
		
		for (Category category : categories) {
			boolean passesFilter = (filter == null || filter.isEmpty())
					|| category.getType().toLowerCase().contains(filter.toLowerCase());
			if (passesFilter) {
				arrayList.add(category);
			}
		}
        return arrayList;
    }

    public synchronized void save(Category category) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		if(category.getId() != null) {
			Category test = em.find(Category.class, category.getId());
			if(test != null) {
				em.merge(category);
			} else {
				em.persist(category);
			}
		} else {
			em.persist(category);
		}
		em.getTransaction().commit();
		em.close();
		HotelEditForm.setItems();
    }

    public synchronized void delete(Set<Category> hotelCategories) {
    	hotelService.changeDeletedCategory(hotelCategories);
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		for (Category category : hotelCategories) {
			em.remove(em.contains(category) ? category : em.merge(category));;
		}
		em.getTransaction().commit();
		em.close();
		HotelEditForm.setItems();
    }
}
