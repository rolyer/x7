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
package x7.core.bean.condition;

import x7.core.bean.CriteriaBuilder;
import x7.core.bean.CriteriaCondition;
import x7.core.bean.Reduce;

import java.util.Objects;


public class ReduceCondition {

    private Reduce.ReduceType type;
    private String reduceProperty;
    private CriteriaCondition condition;

    private transient Class clz;
    private transient CriteriaBuilder builder;

    public Reduce.ReduceType getType() {
        return type;
    }

    public void setType(Reduce.ReduceType type) {
        this.type = type;
    }

    public String getReduceProperty() {
        return reduceProperty;
    }

    public void setReduceProperty(String reduceProperty) {
        this.reduceProperty = reduceProperty;
    }

    public CriteriaCondition getCondition() {
        if (Objects.nonNull(this.builder)) {
            this.condition = builder.get();
        }
        return this.condition;
    }

    public void setCondition(CriteriaCondition condition) {
        this.condition = condition;
    }

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }

    public CriteriaBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(CriteriaBuilder builder) {
        this.builder = builder;
    }

    public ReduceCondition(){
    }

    public ReduceCondition(Reduce.ReduceType type,String reduceProperty){
        this.type= type;
        this.reduceProperty = reduceProperty;
        CriteriaBuilder builder = CriteriaBuilder.buildCondition();
        this.builder = builder;
    }

    public CriteriaBuilder.ConditionBuilder  and(){
        return this.builder.and();
    }

    @Override
    public String toString() {
        return "ReduceCondition{" +
                "type=" + type +
                ", reduceProperty='" + reduceProperty + '\'' +
                ", condition=" + condition +
                '}';
    }
}