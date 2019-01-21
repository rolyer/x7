package x7.core.bean;


public class Reduce {
    private ReduceType type;
    private String property;

    public ReduceType getType() {
        return type;
    }
    public void setType(ReduceType type) {
        this.type = type;
    }
    public String getProperty() {
        return property;
    }
    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "Reduce{" +
                "type=" + type +
                ", property='" + property + '\'' +
                '}';
    }

    public enum ReduceType {

        SUM,
        COUNT,
        MAX,
        MIN,
        AVG
    }
}
