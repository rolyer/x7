package io.xream.x7.demo;

import x7.core.repository.X;

import java.util.Date;

public class TimeJack {

    @X.Key
    private long id;
    private String name;
    private Date date;
    private String testException;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getTestException() {
        return testException;
    }

    public void setTestException(String testException) {
        this.testException = testException;
    }

    @Override
    public String toString() {
        return "TimeJack{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", testException='" + testException + '\'' +
                '}';
    }
}
