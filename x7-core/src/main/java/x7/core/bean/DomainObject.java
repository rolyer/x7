package x7.core.bean;

import java.io.Serializable;
import java.util.List;

public class DomainObject<T,WITH> implements Serializable {

    private static final long serialVersionUID = -1601773516153576783L;

    private T main;
    private List<WITH> withList;


    public T getMain() {
        return main;
    }

    public void setMain(T main) {
        this.main = main;
    }

    public List<WITH> getWithList() {
        return withList;
    }

    public void setWithList(List<WITH> withList) {
        this.withList = withList;
    }

    @Override
    public String toString() {
        return "DomainObject{" +
                "main=" + main +
                ", withList=" + withList +
                '}';
    }
}
