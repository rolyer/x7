package x7.core.util;

public class ExceptionUtil {

    public static String getMessage(Exception e){
        String msg = "";
        StackTraceElement[] eleArr = e.getStackTrace();
        int length = eleArr.length;
        if (eleArr != null && length > 0){
            msg = eleArr[0].toString();
            if (length > 1){
                msg += "\n";
                msg += eleArr[1].toString();
            }
        }

        return msg;
    }

    public static String getMessage(Throwable e){
        String msg = "";
        StackTraceElement[] eleArr = e.getStackTrace();
        int length = eleArr.length;
        if (eleArr != null && length > 0){
            msg = eleArr[0].toString();
            if (length > 1){
                msg += "\n";
                msg += eleArr[1].toString();
            }
        }

        return msg;
    }
}
