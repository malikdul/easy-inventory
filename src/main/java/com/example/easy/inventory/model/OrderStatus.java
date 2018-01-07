package com.example.easy.inventory.model;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Converter;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
	
	CANCELLED("cancelled"),
	DELIVERED("delivered"),
	RECEIVED("received"),
	PENDING("pending"),
	;//end enums
	
	
	private static final Map<String, OrderStatus> MAP = Stream.of(OrderStatus.values()).collect(Collectors.toMap(Object::toString, Function.identity()));
    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return value;
    }

    public static OrderStatus fromValue(String value){
        return MAP.get(value.toLowerCase());
    }

}
