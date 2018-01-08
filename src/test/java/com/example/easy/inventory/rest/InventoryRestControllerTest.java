package com.example.easy.inventory.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.easy.commons.component.Messages;
import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.commons.helper.RestResponseBuilder;
import com.example.easy.commons.model.InfoDTO;
import com.example.easy.commons.model.UserInfo;
import com.example.easy.inventory.dto.ProductDTO;
import com.example.easy.inventory.service.ILookupService;
import com.example.easy.inventory.service.IOrderService;
import com.example.easy.inventory.service.IProductService;
import com.example.easy.inventory.service.ISaleService;
import com.example.easy.inventory.service.IStockService;
import com.example.easy.inventory.service.ISupplierService;

@RunWith(MockitoJUnitRunner.class)
public class InventoryRestControllerTest {

	@Mock
	Messages messages;

	@Mock
	Pageable pageable;

	@Mock
	SecurityContext securityContext;

	@Mock
	private RestResponseBuilder restResponseBuilder;

	@Mock
	private IProductService productService;

	@Mock
	private ISupplierService supplierService;

	@Mock
	private IStockService stockService;

	@Mock
	private ILookupService lookupService;

	@Mock
	private IOrderService orderService;

	@Mock
	private ISaleService saleService;

	@Mock
	HttpServletRequest request;

	@InjectMocks
	private InventoryRestController ctrl = new InventoryRestController();

	@Before
	public void setUp() throws Exception {
		when(securityContext.getUserPrincipal()).thenReturn(new UserInfo("tester", 115, 100));
		ReflectionTestUtils.setField(ctrl, "restResponseBuilder", new RestResponseBuilder());
		// when(restResponseBuilder.withOkResponse(any(IBaseDTO.class))).thenAnswer(i ->
		// {
		// System.out.println(i.getClass());
		// return null;
		// });

	}

	private ProductDTO getProduct(final String name) {
		final ProductDTO p = new ProductDTO();
		p.setName(name);
		p.setDescription("description");

		return p;
	}

	@Test
	public void testAddProduct() throws CustomBadRequestException {
		final ProductDTO p = getProduct("productA");

		// setup mock response
		when(productService.save(p)).thenReturn(p);

		final Response r = ctrl.addProduct(request, p);

		ProductDTO resp = (ProductDTO) r.getEntity();

		assertEquals("product name must match...", p.getName(), resp.getName());
		assertEquals("product description must match...", p.getDescription(), resp.getDescription());
	}

	@Test
	public void testAddProductWithBadRequestResponse() throws CustomBadRequestException {
		final ProductDTO p = null;
		// setup mock response
		when(messages.get("bad.request.msg")).thenReturn(Response.Status.BAD_REQUEST.getReasonPhrase());

		final Response r = ctrl.addProduct(request, p);

		assertEquals("product status code must match...", Response.Status.BAD_REQUEST.getStatusCode(), r.getStatus());
		assertEquals("product error reason must match...", Response.Status.BAD_REQUEST.getReasonPhrase(),
				r.getStatusInfo().getReasonPhrase());
	}

	@Test
	public void testAddProductWithBadRequestResponseForUnexpectedError() throws CustomBadRequestException {
		ReflectionTestUtils.setField(ctrl, "securityContext", null);

		final ProductDTO p = getProduct("productA");

		// setup mock response
		when(messages.get("server.error.check.details"))
				.thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());

		final Response r = ctrl.addProduct(request, p);

		assertEquals("product name must match...", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
				r.getStatus());
		assertEquals("product name must match...", Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(),
				r.getStatusInfo().getReasonPhrase());
	}

	@Test
	public void testGetProducts() {

		final Collection col = new ArrayList<ProductDTO>();

		// setup mock response
		doReturn(col).when(productService).getAll(anyInt(), any());

		final Response r = ctrl.getProducts(request, pageable);

		verify(productService, times(1)).getAll(anyInt(), any());

		assertEquals("status name must match...", 200, r.getStatus());
	}

	@Test
	public void testGetProductsWithBadRequestResponse() throws CustomBadRequestException {
		when(securityContext.getUserPrincipal()).thenReturn(null);
		// setup mock response
		when(messages.get("resource.unauthorized.access")).thenReturn(Response.Status.UNAUTHORIZED.getReasonPhrase());

		final Response r = ctrl.getProducts(request, pageable);

		assertEquals("product status code must match...", Response.Status.BAD_REQUEST.getStatusCode(), r.getStatus());
		assertEquals("product error reason must match...", Response.Status.BAD_REQUEST.getReasonPhrase(),
				r.getStatusInfo().getReasonPhrase());
	}
	
	@Test
	public void testGetProductWithBadRequestResponseForUnexpectedError() throws CustomBadRequestException {
		ReflectionTestUtils.setField(ctrl, "securityContext", null);

		// setup mock response
		when(messages.get("server.error.check.details"))
				.thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());

		final Response r = ctrl.getProducts(request, pageable);

		assertEquals("product name must match...", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
				r.getStatus());
		assertEquals("product name must match...", Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(),
				r.getStatusInfo().getReasonPhrase());
	}


	@Test
	public void testDeleteProduct() throws CustomBadRequestException {
		final InfoDTO dto = new InfoDTO("product deleted.");
		final Response r = ctrl.deleteProduct(123);
		verify(productService, times(1)).delete(anyInt(), any());
		assertEquals("status name must match...", 200, r.getStatus());
	}
	
	@Test
	public void testDeleteProductsWithBadRequestResponse() throws CustomBadRequestException {
		when(securityContext.getUserPrincipal()).thenReturn(null);
		// setup mock response
		when(messages.get("resource.unauthorized.access")).thenReturn(Response.Status.UNAUTHORIZED.getReasonPhrase());

		final Response r = ctrl.deleteProduct(123);

		assertEquals("product status code must match...", Response.Status.BAD_REQUEST.getStatusCode(), r.getStatus());
		assertEquals("product error reason must match...", Response.Status.BAD_REQUEST.getReasonPhrase(),
				r.getStatusInfo().getReasonPhrase());
	}
	
	@Test
	public void testDeleteProductWithBadRequestResponseForUnexpectedError() throws CustomBadRequestException {
		ReflectionTestUtils.setField(ctrl, "securityContext", null);

		// setup mock response
		when(messages.get("server.error.check.details"))
				.thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());

		final Response r = ctrl.deleteProduct(123);

		assertEquals("product name must match...", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
				r.getStatus());
		assertEquals("product name must match...", Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(),
				r.getStatusInfo().getReasonPhrase());
	}
	
	
	//
	// @Test
	// public void testAddSupplier() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetSuppliers() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testDeleteSupplier() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetSupplierFrequencyReport() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAddOrder() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetOrders() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testDeleteOrder() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetOrder() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetOrdersBySupplier() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetStock() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testManageBooking() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetSaleHttpServletRequestPageable() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetSaleInteger() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testDeleteSale() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetAlllookupValues() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAddLookup() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testDeleteLookup() {
	// fail("Not yet implemented");
	// }

}
