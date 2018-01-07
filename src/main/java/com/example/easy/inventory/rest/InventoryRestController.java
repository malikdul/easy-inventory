package com.example.easy.inventory.rest;

import java.net.HttpURLConnection;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.easy.commons.annotations.InSecure;
import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.commons.helper.Roles;
import com.example.easy.commons.helper.RestResponseBuilder;
import com.example.easy.commons.mapper.PageableDeserializer;
import com.example.easy.commons.model.InfoDTO;
import com.example.easy.commons.model.MessageResponseDTO;
import com.example.easy.commons.model.UserInfo;
import com.example.easy.commons.rest.BaseRestController;
import com.example.easy.inventory.dto.LookupDTO;
import com.example.easy.inventory.dto.OrderDTO;
import com.example.easy.inventory.dto.ProductDTO;
import com.example.easy.inventory.dto.SaleDTO;
import com.example.easy.inventory.dto.StockDTO;
import com.example.easy.inventory.dto.SupplierDTO;
import com.example.easy.inventory.service.ILookupService;
import com.example.easy.inventory.service.IOrderService;
import com.example.easy.inventory.service.IProductService;
import com.example.easy.inventory.service.ISaleService;
import com.example.easy.inventory.service.IStockService;
import com.example.easy.inventory.service.ISupplierService;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("inventory")
@Api(value = "easy inventory resources", produces = MediaType.APPLICATION_JSON)
public class InventoryRestController extends BaseRestController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestResponseBuilder restResponseBuilder;
	
	@Autowired
	private IProductService productService;
	
	@Autowired
	private ISupplierService supplierService;
	
	@Autowired
	private IStockService stockService;

	@Autowired
	private ILookupService lookupService;
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private ISaleService saleService;

	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@POST
	@Path("/product/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Add/Update stock Product", notes = "<b>This method is used to add/update product</b>", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, response = ProductDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Add/Update stock product", response = ProductDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response addProduct(@Context HttpServletRequest request, @Valid ProductDTO requestVo) {
		logger.info("Adding new stock product." + requestVo);
		try {
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			logger.debug("User Info loaded::" + userInfo);

			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			if (requestVo == null) {
				throw new CustomBadRequestException(messages.get("bad.request.msg"));
			}
			
			logger.debug("stock product::" + userInfo.toString());

			requestVo.setAccountId(userInfo.getAccountId());
			requestVo.setCreatedBy(userInfo.getUserId());
			return restResponseBuilder.withOkResponse(productService.save(requestVo)).build();

		} catch (CustomBadRequestException e) {
			logger.error("Error:: Adding new Prodcut::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Adding new stock product::" + e.getMessage(), e);

			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}
	}

	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@POST
	@Path("/product/all")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get All stock products", notes = "<b>This method is used to Get All stock products</b>", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, response = ProductDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Return all products, possibly paginated.", response = ProductDTO.class, responseContainer = "List"),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response getProducts(@Context HttpServletRequest request,
			@JsonDeserialize(using = PageableDeserializer.class) Pageable pageable) {
		try {
			logger.info("Getting get all stock products ::" + pageable);
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			logger.debug("User Info loaded::" + userInfo);
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}

			logger.debug("Getting all stock products for account." + userInfo.getAccountId());

			return restResponseBuilder.withOkResponse(productService.getAll(userInfo.getAccountId(), pageable))
					.build();

		}  catch (CustomBadRequestException e) {
			logger.error("Error:: get all Prodcuts::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Get all stock products::" + e.getMessage(), e);

			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}
	}
	
	
	@DELETE
	@Path("/product/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete product", notes = "<b>This method delete product by Id</b>")
	@ApiResponses(value =
	{ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Object Deleted", response = MessageResponseDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response deleteProduct(@PathParam("id") Integer id)
	{
		try
		{
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			logger.debug("User Info loaded::" + userInfo);
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			logger.info("Deleting product by accountId and id::" + userInfo.getAccountId() + "::" + id);
			productService.delete(userInfo.getAccountId(), id);
			return restResponseBuilder.withOkResponse(new InfoDTO("product deleted.")).build();
		}  catch (CustomBadRequestException e) {
			logger.error("Error:: delete Prodcut::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Deleting product " + e.getMessage(), e);
			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}

	}

	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@POST
	@Path("/supplier/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Add/Update stock Supplier", notes = "<b>This method is used to add/update Supplier</b>", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, response = SupplierDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Add/Update stock Supplier", response = SupplierDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response addSupplier(@Context HttpServletRequest request, @Valid SupplierDTO requestVo) {
		logger.info("Adding new stock Supplier." + requestVo);
		try {
			userInfo = (UserInfo) securityContext.getUserPrincipal();

			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			if (requestVo == null) {
				throw new CustomBadRequestException(messages.get("bad.request.msg"));
			}
			
			if(StringUtils.isEmpty(requestVo.getName())) {
				throw new CustomBadRequestException(messages.get("supplier.name.missing"));
	    	}
			
			if (CollectionUtils.isEmpty(requestVo.getTelecoms())) {
				throw new CustomBadRequestException(messages.get("supplier.telecom.missing"));
			}
			
			if (CollectionUtils.isEmpty(requestVo.getAddresses())) {
				throw new CustomBadRequestException(messages.get("supplier.address.missing"));
			}

			logger.debug("User Info loaded::" + userInfo);
			requestVo.setAccountId(userInfo.getAccountId());
			requestVo.setCreatedBy(userInfo.getUserId());
			return restResponseBuilder.withOkResponse(supplierService.save(requestVo)).build();

		} catch (CustomBadRequestException e) {
			logger.error("Error:: Adding new Supplier::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Adding new stock Supplier::" + e.getMessage(), e);

			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}
	}

	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@POST
	@Path("/supplier/all")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get All stock Suppliers", notes = "<b>This method is used to Get All stock Suppliers</b>", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, response = SupplierDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Return all Suppliers, possibly paginated.", response = SupplierDTO.class, responseContainer = "List"),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response getSuppliers(@Context HttpServletRequest request,
			@JsonDeserialize(using = PageableDeserializer.class) Pageable pageable) {
		try {
			logger.info("Getting get all stock Suppliers ::" + pageable);
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			logger.debug("Getting all stock Suppliers for account." + userInfo.getAccountId());

			return restResponseBuilder.withOkResponse(supplierService.getAll(userInfo.getAccountId(), pageable))
					.build();

		} catch (CustomBadRequestException e) {
			logger.error("Error:: get suppliers::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Get all stock Suppliers::" + e.getMessage(), e);

			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}
	}
	
	
	@DELETE
	@Path("/supplier/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete Supplier", notes = "<b>This method delete Supplier by Id</b>")
	@ApiResponses(value =
	{ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Object Deleted", response = MessageResponseDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response deleteSupplier(@PathParam("id") Integer id)
	{
		try
		{
			userInfo = (UserInfo) securityContext.getUserPrincipal();

			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			logger.info("Deleting supplier by id::" + id);
			supplierService.delete(id);
			return restResponseBuilder.withOkResponse(new InfoDTO("Supplier deleted.")).build();
		} catch (CustomBadRequestException e) {
			logger.error("Error:: delete suppliers::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Deleting supplier " + e.getMessage(), e);
			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}

	}
	
	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@GET
	@Path("/supplier/frequency")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get All stock Suppliers", notes = "<b>This method is used to Get All stock Suppliers</b>", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, response = SupplierDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Return all Suppliers, possibly paginated.", response = SupplierDTO.class, responseContainer = "List"),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response getSupplierFrequencyReport(@Context HttpServletRequest request) {
		try {
			logger.info("get Supplier Frequency Report");
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			logger.debug("getSupplierFrequencyReport for account." + userInfo.getAccountId());

			return restResponseBuilder.withOkResponse(supplierService.getSupplierFrequencyReport(userInfo.getAccountId()))
					.build();

		} catch (CustomBadRequestException e) {
			logger.error("Error:: get suppliers frequency report::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error::getSupplierFrequencyReport::" + e.getMessage(), e);

			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}
	}
	
	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@POST
	@Path("/order/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Add/Update stock order", notes = "<b>This method is used to add/update order</b>", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, response = OrderDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Add/Update stock order", response = OrderDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response addOrder(@Context HttpServletRequest request, @Valid OrderDTO requestVo) {
		logger.info("Adding new stock order." + requestVo);
		try {
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			if (requestVo == null) {
				throw new CustomBadRequestException(messages.get("bad.request.msg"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			
			if (requestVo.getType() == null) {
				return restResponseBuilder.withStatus(Response.Status.BAD_REQUEST)
						.withMessage(messages.get("bad.request.msg")).withDetails(messages.get("order.type.missing"))
						.build();
			}
			
			if (requestVo.getDetails() == null) {
				return restResponseBuilder.withStatus(Response.Status.BAD_REQUEST)
						.withMessage(messages.get("bad.request.msg")).withDetails(messages.get("order.details.missing"))
						.build();
			}
			
			requestVo.setAccountId(userInfo.getAccountId());
			requestVo.setCreatedBy(userInfo.getUserId());
			return restResponseBuilder.withOkResponse(orderService.save(requestVo)).build();

		} catch (CustomBadRequestException e) {
			logger.error("Error:: add order::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Adding new stock order::" + e.getMessage(), e);

			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}
	}

	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@POST
	@Path("/order/all")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get All stock orders", notes = "<b>This method is used to Get All stock orders</b>", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, response = OrderDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Return all orders, possibly paginated.", response = OrderDTO.class, responseContainer = "List"),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response getOrders(@Context HttpServletRequest request,
			@JsonDeserialize(using = PageableDeserializer.class) Pageable pageable) {
		try {
			logger.info("Getting get all stock orders ::" + pageable);
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			logger.debug("Getting all stock orders for account." + userInfo.getAccountId());

			return restResponseBuilder.withOkResponse(orderService.getAll(userInfo.getAccountId(), pageable))
					.build();

		} catch (CustomBadRequestException e) {
			logger.error("Error:: get order::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Get all stock orders::" + e.getMessage(), e);

			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}
	}
	
	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@DELETE
	@Path("/order/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete order", notes = "<b>This method delete order by Id</b>")
	@ApiResponses(value =
	{ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Object Deleted", response = MessageResponseDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response deleteOrder(@PathParam("id") Integer id)
	{
		try
		{
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			
			logger.info("Deleting order by id::" + id);
			orderService.delete(id);
			return restResponseBuilder.withOkResponse(new InfoDTO("order deleted.")).build();
		} catch (CustomBadRequestException e) {
			logger.error("Error:: delete order::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Deleting order " + e.getMessage(), e);
			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}

	}
	
	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@GET
	@Path("/order/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get order", notes = "<b>This method is to get order by Id</b>")
	@ApiResponses(value =
	{ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "order object", response = OrderDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response getOrder(@PathParam("id") Integer id)
	{
		try
		{
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			
			logger.info("get order by id::" + id);
			return restResponseBuilder.withOkResponse(orderService.findOne(id)).build();
		} catch (CustomBadRequestException e) {
			logger.error("Error:: get order::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: get order by id " + e.getMessage(), e);
			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}

	}
	
	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@GET
	@Path("/order/supplier/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get orders by supplier", notes = "<b>This method is to get orders by supplier</b>")
	@ApiResponses(value =
	{ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "order object", response = OrderDTO.class, responseContainer = "List"),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response getOrdersBySupplier(@PathParam("id") Integer supplierId)
	{
		try
		{
			logger.info("get orders by supplier::" + supplierId);
			
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			logger.debug("Getting all stock orders by supplier for account." + userInfo.getAccountId());

			
			return restResponseBuilder.withOkResponse(orderService.getAllBySupplier(userInfo.getAccountId(), supplierId, null)).build();
		} catch (CustomBadRequestException e) {
			logger.error("Error:: get order by supplier::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: get order by id " + e.getMessage(), e);
			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}

	}
	
	
	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@POST
	@Path("/stock/all")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get All stock", notes = "<b>This method is used to Get All stock</b>", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, response = StockDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Return all stock, possibly paginated.", response = StockDTO.class, responseContainer = "List"),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response getStock(@Context HttpServletRequest request,
			@JsonDeserialize(using = PageableDeserializer.class) Pageable pageable) {
		try {
			logger.info("Getting get all stock ::" + pageable);
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			logger.debug("Getting all stock for account." + userInfo.getAccountId());

			return restResponseBuilder.withOkResponse(stockService.getAll(userInfo.getAccountId(), pageable))
					.build();

		} catch (CustomBadRequestException e) {
			logger.error("Error:: get stock::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Get all stock::" + e.getMessage(), e);

			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}
	}
	

	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@POST
	@Path("/sale/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Add/Update manage booking", notes = "<b>This method is used to add/update manage booking stock sale</b>", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, response = SaleDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Add/Update manage booking", response = SaleDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response saleAdd(@Context HttpServletRequest request, @Valid SaleDTO requestVo) {
		logger.info("Adding new sale." + requestVo);
		try {
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			if (requestVo == null) {
				throw new CustomBadRequestException(messages.get("bad.request.msg"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			
			
			if (requestVo.getDetails() == null) {
				return restResponseBuilder.withStatus(Response.Status.BAD_REQUEST)
						.withMessage(messages.get("bad.request.msg")).withDetails(messages.get("sale.details.missing"))
						.build();
			}
			
			requestVo.setAccountId(userInfo.getAccountId());
			requestVo.setCreatedBy(userInfo.getUserId());
			return restResponseBuilder.withOkResponse(saleService.save(requestVo)).build();

		} catch (CustomBadRequestException e) {
			logger.error("Error:: add sale::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Adding new manage booking::" + e.getMessage(), e);

			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}
	}
	
	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@POST
	@Path("/sale/all")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get All stock", notes = "<b>This method is used to Get All sales</b>", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, response = StockDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Return all sales, possibly paginated.", response = SaleDTO.class, responseContainer = "List"),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response getSale(@Context HttpServletRequest request,
			@JsonDeserialize(using = PageableDeserializer.class) Pageable pageable) {
		try {
			logger.info("Getting get all sales ::" + pageable);
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			logger.debug("Getting all sales for account." + userInfo.getAccountId());

			return restResponseBuilder.withOkResponse(saleService.getAll(userInfo.getAccountId(), pageable))
					.build();

		} catch (CustomBadRequestException e) {
			logger.error("Error:: get all sales::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Get all sales::" + e.getMessage(), e);

			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}
	}
	
	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@GET
	@Path("/sale/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get order", notes = "<b>This method is to get sale by Id</b>")
	@ApiResponses(value =
	{ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "sale object", response = SaleDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response getSale(@PathParam("id") Integer id)
	{
		try
		{
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}

			logger.debug("User Info loaded::" + userInfo);
			
			logger.info("get sale by id::" + id);
			return restResponseBuilder.withOkResponse(saleService.findOne(id)).build();
		} catch (CustomBadRequestException e) {
			logger.error("Error:: get sale by id::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: get sale by id " + e.getMessage(), e);
			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}

	}
	
	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@DELETE
	@Path("/sale/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete sale", notes = "<b>This method delete sale by Id</b>")
	@ApiResponses(value =
	{ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Object Deleted", response = MessageResponseDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response deleteSale(@PathParam("id") Integer id)
	{
		try
		{
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}

			logger.debug("User Info loaded::" + userInfo);
			logger.info("Deleting sale by id::" + id);
			saleService.delete(id);
			return restResponseBuilder.withOkResponse(new InfoDTO("sale deleted.")).build();
		} catch (CustomBadRequestException e) {
			logger.error("Error:: delete sale::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Deleting sale " + e.getMessage(), e);
			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}

	}
	
	

	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@InSecure
	@GET
	@Path("/lookup/all")
	@Produces("application/json")
	@ApiOperation(value = "Get Lookup By Type", notes = "<b>This method is used to Get Lookup By Type</b>", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, response = LookupDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Return Lookup list", response = LookupDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response getAlllookupValues(@Context HttpServletRequest request, @JsonDeserialize(using = PageableDeserializer.class) Pageable pageable) {
		try {
			logger.info("Getting getAlllookupValues ");
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}

			logger.debug("User Info loaded::" + userInfo);
			logger.debug("Getting all Lookup values for account: " + userInfo.getAccountId());

			return restResponseBuilder.withOkResponse(lookupService.getAll(userInfo.getAccountId(), pageable)).build();
		} catch (CustomBadRequestException e) {
			logger.error("Error:: get all lookup values::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Getting getAlllookupValues ::" + e.getMessage(), e);
			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}
	}

	@RolesAllowed({ Roles.ROLE_SADMIN, Roles.ROLE_ADMIN, Roles.ROLE_OWNER, Roles.ROLE_USER })
	@POST
	@Path("/lookup/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Add/Update stock lookup", notes = "<b>This method is used to add/update lookup</b>", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, response = LookupDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Add/Update stock lookup", response = LookupDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response addLookup(@Context HttpServletRequest request, @Valid LookupDTO requestVo) {
		logger.info("Adding new lookup lookup." + requestVo);
		try {
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			if (requestVo == null) {
				throw new CustomBadRequestException(messages.get("bad.request.msg"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			logger.debug("stock lookup::" + userInfo.toString());

			requestVo.setAccountId(userInfo.getAccountId());
			requestVo.setCreatedBy(userInfo.getUserId());
			return restResponseBuilder.withOkResponse(lookupService.save(requestVo)).build();

		} catch (CustomBadRequestException e) {
			logger.error("Error:: add lookup::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Adding new stock lookup::" + e.getMessage(), e);

			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}
	}
	
	@DELETE
	@Path("/lookup/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete Lookup", notes = "<b>This method delete Lookup by Id</b>")
	@ApiResponses(value =
	{ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Object Deleted", response = MessageResponseDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "JSON parser exception, invalid request JSON"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server problems") })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })

	public Response deleteLookup(@PathParam("id") Integer id)
	{
		try
		{
			userInfo = (UserInfo) securityContext.getUserPrincipal();
			if (userInfo == null) {
				throw new CustomBadRequestException(messages.get("resource.unauthorized.access"));
			}
			
			logger.debug("User Info loaded::" + userInfo);
			
			logger.info("Deleting Lookup by id::" + id);
			lookupService.delete(id);
			return restResponseBuilder.withOkResponse(new InfoDTO("Lookup deleted.")).build();
		} catch (CustomBadRequestException e) {
			logger.error("Error:: delete lookup::" + e.getMessage(), e);
			return restResponseBuilder
					.withBadRequest(e.getMessage())
					.build();
		} catch (Exception e) {
			logger.error("Error:: Deleting Lookup " + e.getMessage(), e);
			return restResponseBuilder.withStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.withMessage(messages.get("server.error.check.details")).withDetails(e.getMessage()).build();
		}

	}
	

}
