package x7.core.bean;

import x7.core.web.Direction;

import java.io.Serializable;

public class Sort implements Serializable {
    private static final long serialVersionUID = 7492946384236689679L;

    private Direction direction = Direction.DESC;
    private String orderBy;

    public Sort(){}

    public Sort(String orderBy, Direction direction){
        this.orderBy = orderBy;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public String toString() {
        return "Sort{" +
                "direction=" + direction +
                ", orderBy='" + orderBy + '\'' +
                '}';
    }
}
