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
package x7.repository.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x7.core.bean.*;
import x7.core.repository.X;
import x7.core.web.Page;
import x7.repository.KeyOne;
import x7.repository.exception.PersistenceException;
import x7.repository.exception.RollbackException;
import x7.repository.mapper.Mapper;
import x7.repository.mapper.MapperFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * @author Sim
 */
public class DaoTemplate {


    private final static Logger logger = LoggerFactory.getLogger(Dao.class);




    /**
     * 放回连接池,<br>
     * 连接池已经重写了关闭连接的方法
     */
    private static void close(Connection conn) {
        RcDataSourceUtil.releaseConnection(conn);
    }

    private static void close(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public long execute(String sql,  DaoBuildable daoBuildable) {

        Connection conn = null;
        try {
            conn = DataSourceUtil.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("NO CONNECTION");
        }

        long id = 0;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);

            daoBuildable.buildStatement( pstmt);

            pstmt.execute();

            id = daoBuildable.buildId(pstmt);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RollbackException("RollbackException: " + e.getMessage());
        } finally {
            close(pstmt);
            DataSourceUtil.releaseConnection(conn);
        }


        return id;
    }



    public boolean executeUpdate(String sql, DaoBuildable daoBuildable) {

        Connection conn = null;
        try {
            conn = DataSourceUtil.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("NO CONNECTION");
        }

        boolean flag;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);

            daoBuildable.buildStatement(pstmt);

            flag = pstmt.executeUpdate() == 0 ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RollbackException("RollbackException: " + e.getMessage());
        } finally {
            close(pstmt);
            DataSourceUtil.releaseConnection(conn);
        }


        return flag;
    }

    private <T> boolean testRemove(KeyOne<T> keyOne) {

        Class clz = keyOne.getClzz();

        Parsed parsed = Parser.get(clz);

        int i = 1;

        Field keyOneField = parsed.getKeyField(X.KEY_ONE);
        if (Objects.isNull(keyOneField))
            throw new PersistenceException("No setting of PrimaryKey by @X.Key");

        String sql = MapperFactory.getSql(clz, Mapper.REMOVE);

        return executeUpdate(sql,
                new DaoBuildable() {
                    @Override
                    public void buildStatement(PreparedStatement pstmt) {
                        SqlUtil.adpterSqlKey(pstmt, keyOneField, keyOne.get(), i);
                    }

                    @Override
                    public long buildId(PreparedStatement pstmt) {
                        return 0;
                    }

                    @Override
                    public Page<Map<String, Object>> buildResultMapPage(ResultSet resultSet) {
                        return null;
                    }

                    @Override
                    public List<Map<String, Object>> buildResultMapList(ResultSet resultSet) {
                        return null;
                    }

                    @Override
                    public <T> Page<T> buildPage(ResultSet resultSet) {
                        return null;
                    }

                    @Override
                    public <T> List<T> buildList(ResultSet resultSet) {
                        return null;
                    }
                }
        );

    }


}