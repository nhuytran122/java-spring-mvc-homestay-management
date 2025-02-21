package com.lullabyhomestay.homestay_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(JstlView.class);
        bean.setPrefix("/WEB-INF/view/");
        bean.setSuffix(".jsp");
        return bean;
    }

    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(viewResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/admin/css/**").addResourceLocations("/resources/admin/css/");
        registry.addResourceHandler("/admin/fonts/**").addResourceLocations("/resources/admin/fonts/");
        registry.addResourceHandler("/admin/js/**").addResourceLocations("/resources/admin/js/");
        registry.addResourceHandler("/admin/images/**").addResourceLocations("/resources/admin/images/");
        registry.addResourceHandler("/client/**").addResourceLocations("/resources/client/");
        registry.addResourceHandler("/images/**").addResourceLocations("/resources/images/");
    }

}