package com.example.easy.commons.model.jaxb;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pageable")
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbPageable {

	private int pageNumber;

	private int pageSize;

	private int offset;

	private JaxbSort sort;

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setSort(JaxbSort sort) {
		this.sort = sort;
	}

	public JaxbSort getSort() {
		return sort;
	}
}
