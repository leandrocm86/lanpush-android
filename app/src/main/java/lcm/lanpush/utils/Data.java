package lcm.lanpush.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {
    public static String agora() {
        return new SimpleDateFormat("dd/MM HH:mm:ss,SSS").format(new Date());
//        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
