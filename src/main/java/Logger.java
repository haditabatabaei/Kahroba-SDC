import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public Logger(){}

    public String getLog(String rawToLog){
        StringBuilder logBuilder = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        logBuilder.append(dateFormat.format(date) + " => " + rawToLog);
        return logBuilder.toString();
    }

    public void printLog(String rawToLog){
        System.out.println(getLog(rawToLog));

    }
}
