package x7.core.exception;

import java.io.Serializable;
import java.util.Date;

/**
 * {"timestamp":"2019-03-05T09:30:04.312+0000",
 * "status":500,"error":"Internal Server Error","
 * message":"Request processing failed; nested exception is java.lang.RuntimeException: test",
 * "path":"/xxx/reyc/test"}
 */
public class ExceptionBean implements Serializable {

    private static final long serialVersionUID = -4827767279834747124L;
    private int status;
    private String path;
    private String message;
    private Date timestamp;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ExceptionBean{" +
                "status=" + status +
                ", path='" + path + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
