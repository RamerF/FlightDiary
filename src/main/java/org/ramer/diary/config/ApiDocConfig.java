package org.ramer.diary.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by RAMER on 10/28/2017.
 */
@Configuration
@EnableSwagger2
public class ApiDocConfig{
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("org.ramer.diary.controller")).paths(PathSelectors.any())
                .build();
    }

    private ApiInfo getApiInfo() {
        Contact contact = new Contact("Tang Xiaofeng", "http://www.ramer.cn", "feng1390635973@gmail.com");
        return new ApiInfoBuilder().title("Diary API")
                .description("一个简约风格的图片社交网站.使用技术:springmvc+spring+springdata+hibernate+jpa+mysql+jsp+jquery")
                .version("1.0.0").license("Apache 2.0").licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .contact(contact).build();
    }
}
