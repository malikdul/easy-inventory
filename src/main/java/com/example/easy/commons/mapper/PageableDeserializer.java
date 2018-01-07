package com.example.easy.commons.mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.example.easy.commons.constants.AppConstant;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;

@JsonComponent
public class PageableDeserializer extends JsonDeserializer<Pageable>
{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Pageable deserialize(JsonParser jp, DeserializationContext arg1) throws IOException, JsonProcessingException
	{
		int pageNumber = 0;
		int pageSize = AppConstant.DEFAULT_PAGE_SIZE;
		PageRequest pgRequest = null;
		try
		{
			JsonNode node = jp.getCodec().readTree(jp);
			logger.debug("json Node::" + node.toString());

			if (node.get("page") != null)
			{
				pageNumber = (Integer) ((IntNode) node.get("page")).numberValue();
			}
			if (node.get("size") != null)
			{
				pageSize = (Integer) ((IntNode) node.get("size")).numberValue();
			}

			ArrayNode sortArray = (ArrayNode) node.get("sort");
			if (sortArray != null)
			{
				List<Order> sortOrder = new ArrayList<>();

				Iterator it = sortArray.iterator();
				while (it.hasNext())
				{
					JsonNode sortNode = (JsonNode) it.next();

					if (sortNode != null)
					{
						String sortProprty = sortNode.get("property").asText();
						String sortDirection = sortNode.get("direction").asText();

						sortOrder.add(new Order((sortDirection != null && sortDirection.equalsIgnoreCase(Sort.Direction.DESC.toString()) ? Sort.Direction.DESC
								: Sort.Direction.ASC), sortProprty));

						logger.info(sortOrder.toString());
					}
				}
				pgRequest = new PageRequest(pageNumber, pageSize, new Sort(sortOrder));
			} else
			{
				pgRequest = new PageRequest(pageNumber, pageSize);
			}

			// pgRequest = new PageRequest(pageNumber, pageSize);

			/*ObjectMapper mapper = new ObjectMapper();
			String json =  node.toString();
			logger.debug("JSON::" + json);

			pgRequest = mapper.readValue(json, PageRequest.class);*/
			
			logger.debug("pgRequest::" + pgRequest);
		} catch (Exception exp)
		{
			logger.error("Error parsing Josn for Pageable::" + exp.getMessage(), exp);
			return new PageRequest(pageNumber, pageSize);
		}

		return pgRequest;
	}

}
