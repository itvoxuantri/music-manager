package com.tma.spring;

import java.io.IOException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.JMX;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tma.spring.mbean.StudentServiceImplMBean;

public class AppMain {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws MalformedObjectNameException, InstanceAlreadyExistsException,
			MBeanRegistrationException, NotCompliantMBeanException, BeansException, IOException {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
		MBeanServer beanServer = (MBeanServer) applicationContext.getBean("beanServer");
		ObjectName mbeanName = new ObjectName("bean:name=studentService");
		StudentServiceImplMBean studentService = JMX.newMBeanProxy(beanServer, mbeanName, StudentServiceImplMBean.class,
				true);
		// Student student = new Student("Võ A", "16130629");
		// studentService.createRecord(student);
		System.out.println(studentService.findRecordById(32));
		System.out.println(studentService.displayRecords());
	}

}
