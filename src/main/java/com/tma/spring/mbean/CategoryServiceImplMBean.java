package com.tma.spring.mbean;

import java.util.List;

import com.tma.spring.entity.Category;

public interface CategoryServiceImplMBean {
	void createRecord(Category category);

	List<Category> displayRecords();

	void updateRecord(Category category);

	void deleteRecord(Integer id);

	Category findRecordById(Integer id);
}
