package lcm.lanpush.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dates {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMdd HH:mm:ss");

    private Dates() {}

    public static String now() {
        return dateFormat.format(new Date());
//        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public static long timestampNextMorning(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 6);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (!isSleepTime())
            cal.add(Calendar.DATE, 1);
        return cal.getTime().getTime();
    }

    public static boolean isSleepTime() {
        int hora = getHour();
        return hora >= 0 && hora < 6;
    }

    public static int getHour() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static String formatTime(long millis) {
        if (millis <= 0)
            return new String();
        StringBuilder retorno = new StringBuilder();
        if (millis >= Dates.SEGUNDO) {
            if (millis >= Dates.MINUTO) {
                if (millis >= Dates.HORA) {
                    if (millis >= Dates.DIA)
                        retorno.append(millis/ Dates.DIA).append("d").append(formatTime(millis % Dates.DIA));
                    else
                        retorno.append(millis/ Dates.HORA).append("h").append(formatTime(millis % Dates.HORA));
                }
                else
                    retorno.append(millis/ Dates.MINUTO).append("m").append(formatTime(millis % Dates.MINUTO));
            }
             else retorno.append(millis/ Dates.SEGUNDO).append("s").append(formatTime(millis % Dates.SEGUNDO));
        }
        else {
            //retorno.append(millis).append("ms");
        }
        return retorno.toString();
    }

    /**
     * 1 Dia em milisegundos.
     */
    public static final int DIA = 86400000;

    /**
     * 1 Hora em milisegundos.
     */
    public static final int HORA = 3600000;
    /**
     * 1 Minuto em milisegundos.
     */
    public static final int MINUTO = 60000;
    /**
     * 1 Segundo em milisegundos.
     */
    public static final int SEGUNDO = 1000;

}
