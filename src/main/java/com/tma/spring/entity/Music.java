package com.tma.spring.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tma.spring.util.Util;

@Entity(name = "musics")
public class Music extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@ManyToOne
	@JoinColumn(name = "authorId")
	private Author author;

	@ManyToOne
	@JoinColumn(name = "categoryId")
	private Category category;

	@Column(name = "publishedDate")
	private Date publishedDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public Music(String name, Author author, Category category, Date publishedDate) {
		super();
		this.name = name;
		this.author = author;
		this.category = category;
		this.publishedDate = publishedDate;
	}

	@Override
	public String toString() {
		return "Music: name= " + name + ", " + author + ", " + category + ", publishedDate= "
				+ Util.convertDateToString(publishedDate);
	}

}
