package x7.core.bean;

import x7.core.web.Direction;

import java.util.List;

public interface PageBuilder {

    PageBuilder scroll(boolean isScroll);
    PageBuilder rows(int rows);
    PageBuilder page(int page);
    PageBuilder orderIn(String property, List<? extends Object> inList);
    PageBuilder orderBy(String property);
    void on(Direction direction);
}
