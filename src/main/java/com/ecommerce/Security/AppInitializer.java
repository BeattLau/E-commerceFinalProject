package com.ecommerce.Security;


import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppConfig.class};
    }
    @Override
    protected Class<?>[] getServletConfigClasses() {
        Class[] configFiles = {AppConfig.class};
        return configFiles;
    }
    @Override
    protected String[] getServletMappings() {
        String[] mappings = {"/"};
        return mappings;
    }
}
