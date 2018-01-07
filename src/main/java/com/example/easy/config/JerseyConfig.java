package com.example.easy.config;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;
import org.glassfish.jersey.server.validation.ValidationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.easy.commons.mapper.ConstraintViolationExceptionMapper;
import com.example.easy.commons.mapper.PageableValueFactoryProvider;
import com.example.easy.filter.AuthenticationFilter;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig
{
	@Value("${app.base.path}")
	String APP_BASE_PATH="/api";

	public JerseyConfig()
	{
		registerEndpoints();
		configureSwagger();
		configurePagination();
	}
	
	private void configurePagination() {
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(PageableValueFactoryProvider.class)
                        .to(ValueFactoryProvider.class)
                        .in(Singleton.class);
            }
        });
	}

	private void registerEndpoints()
	{
		packages("com.example.easy");
		// register(TestEndpointRestController.class);

		register(ApiListingResource.class);
		register(SwaggerSerializers.class);
		// register(AuthenticationFilter.class);
		register(ConstraintViolationExceptionMapper.class);
		register(ValidationFeature.class);

	}

	private void configureSwagger()
	{

		register(ApiListingResource.class);
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0");
		beanConfig.setSchemes(new String[] { "http", "https" });
		// beanConfig.setHost("localhost:8082");
		// beanConfig.setBasePath("/bms/rest");

		// beanConfig.setHost("54.89.252.35");

		beanConfig.setBasePath(APP_BASE_PATH);

		beanConfig.setResourcePackage("com.example.easy");
		beanConfig.setPrettyPrint(true);
		beanConfig.setScan(true);

	}

}