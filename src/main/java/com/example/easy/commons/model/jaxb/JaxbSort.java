package com.example.easy.commons.model.jaxb;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sort")
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbSort {

	private List<JaxbOrder> orders;

	public void setOrders(List<JaxbOrder> orders) {
		this.orders = orders;
	}

	public List<JaxbOrder> getOrders() {
		return orders;
	}
}
