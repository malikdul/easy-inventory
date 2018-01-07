package com.example.easy.commons.model.jaxb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class SortAdapter extends XmlAdapter<JaxbSort, Sort> {

	private static final SortAdapter INSTANCE = new SortAdapter();

	public static SortAdapter getInstance() {
		return INSTANCE;
	}

	@Override
	public JaxbSort marshal(Sort sort) throws Exception {
		if (sort == null) {
			return null;
		}
		JaxbSort pojoSort = new JaxbSort();
		List<JaxbOrder> pojoOrders = new ArrayList<JaxbOrder>();
		pojoSort.setOrders(pojoOrders);
		for (Iterator<Order> orders = sort.iterator(); orders.hasNext();) {
			Order order = orders.next();
			JaxbOrder pojoOrder = new JaxbOrder();
			pojoOrder.setDirection(order.getDirection().name());
			pojoOrder.setProperty(order.getProperty());
			pojoOrders.add(pojoOrder);
		}
		return pojoSort;
	}

	@Override
	public Sort unmarshal(JaxbSort pojo) throws Exception {
		if (pojo == null) {
			return null;
		}
		List<Order> orders = new ArrayList<Sort.Order>();
		for (JaxbOrder pojoOrder : pojo.getOrders()) {
			Direction direction = Direction
					.fromString(pojoOrder.getDirection());
			Order order = new Order(direction, pojoOrder.getProperty());
			orders.add(order);
		}

		return new Sort(orders);
	}

}
