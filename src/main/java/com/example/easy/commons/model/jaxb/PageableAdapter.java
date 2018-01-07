package com.example.easy.commons.model.jaxb;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableAdapter extends XmlAdapter<JaxbPageable, Pageable> {

	@Override
	public JaxbPageable marshal(Pageable pageable) throws Exception {
		JaxbPageable pojo = new JaxbPageable();
		pojo.setOffset(pageable.getOffset());
		pojo.setPageNumber(pageable.getPageNumber());
		pojo.setPageSize(pageable.getPageSize());
		pojo.setSort(SortAdapter.getInstance().marshal(pageable.getSort()));
		return pojo;
	}

	@Override
	public Pageable unmarshal(JaxbPageable pojo) throws Exception {
		Sort sort = SortAdapter.getInstance().unmarshal(pojo.getSort());
		if (sort != null) {
			return new PageRequest(pojo.getPageNumber(), pojo.getPageSize(),
					sort);
		}
		return new PageRequest(pojo.getPageNumber(), pojo.getPageSize());
	}

}
