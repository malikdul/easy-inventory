package com.example.easy.inventory.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    public String convertToDatabaseColumn(OrderStatus value) {
        if ( value == null ) {
            return null;
        }

        return value.toString();
    }

    public OrderStatus convertToEntityAttribute(String value) {
        if ( value == null ) {
            return null;
        }

        return OrderStatus.fromValue(value);
    }
}