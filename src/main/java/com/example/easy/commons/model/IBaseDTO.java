package com.example.easy.commons.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;

@XmlRootElement
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface IBaseDTO
{

}
