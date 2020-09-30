package com.tma.spring.mbean;

import java.util.List;

import com.tma.spring.entity.Student;

public interface StudentServiceImplMBean {

	void createRecord(Student student);

	List<Student> displayRecords();

	void updateRecord(Student student);

	void deleteRecord(Integer id);

	Student findRecordById(Integer id);
}
