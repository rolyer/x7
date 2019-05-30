package io.xream.x7.demo.bean;

import x7.core.bean.Transformed;
import x7.core.repository.X;

import java.util.Date;

public class Pig implements Transformed {

    @X.Key
    private long id;
    private long catId;
    private String name;
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCatId() {
        return catId;
    }

    public void setCatId(long catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Pig{" +
                "id=" + id +
                ", catId=" + catId +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public String getAlia() {
        return null;
    }

    @Override
    public String getOriginTable() {
        return null;
    }

    @Override
    public String getTargetTable() {
        return null;
    }

    @Override
    public String getOriginColumn() {
        return null;
    }

    @Override
    public String getTargetColumn() {
        return null;
    }
}
