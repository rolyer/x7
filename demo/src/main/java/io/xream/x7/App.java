package io.xream.x7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import x7.*;

/**
 *
 * Demo
 *
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableTransactionManagementReadable
@EnableX7L2Cache(timeSeconds = 120)
@EnableX7Repository(mappingPrefix = "t_",mappingSpec = "_")
@EnableReyClient
@EnableTracingServlet
@EnableCorsConfig
@EnableDateToLongForJackson
public class App {
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class);
    }
}