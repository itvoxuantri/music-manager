package com.tma.spring.service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.NotificationBroadcasterSupport;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.tma.spring.entity.Music;
import com.tma.spring.jms.JmsProducer;
import com.tma.spring.mbean.MusicServiceImplMBean;
import com.tma.spring.util.Util;

public class MusicServiceImpl extends NotificationBroadcasterSupport implements MusicServiceImplMBean {
	public final static Logger logger = Logger.getLogger(StudentServiceImpl.class);

	private SessionFactory sessionFactory;
	JmsProducer jmsProducer;

	public MusicServiceImpl(SessionFactory sessionFactory, JmsProducer jmsProducer)
			throws URISyntaxException, Exception {
		this.sessionFactory = sessionFactory;
		this.jmsProducer = jmsProducer;
	}

	public void createRecord(Music music) {
		Session sessionObj = sessionFactory.getCurrentSession();
		sessionObj.save(music);
		jmsProducer.sendMessage("Inserted " + music.toString() + " at " + Util.convertDatetimeToString(new Date()));
	}

	@SuppressWarnings("unchecked")
	public List<Music> displayRecords() {
		List<Music> musicsList = new ArrayList<Music>();
		Session sessionObj = sessionFactory.getCurrentSession();
		musicsList = sessionObj.createQuery("FROM musics").getResultList();
		return musicsList;
	}

	public void updateRecord(Music music) {
		Session sessionObj = sessionFactory.getCurrentSession();
		music = (Music) sessionObj.get(Music.class, music.getId());
		music.setName(music.getName());
		logger.info("\nMusic With Id?= " + music.getId() + " Is Successfully Updated In The Database!\n");
		jmsProducer.sendMessage("Updated " + music.toString() + " at " + Util.convertDatetimeToString(new Date()));
	}

	public void deleteRecord(Integer id) {
		Session sessionObj = sessionFactory.getCurrentSession();
		Music music = findRecordById(id);
		sessionObj.delete(music);
		logger.info("\nMusic With Id?= " + id + " Is Successfully Deleted From The Database!\n");
		jmsProducer.sendMessage("Deleted " + music.toString() + " at " + Util.convertDatetimeToString(new Date()));
	}

	public Music findRecordById(Integer id) {
		Music music = null;
		Session sessionObj = sessionFactory.getCurrentSession();
		music = (Music) sessionObj.get(Music.class, id);
		return music;
	}

}
