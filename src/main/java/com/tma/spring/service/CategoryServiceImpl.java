package com.tma.spring.service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.NotificationBroadcasterSupport;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.tma.spring.entity.Category;
import com.tma.spring.jms.JmsProducer;
import com.tma.spring.mbean.CategoryServiceImplMBean;
import com.tma.spring.util.Util;

public class CategoryServiceImpl extends NotificationBroadcasterSupport implements CategoryServiceImplMBean {

	public final static Logger logger = Logger.getLogger(StudentServiceImpl.class);

	private SessionFactory sessionFactory;
	JmsProducer jmsProducer;

	public CategoryServiceImpl(SessionFactory sessionFactory, JmsProducer jmsProducer)
			throws URISyntaxException, Exception {
		this.sessionFactory = sessionFactory;
		this.jmsProducer = jmsProducer;
	}

	public void createRecord(Category category) {
		Session sessionObj = sessionFactory.getCurrentSession();
		sessionObj.save(category);
		jmsProducer.sendMessage("Inserted " + category.toString() + " at " + Util.convertDatetimeToString(new Date()));

	}

	@SuppressWarnings("unchecked")
	public List<Category> displayRecords() {
		List<Category> categoriesList = new ArrayList<Category>();
		Session sessionObj = sessionFactory.getCurrentSession();
		categoriesList = sessionObj.createQuery("FROM categories").getResultList();
		return categoriesList;
	}

	public void updateRecord(Category category) {
		Session sessionObj = sessionFactory.getCurrentSession();
		category = (Category) sessionObj.get(Category.class, category.getId());
		category.setName(category.getName());
		logger.info("\nCategory With Id?= " + category.getId() + " Is Successfully Updated In The Database!\n");
		jmsProducer.sendMessage("Updated " + category.toString() + " at " + Util.convertDatetimeToString(new Date()));
	}

	public void deleteRecord(Integer id) {
		Session sessionObj = sessionFactory.getCurrentSession();
		Category category = findRecordById(id);
		sessionObj.delete(category);
		logger.info("\nCategory With Id?= " + id + " Is Successfully Deleted From The Database!\n");
		jmsProducer.sendMessage("Deleted " + category.toString() + " at " + Util.convertDatetimeToString(new Date()));
	}

	public Category findRecordById(Integer id) {
		Category category = null;
		Session sessionObj = sessionFactory.getCurrentSession();
		category = (Category) sessionObj.get(Category.class, id);
		return category;
	}

}
