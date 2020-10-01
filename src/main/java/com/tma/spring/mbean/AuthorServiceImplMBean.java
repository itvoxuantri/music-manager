package com.tma.spring.mbean;

import java.util.List;

import com.tma.spring.entity.Author;

public interface AuthorServiceImplMBean {
	void createRecord(Author author);

	List<Author> displayRecords();

	void updateRecord(Author author);

	void deleteRecord(Integer id);

	Author findRecordById(Integer id);
}
