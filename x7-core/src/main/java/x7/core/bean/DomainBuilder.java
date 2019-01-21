package x7.core.bean;

import java.util.List;

public interface DomainBuilder {
    DomainBuilder known(List<? extends Object> mainIdList);
    DomainBuilder relative(Class relativeClz);
    DomainBuilder on(String mainProperty);
    CriteriaBuilder with(String withProperty);
}
