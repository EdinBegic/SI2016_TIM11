package com.timxyz;

import org.resthub.web.springmvc.router.RouterConfigurationSupport;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.timxyz.controllers")
public class WebAppConfig extends RouterConfigurationSupport {
    @Override
    public List<String> listRouteFiles() {
        List<String> routeFiles = new ArrayList<String>();

        routeFiles.add("/" + WebAppConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath()  + "routes.conf");

        return routeFiles;
    }
}