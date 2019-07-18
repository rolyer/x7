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

import java.util.ArrayList;
import java.util.List;

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


    @Bean
    public SchemaTransformCustomizer schemaTransformCustomizer(){

        SchemaTransformCustomizer schemaTransformCustomizer = new SchemaTransformCustomizer() {
            @Override
            public List<Class<? extends BaseRepository>> customize(SchemaTransformRepositoryBuilder builder) {

                List<Class<? extends BaseRepository>> list = new ArrayList<>();
                Class<? extends BaseRepository> clzz = builder.build(PigRepository.class);
                list.add(clzz);

                return list;
            }
        };

        return schemaTransformCustomizer;
    }
}