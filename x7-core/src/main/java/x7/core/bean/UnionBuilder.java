package x7.core.bean;

import x7.core.util.BeanUtilX;
import x7.core.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import x7.core.bean.Criteria.X;

public class UnionBuilder{

    private Criteria.Union union;
    protected Criteria.Union getUnion(){
        return this.union;
    }

    private Criteria criteria;

    private  UnionBuilder instance;

    protected UnionBuilder(Criteria criteria){
        instance = this;
        this.union = new Criteria.Union();
        this.criteria = criteria;
    }


    public UnionBuilder.ConditionBuilder and() {

        X x = new X();
        x.setConjunction(Conjunction.AND);
        x.setValue(Conjunction.AND);

        X current = conditionBuilder.getX();
        if (current != null) {
            X parent = current.getParent();
            if (parent != null) {
                List<X> subList = parent.getSubList();
                if (subList != null) {
                    subList.add(x);
                    x.setParent(parent);
                }
            } else {
                this.union.add(x);
            }
        } else {
            this.union.add(x);
        }

        conditionBuilder.under(x);

        return conditionBuilder;
    }

    public UnionBuilder.ConditionBuilder or() {

        X x = new X();
        x.setConjunction(Conjunction.OR);
        x.setValue(Conjunction.OR);

        X current = conditionBuilder.getX();
        if (current != null) {
            X parent = current.getParent();
            if (parent != null) {
                List<X> subList = parent.getSubList();
                if (subList != null) {
                    subList.add(x);
                    x.setParent(parent);
                }
            } else {
                this.union.add(x);
            }
        } else {
            this.union.add(x);
        }

        conditionBuilder.under(x);

        return conditionBuilder;
    }

    public UnionBuilder endSub() {

        X x = new X();
        x.setPredicate(Predicate.SUB_END);
        x.setValue(Predicate.SUB_END);

        X current = conditionBuilder.getX();
        X parent = current.getParent();
        if (parent != null) {
            List<X> subList = parent.getSubList();
            if (subList != null) {
                subList.add(x);
            }

            this.conditionBuilder.under(parent);
        }

        return instance;
    }


    public interface ConditionBuilder {

        UnionBuilder eq(String property, Object value);

        UnionBuilder lt(String property, Object value);

        UnionBuilder lte(String property, Object value);

        UnionBuilder gt(String property, Object value);

        UnionBuilder gte(String property, Object value);

        UnionBuilder ne(String property, Object value);

        UnionBuilder like(String property, String value);

        UnionBuilder likeRight(String property, String value);

        UnionBuilder notLike(String property, String value);

        UnionBuilder between(String property, Object min, Object max);

        UnionBuilder in(String property, List<? extends Object> list);

        UnionBuilder nin(String property, List<Object> list);

        UnionBuilder nonNull(String property);

        UnionBuilder isNull(String property);

        UnionBuilder x(String sql);

        UnionBuilder x(String sql, List<Object> valueList);

        void under(Criteria.X x);

        Criteria.X getX();

        UnionBuilder.ConditionBuilder beginSub();

    }

