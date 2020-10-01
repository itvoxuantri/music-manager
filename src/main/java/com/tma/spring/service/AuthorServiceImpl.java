package com.tma.spring.service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.NotificationBroadcasterSupport;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.tma.spring.entity.Author;
import com.tma.spring.jms.JmsProducer;
import com.tma.spring.mbean.AuthorServiceImplMBean;
import com.tma.spring.util.Util;

public class AuthorServiceImpl extends NotificationBroadcasterSupport implements AuthorServiceImplMBean {
	public final static Logger logger = Logger.getLogger(StudentServiceImpl.class);

	private SessionFactory sessionFactory;
	JmsProducer jmsProducer;

	public AuthorServiceImpl(SessionFactory sessionFactory, JmsProducer jmsProducer)
			throws URISyntaxException, Exception {
		this.sessionFactory = sessionFactory;
		this.jmsProducer = jmsProducer;
	}

	public void createRecord(Author author) {
		Session sessionObj = sessionFactory.getCurrentSession();
		sessionObj.save(author);
		jmsProducer.sendMessage("Inserted " + author.toString() + " at " + Util.convertDatetimeToString(new Date()));
	}

	@SuppressWarnings("unchecked")
	public List<Author> displayRecords() {
		List<Author> authorsList = new ArrayList<Author>();
		Session sessionObj = sessionFactory.getCurrentSession();
		authorsList = sessionObj.createQuery("FROM authors").getResultList();
		return authorsList;
	}

	public void updateRecord(Author author) {
		Session sessionObj = sessionFactory.getCurrentSession();
		author = (Author) sessionObj.get(Author.class, author.getId());
		author.setName(author.getName());
		logger.info("\nAuthor With Id?= " + author.getId() + " Is Successfully Updated In The Database!\n");
		jmsProducer.sendMessage("Updated " + author.toString() + " at " + Util.convertDatetimeToString(new Date()));
	}

	public void deleteRecord(Integer id) {
		Session sessionObj = sessionFactory.getCurrentSession();
		Author author = findRecordById(id);
		sessionObj.delete(author);
		logger.info("\nAuthor With Id?= " + id + " Is Successfully Deleted From The Database!\n");
		jmsProducer.sendMessage("Deleted " + author.toString() + " at " + Util.convertDatetimeToString(new Date()));
	}

	public Author findRecordById(Integer id) {
		Author author = null;
		Session sessionObj = sessionFactory.getCurrentSession();
		author = (Author) sessionObj.get(Author.class, id);
		System.out.println(author.getId());
		return author;
	}

}
