package org.solution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;

/**
 * Products API main Application
 *
 */

@SpringBootApplication
public class Application 
{
    public static void main( String[] args ) throws Exception
    {
    	System.setProperty("server.port", "8014");
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public CacheManager cacheManager() {
    	GuavaCacheManager cacheManager = new GuavaCacheManager("products");
    	return cacheManager;
    }
}
