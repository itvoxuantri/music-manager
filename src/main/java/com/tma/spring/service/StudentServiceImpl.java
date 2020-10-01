package com.tma.spring.service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.NotificationBroadcasterSupport;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.tma.spring.entity.Student;
import com.tma.spring.jms.JmsProducer;
import com.tma.spring.mbean.StudentServiceImplMBean;
import com.tma.spring.util.Util;

public class StudentServiceImpl extends NotificationBroadcasterSupport implements StudentServiceImplMBean {

	public final static Logger logger = Logger.getLogger(StudentServiceImpl.class);

	private SessionFactory sessionFactory;
	JmsProducer jmsProducer;

	public StudentServiceImpl(SessionFactory sessionFactory, JmsProducer jmsProducer)
			throws URISyntaxException, Exception {
		this.sessionFactory = sessionFactory;
		this.jmsProducer = jmsProducer;
	}

	public void createRecord(Student student) {
		Session sessionObj = sessionFactory.getCurrentSession();
		sessionObj.save(student);
		jmsProducer.sendMessage("Inserted " + student.toString() + " at " + Util.convertDatetimeToString(new Date()));

	}

	@SuppressWarnings("unchecked")
	public List<Student> displayRecords() {
		List<Student> studentsList = new ArrayList<Student>();
		Session sessionObj = sessionFactory.getCurrentSession();
		studentsList = sessionObj.createQuery("FROM students").getResultList();
		return studentsList;
	}

	public void updateRecord(Student student) {
		Session sessionObj = sessionFactory.getCurrentSession();
		student = (Student) sessionObj.get(Student.class, student.getId());
		student.setName(student.getName());
		student.setCode(student.getCode());
		logger.info("\nStudent With Id?= " + student.getId() + " Is Successfully Updated In The Database!\n");
		jmsProducer.sendMessage("Updated " + student.toString());
	}

	public void deleteRecord(Integer id) {
		Session sessionObj = sessionFactory.getCurrentSession();
		Student student = findRecordById(id);
		sessionObj.delete(student);
		logger.info("\nStudent With Id?= " + id + " Is Successfully Deleted From The Database!\n");
		jmsProducer.sendMessage("Deleted " + student.toString());
	}

	public Student findRecordById(Integer id) {
		Student student = null;
		Session sessionObj = sessionFactory.getCurrentSession();
		student = (Student) sessionObj.get(Student.class, id);
		return student;
	}

}