    private UnionBuilder.ConditionBuilder conditionBuilder = new UnionBuilder.ConditionBuilder() {

        private Criteria.X x = null;

        @Override
        public Criteria.X getX() {
            return x;
        }

        @Override
        public void under(Criteria.X x) {
            this.x = x;
        }

        @Override
        public UnionBuilder eq(String property, Object value) {

            if (value == null)
                return instance;
            if (Objects.nonNull(criteria.getParsed())) {
                if (BeanUtilX.isBaseType_0(property, value,criteria.getParsed()))
                    return instance;
            }
            if (CriteriaBuilder.isNullOrEmpty(value))
                return instance;

            x.setPredicate(Predicate.EQ);
            x.setKey(property);
            x.setValue(value);

            return instance;
        }

        @Override
        public UnionBuilder lt(String property, Object value) {

            if (value == null)
                return instance;
            if (BeanUtilX.isBaseType_0(property, value,criteria.getParsed()))
                return instance;
            if (CriteriaBuilder.isNullOrEmpty(value))
                return instance;

            x.setPredicate(Predicate.LT);
            x.setKey(property);
            x.setValue(value);

            return instance;
        }

        @Override
        public UnionBuilder lte(String property, Object value) {

            if (value == null)
                return instance;

            if (BeanUtilX.isBaseType_0(property, value,criteria.getParsed()))
                return instance;
            if (CriteriaBuilder.isNullOrEmpty(value))
                return instance;

            x.setPredicate(Predicate.LTE);
            x.setKey(property);
            x.setValue(value);

            return instance;
        }

        @Override
        public UnionBuilder gt(String property, Object value) {

            if (value == null)
                return instance;
            if (BeanUtilX.isBaseType_0(property, value,criteria.getParsed()))
                return instance;
            if (CriteriaBuilder.isNullOrEmpty(value))
                return instance;

            x.setPredicate(Predicate.GT);
            x.setKey(property);
            x.setValue(value);

            return instance;
        }

        @Override
        public UnionBuilder gte(String property, Object value) {

            if (value == null)
                return instance;

            if (BeanUtilX.isBaseType_0(property, value,criteria.getParsed()))
                return instance;
            if (CriteriaBuilder.isNullOrEmpty(value))
                return instance;

            x.setPredicate(Predicate.GTE);
            x.setKey(property);
            x.setValue(value);

            return instance;
        }

        @Override
        public UnionBuilder ne(String property, Object value) {

            if (value == null)
                return instance;
            if (BeanUtilX.isBaseType_0(property, value,criteria.getParsed()))
                return instance;
            if (CriteriaBuilder.isNullOrEmpty(value))
                return instance;

            x.setPredicate(Predicate.NE);
            x.setKey(property);
            x.setValue(value);

            return instance;
        }

        @Override
        public UnionBuilder like(String property, String value) {

            if (StringUtil.isNullOrEmpty(value))
                return instance;

            x.setPredicate(Predicate.LIKE);
            x.setKey(property);
            x.setValue(SqlScript.LIKE_HOLDER + value + SqlScript.LIKE_HOLDER);

            return instance;
        }

        @Override
        public UnionBuilder likeRight(String property, String value) {

            if (StringUtil.isNullOrEmpty(value))
                return instance;

            x.setPredicate(Predicate.LIKE);
            x.setKey(property);
            x.setValue(value + SqlScript.LIKE_HOLDER);

            return instance;
        }

        @Override
        public UnionBuilder notLike(String property, String value) {

            if (StringUtil.isNullOrEmpty(value))
                return instance;

            x.setPredicate(Predicate.NOT_LIKE);
            x.setKey(property);
            x.setValue(SqlScript.LIKE_HOLDER + value + SqlScript.LIKE_HOLDER);

            return instance;
        }

        @Override
        public UnionBuilder between(String property, Object min, Object max) {

            if (min == null || max == null)
                return instance;

            if (BeanUtilX.isBaseType_0(property, max,criteria.getParsed()))
                return instance;
            if (CriteriaBuilder.isNullOrEmpty(max))
                return instance;
            if (CriteriaBuilder.isNullOrEmpty(min))
                return instance;

            MinMax minMax = new MinMax();
            minMax.setMin(min);
            minMax.setMax(max);

            x.setPredicate(Predicate.BETWEEN);
            x.setKey(property);
            x.setValue(minMax);

            return instance;
        }

        @Override
        public UnionBuilder in(String property, List<? extends Object> list) {

            if (list == null || list.isEmpty())
                return instance;

            List<Object> tempList = new ArrayList<Object>();
            for (Object obj : list) {
                if (Objects.isNull(obj))
                    continue;
                if (!tempList.contains(obj)) {
                    tempList.add(obj);
                }
            }

            if (tempList.isEmpty())
                return instance;

            if (tempList.size() == 1) {
                return eq(property, tempList.get(0));
            }

            x.setPredicate(Predicate.IN);
            x.setKey(property);
            x.setValue(tempList);

            return instance;
        }

        @Override
        public UnionBuilder nin(String property, List<Object> list) {

            if (list == null || list.isEmpty())
                return instance;

            List<Object> tempList = new ArrayList<Object>();
            for (Object obj : list) {
                if (Objects.isNull(obj))
                    continue;
                if (!tempList.contains(obj)) {
                    tempList.add(obj);
                }
            }

            if (tempList.isEmpty())
                return instance;

            if (tempList.size() == 1) {
                return ne(property, tempList.get(0));
            }

            x.setPredicate(Predicate.NOT_IN);
            x.setKey(property);
            x.setValue(tempList);

            return instance;
        }

        @Override
        public UnionBuilder nonNull(String property) {

            if (StringUtil.isNullOrEmpty(property))
                return instance;

            x.setPredicate(Predicate.IS_NOT_NULL);
            x.setValue(property);

            return instance;
        }

        @Override
        public UnionBuilder isNull(String property) {

            if (StringUtil.isNullOrEmpty(property))
                return instance;

            x.setPredicate(Predicate.IS_NULL);
            x.setValue(property);

            return instance;
        }

        @Override
        public UnionBuilder x(String sql) {

            if (StringUtil.isNullOrEmpty(sql))
                return instance;

            sql = BeanUtilX.normalizeSql(sql);

            x.setPredicate(Predicate.X);
            x.setKey(sql);
            x.setValue(null);

            return instance;
        }

        @Override
        public UnionBuilder x(String sql, List<Object> valueList) {

            if (StringUtil.isNullOrEmpty(sql))
                return instance;

            sql = BeanUtilX.normalizeSql(sql);

            x.setPredicate(Predicate.X);
            x.setKey(sql);
            x.setValue(valueList);

            return instance;
        }

        @Override
        public UnionBuilder.ConditionBuilder beginSub() {

            x.setKey(Predicate.SUB.sql());// special treat FIXME
            x.setValue(Predicate.SUB);

            List<Criteria.X> subList = new ArrayList<>();
            x.setSubList(subList);

            Criteria.X from = new Criteria.X();
            from.setPredicate(Predicate.SUB_BEGIN);
            from.setValue(Predicate.SUB_BEGIN);

            subList.add(from);

            Criteria.X xx = new Criteria.X();//?
            subList.add(xx);//?
            xx.setParent(x);
            conditionBuilder.under(xx);

            return conditionBuilder;
        }

    };


}
