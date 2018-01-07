package com.example.easy.commons.model.jaxb;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SuppressWarnings("rawtypes")

public class PageResponse implements Page {

	private final JaxbPage pojo;

	public PageResponse(JaxbPage pojo) {
		this.pojo = pojo;
	}

	@Override
	public Pageable nextPageable()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pageable previousPageable()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page map(Converter converter)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public List getContent() {
		return pojo.getContent() != null ? pojo.getContent()
				: Collections.EMPTY_LIST;
	}

	public int getNumber() {
		return pojo.getNumber();
	}

	public int getNumberOfElements() {
		return pojo.getNumberOfElements();
	}

	public int getSize() {
		return pojo.getSize();
	}

	public Sort getSort() {
		try {
			return SortAdapter.getInstance().unmarshal(pojo.getSort());
		} catch (Exception e) {
			return null;
		}
	}

	public long getTotalElements() {
		return pojo.getTotalElements();
	}

	public int getTotalPages() {
		return pojo.getTotalPages();
	}

	public boolean hasContent() {
		return getContent().size() > 0;
	}

	public boolean hasNext() {
		return false;
	}

	public boolean hasPrevious() {
		return false;
	}

	public boolean isFirst() {
		return false;
	}

	public boolean isLast() {
		return false;
	}

	public Iterator iterator() {
		return null;
	}

	

}
