package com.example.easy.commons.model.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "page")
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbPage {

	@SuppressWarnings("rawtypes")
	private List content;

	private int number;

	private int size;

	private int totalPages;

	private int numberOfElements;

	private long totalElements;

	private JaxbSort sort;

	@SuppressWarnings("rawtypes")
	public List getContent() {
		return content;
	}

	@SuppressWarnings("rawtypes")
	public void setContent(List content) {
		this.content = content;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public JaxbSort getSort() {
		return sort;
	}

	public void setSort(JaxbSort sort) {
		this.sort = sort;
	}

}
