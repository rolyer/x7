/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package x7;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import x7.core.bean.BeanElement;
import x7.core.bean.Parsed;
import x7.core.bean.Parser;
import x7.core.bean.TransformConfigurable;
import x7.core.repository.CacheResolver;
import x7.repository.BaseRepository;
import x7.repository.DataRepository;
import x7.repository.Repository;
import x7.repository.RepositoryBooter;
import x7.repository.cache.CacheStoragePolicy;
import x7.repository.cache.LevelTwoCacheResolver;
import x7.repository.cache.customizer.CacheStoragePolicyCustomizer;
import x7.repository.id.IdGeneratorPolicy;
import x7.repository.id.customizer.IdGeneratorPolicyCustomizer;
import x7.repository.mapper.MapperFactory;
import x7.repository.schema.SchemaConfig;
import x7.repository.schema.SchemaTransformRepository;
import x7.repository.schema.customizer.SchemaTransformCustomizer;
import x7.repository.schema.customizer.SchemaTransformRepositoryBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RepositoryListener implements
        ApplicationListener<ApplicationStartedEvent> {


    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        if (!X7Data.isEnabled)
            return;

        customizeCacheStoragePolicy(applicationStartedEvent);

        customizeIdGeneratorPolicy(applicationStartedEvent);

        RepositoryBooter.onStarted();

        transform(applicationStartedEvent);
    }


    private void customizeCacheStoragePolicy(ApplicationStartedEvent applicationStartedEvent) {

        CacheStoragePolicyCustomizer customizer = null;
        try {
            customizer = applicationStartedEvent.getApplicationContext().getBean(CacheStoragePolicyCustomizer.class);
        } catch (Exception e) {

        }

        if (customizer == null)
            return;

        CacheStoragePolicy cacheStoragePolicy = customizer.customize();
        if (cacheStoragePolicy == null)
            return;

        CacheResolver levelTwoCacheResolver = applicationStartedEvent.getApplicationContext().getBean(CacheResolver.class);
        if (levelTwoCacheResolver == null)
            return;
        ((LevelTwoCacheResolver)levelTwoCacheResolver).setCacheStoragePolicy(cacheStoragePolicy);

    }


    private void customizeIdGeneratorPolicy(ApplicationStartedEvent applicationStartedEvent) {
        IdGeneratorPolicyCustomizer customizer = null;
        try {
            customizer = applicationStartedEvent.getApplicationContext().getBean(IdGeneratorPolicyCustomizer.class);
        } catch (Exception e) {

        }

        if (customizer == null)
            return;

        IdGeneratorPolicy idGeneratorPolicy = customizer.customize();
        if (idGeneratorPolicy == null)
            return;

        DataRepository dataRepository = applicationStartedEvent.getApplicationContext().getBean(DataRepository.class);
        if (dataRepository == null)
            return;
        dataRepository.setIdGeneratorPolicy(idGeneratorPolicy);

    }


    private void transform(ApplicationStartedEvent applicationStartedEvent) {
        List<Class<? extends BaseRepository>> clzzList = null;
        if (SchemaConfig.isSchemaTransformEnabled) {
            clzzList = customizeSchemaTransform(applicationStartedEvent);
        }

        if (clzzList != null) {

            for (Class<? extends BaseRepository> clzz : clzzList) {

                DataRepository dataRepository = (DataRepository) applicationStartedEvent.getApplicationContext().getBean(Repository.class);

                List list = list(dataRepository, clzz);//查出所有配置
                if (!list.isEmpty()) {
                    reparse(list);
                }
            }
        }
    }


    private List<Class<? extends BaseRepository>> customizeSchemaTransform(ApplicationStartedEvent applicationStartedEvent) {


        SchemaTransformCustomizer customizer = null;
        try {
            customizer = applicationStartedEvent.getApplicationContext().getBean(SchemaTransformCustomizer.class);
        } catch (Exception e) {
        }

        if (customizer != null) {
            SchemaTransformRepositoryBuilder builder = new SchemaTransformRepositoryBuilder();
            return customizer.customize(builder);
        }

        SchemaTransformRepositoryBuilder.registry = null;

        List<Class<? extends BaseRepository>> list = new ArrayList<>();
        list.add(SchemaTransformRepository.class);
        return list;
    }


    private void reparse(List list) {

        //key: originTable
        Map<String, List<TransformConfigurable>> map = new HashMap<>();

        for (Object obj : list) {
            if (obj instanceof TransformConfigurable) {

                TransformConfigurable transformed = (TransformConfigurable) obj;
                String originTable = transformed.getOriginTable();
                List<TransformConfigurable> transformedList = map.get(originTable);
                if (transformedList == null) {
                    transformedList = new ArrayList<>();
                    map.put(originTable, transformedList);
                }
                transformedList.add(transformed);
            }
        }

        for (Map.Entry<String, List<TransformConfigurable>> entry : map.entrySet()) {
            String originTable = entry.getKey();

            Parsed parsed = Parser.getByTableName(originTable);
            if (parsed == null)
                continue;

            List<TransformConfigurable> transformedList = entry.getValue();
            for (TransformConfigurable transformed : transformedList) {
                parsed.setTableName(transformed.getTargetTable());//FIXME 直接替换了原始的表
                parsed.setTransforemedAlia(transformed.getAlia());

                for (BeanElement be : parsed.getBeanElementList()) {
                    if (be.getMapper().equals(transformed.getOriginColumn())) {
                        be.mapper = transformed.getTargetColumn();//FIXME 直接替换了原始的列, 只需要目标对象的属性有值
                        break;
                    }
                }
            }

            parsed.reset(parsed.getBeanElementList());
            String tableName = parsed.getTableName();
            Parsed parsedTransformed = Parser.getByTableName(tableName);
            parsed.setParsedTransformed(parsedTransformed);

            SchemaConfig.transformableSet.add(parsed.getClz());

            Map<String, String> sqlMap = MapperFactory.getSqlMap(parsedTransformed.getClz());
            MapperFactory.putSqlMap(parsed.getClz(), sqlMap);
        }
    }

    private List list(DataRepository dataRepository, Class<? extends BaseRepository> clzz) {

        Type[] types = clzz.getGenericInterfaces();

        ParameterizedType parameterized = (ParameterizedType) types[0];
        Class clazz = (Class) parameterized.getActualTypeArguments()[0];

        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        List list = dataRepository.list(obj);

        return list;
    }

}
