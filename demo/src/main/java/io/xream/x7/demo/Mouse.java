package io.xream.x7.demo;

import x7.core.repository.X;

public class Mouse {

    @X.Key
    private long id;
    private String test;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "Mouse{" +
                "id=" + id +
                ", test='" + test + '\'' +
                '}';
    }
}
