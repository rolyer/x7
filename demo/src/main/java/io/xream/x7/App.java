package io.xream.x7;

import io.xream.x7.demo.PigRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import x7.*;
import x7.repository.BaseRepository;
import x7.repository.schema.customizer.SchemaTransformCustomizer;
import x7.repository.schema.customizer.SchemaTransformRepositoryBuilder;

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
@EnableSchemaTransform
public class App {
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class);
    }


//    @Bean
//    public SchemaTransformCustomizer schemaTransformCustomizer(){
//
//        SchemaTransformCustomizer schemaTransformCustomizer = new SchemaTransformCustomizer() {
//            @Override
//            public Class<? extends BaseRepository> customize(SchemaTransformRepositoryBuilder builder) {
//                return builder.build(PigRepository.class);
//            }
//        };
//
//        return schemaTransformCustomizer;
//    }
}