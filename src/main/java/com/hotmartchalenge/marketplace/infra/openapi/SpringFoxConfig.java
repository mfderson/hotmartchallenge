package com.hotmartchalenge.marketplace.infra.openapi;

import com.fasterxml.classmate.TypeResolver;
import com.hotmartchalenge.marketplace.api.exceptionHandlers.ErrorMessage;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig implements WebMvcConfigurer {

  @Bean
  public Docket apiDocket() {
    var typeResolver = new TypeResolver();

    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.hotmartchalenge.marketplace.api"))
        .build()
        .useDefaultResponseMessages(false)
        .apiInfo(apiInfo())
        .globalResponseMessage(RequestMethod.GET, globalGetResponseMessages())
        .globalResponseMessage(RequestMethod.POST, globalPostPutResponseMessages())
        .globalResponseMessage(RequestMethod.PUT, globalPostPutResponseMessages())
        .globalResponseMessage(RequestMethod.DELETE, globalDeleteResponseMessages())
        .additionalModels(typeResolver.resolve(ErrorMessage.class))
        .tags(new Tag("Products", "Manage products"));
  }

  private List<ResponseMessage> globalGetResponseMessages() {
    return Arrays.asList(
        new ResponseMessageBuilder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("Internal server error")
            .responseModel(new ModelRef("ErrorMessage"))
            .build(),
        new ResponseMessageBuilder().code(HttpStatus.OK.value()).message("Success").build());
  }

  private List<ResponseMessage> globalPostPutResponseMessages() {
    return Arrays.asList(
        new ResponseMessageBuilder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message("Invalid request (client error)")
            .responseModel(new ModelRef("ErrorMessage"))
            .build(),
        new ResponseMessageBuilder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("Internal server error")
            .responseModel(new ModelRef("ErrorMessage"))
            .build(),
        new ResponseMessageBuilder()
            .code(HttpStatus.NOT_ACCEPTABLE.value())
            .message("Method not supported")
            .build(),
        new ResponseMessageBuilder()
            .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
            .message("Format body isn't supported")
            .build());
  }

  private List<ResponseMessage> globalDeleteResponseMessages() {
    return Arrays.asList(
        new ResponseMessageBuilder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message("Invalid request (client error)")
            .responseModel(new ModelRef("ErrorMessage"))
            .build(),
        new ResponseMessageBuilder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("Internal server error")
            .responseModel(new ModelRef("ErrorMessage"))
            .build());
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Hotmart Challenge")
        .description("API para processo seletivo")
        .version("1")
        .contact(new Contact("Anderson", "https://test.test.test", "test@test.test"))
        .build();
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");

    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}
