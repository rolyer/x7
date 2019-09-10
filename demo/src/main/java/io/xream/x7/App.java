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
@EnableX7L2Caching(timeSeconds = 120)
@EnableX7Repository(mappingPrefix = "t_",mappingSpec = "_")
@EnableReyClient
@EnableTracingServlet
@EnableCorsConfig
@EnableDateToLongForJackson
@EnableSchemaTransform
//@EnableX7L3Caching
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
//            public List<Class<? extends BaseRepository>> customizer(SchemaTransformRepositoryBuilder builder) {
//
//                List<Class<? extends BaseRepository>> list = new ArrayList<>();
//                Class<? extends BaseRepository> clzz = builder.build(PigRepository.class);
//                list.add(clzz);
//
//                return list;
//            }
//        };
//
//        return schemaTransformCustomizer;
//    }

//    @Bean
//    public IdGeneratorPolicyCustomizer idGeneratorPolicyCustomizer(){
//
//        return new IdGeneratorPolicyCustomizer() {
//            @Override
//            public IdGeneratorPolicy customizer() {
//
//                IdGeneratorPolicy idGeneratorPolicy = null;
//
//                return idGeneratorPolicy;
//            }
//        };
//    }


}