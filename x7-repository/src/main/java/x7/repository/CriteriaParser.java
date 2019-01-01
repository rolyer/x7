package x7.repository;

import x7.core.bean.Criteria;
import x7.core.bean.CriteriaCondition;
import x7.repository.mapper.Mapper;

public interface CriteriaParser {

    void setDialect(Mapper.Dialect dialect);

    String parseCondition(CriteriaCondition criteriaCondition) ;

    String[] parse(Criteria criteria) ;

}
