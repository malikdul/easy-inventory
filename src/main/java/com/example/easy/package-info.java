/**
 * 
 */
@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(value=PageableAdapter.class, type=SortAdapter.class)
})
package com.example.easy;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import com.example.easy.commons.model.jaxb.PageableAdapter;
import com.example.easy.commons.model.jaxb.SortAdapter;
