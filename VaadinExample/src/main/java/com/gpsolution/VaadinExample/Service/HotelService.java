package com.gpsolution.VaadinExample.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.gpsolution.VaadinExample.Entity.Category;
import com.gpsolution.VaadinExample.Entity.Hotel;
import com.gpsolution.VaadinExample.View.BulkUpdate;
import com.gpsolution.VaadinExample.View.PopUpBean;


public class HotelService {

	private static HotelService instanceHotel;

	private HotelService() {
	}

	public static HotelService getInstance() {
		if (instanceHotel == null) {
			instanceHotel = new HotelService();
		}
		return instanceHotel;
	}

	public synchronized List<Hotel> findAll() {

		ArrayList<Hotel> arrayList = new ArrayList<>();		
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		
		CriteriaQuery<Hotel> criteria = builder.createQuery(Hotel.class);
		Root<Hotel> root = criteria.from(Hotel.class);
		criteria.select(root);
		TypedQuery<Hotel> query = em.createQuery(criteria);
		arrayList = (ArrayList<Hotel>) query.getResultList();

		return arrayList;

	}

	public synchronized List<Hotel> findByFilter(String name, String adress) {
		ArrayList<Hotel> hotels = (ArrayList<Hotel>) findAll();
		
		ArrayList<Hotel> arrayList = new ArrayList<>();
		for (Hotel hotel : hotels) {
			boolean passesFilterByName = (name == null || name.isEmpty())
					|| hotel.getName().toLowerCase().contains(name.toLowerCase());
			boolean passesFilterByAddress = (adress == null || adress.isEmpty())
					|| hotel.getAddress().toLowerCase().contains(adress.toLowerCase());
			if (passesFilterByName && passesFilterByAddress) {
				arrayList.add(hotel);
			}
			
		}
		return arrayList;	
	}
	
	public synchronized void delete(Set<Hotel> deleteHotels) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		for (Hotel hotel2 : deleteHotels) {
			em.remove(em.contains(hotel2) ? hotel2 : em.merge(hotel2));;
		}
		em.getTransaction().commit();
		em.close();
	}

	public synchronized void save(Hotel hotel) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		if(hotel.getId() != null) {
			Hotel test = em.find(Hotel.class, hotel.getId());
			if(test != null) {
				em.merge(hotel);
			} else {
				em.persist(hotel);
			}
		} else {
			em.persist(hotel);
		}
		em.getTransaction().commit();
		em.close();
	}

	public synchronized void changeDeletedCategory(Set<Category> hotelCategories) {
		ArrayList<Hotel> hotels = (ArrayList<Hotel>) findAll();
		for (Category category : hotelCategories) {
			for (Hotel hotel : hotels) {
				if (category.toString().equals(hotel.getCategory().toString())) {
					hotel.setCategory(null);
					save(hotel);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void bulkUpdate(Set<Hotel> hotels, PopUpBean bean) {
		String field = bean.getSelectFields();
		if (bean.getValue() instanceof String) {
			String value = (String) bean.getValue();
			Class[] paramTypes = new Class[] { String.class };
			SaveHotel<String> saveHotel = new SaveHotel<>();
			saveHotel.saveForParametr(hotels, field, value, paramTypes);
		} else if(bean.getValue() instanceof Integer) { 
			Integer value = (Integer) bean.getValue();
			Class[] paramTypes = new Class[] { Integer.class };
			SaveHotel<Integer> saveHotel = new SaveHotel<>();
			saveHotel.saveForParametr(hotels, field, value, paramTypes);
		}else if (bean.getValue() instanceof Category) {
			Category value = (Category) bean.getValue();
			Class[] paramTypes = new Class[] { Category.class };
			SaveHotel<Category> saveHotel = new SaveHotel<>();
			saveHotel.saveForParametr(hotels, field, value, paramTypes);
		} else if (bean.getValue() instanceof Long) {
			Long value = (Long) bean.getValue();
			Class[] paramTypes = new Class[] { Long.class };
			SaveHotel<Long> saveHotel = new SaveHotel<>();
			saveHotel.saveForParametr(hotels, field, value,paramTypes);
		}
	}

	@SuppressWarnings("rawtypes")
	public class SaveHotel<T>{
		private void saveForParametr(Set<Hotel> hotels, String field, T value, Class[] paramTypes) {
			try {
				Class<Hotel> hotelclass = Hotel.class;
				Method method = hotelclass.getDeclaredMethod("set" + field, paramTypes);
				hotels.forEach(h -> {
					try {
						method.invoke(h, value);
						save(h);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				});
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}
}
