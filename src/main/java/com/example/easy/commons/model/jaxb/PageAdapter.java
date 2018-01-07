package com.example.easy.commons.model.jaxb;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.data.domain.Page;

@SuppressWarnings("rawtypes")
public class PageAdapter extends XmlAdapter<JaxbPage, Page> {

	public Page unmarshal(final JaxbPage pojo) throws Exception {
		return new PageResponse(pojo);
	}

	public JaxbPage marshal(Page page) throws Exception {
		JaxbPage pojo = new JaxbPage();
		pojo.setContent(page.getContent());
		pojo.setNumber(page.getNumber());
		pojo.setNumberOfElements(page.getNumberOfElements());
		pojo.setSize(page.getSize());
		pojo.setTotalElements(page.getTotalElements());
		pojo.setTotalPages(page.getTotalPages());
		return pojo;
	}

}
