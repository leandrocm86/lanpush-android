package lcm.lanpush.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Data {
    public static String agora() {
        return new SimpleDateFormat("dd/MM HH:mm:ss").format(new Date());
//        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public static long timestampProximaManha(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 6);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (!madrugada())
            cal.add(Calendar.DATE, 1);
        return cal.getTime().getTime();
    }

    public static boolean madrugada() {
        int hora = getHora();
        return hora >= 0 && hora < 6;
    }

    public static int getHora() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static String formataTempo(long milisegundos) {
        if (milisegundos <= 0)
            return new String();
        StringBuilder retorno = new StringBuilder();
        if (milisegundos >= Data.SEGUNDO) {
            if (milisegundos >= Data.MINUTO) {
                if (milisegundos >= Data.HORA) {
                    if (milisegundos >= Data.DIA)
                        retorno.append(milisegundos/Data.DIA).append("d").append(formataTempo(milisegundos % Data.DIA));
                    else
                        retorno.append(milisegundos/Data.HORA).append("h").append(formataTempo(milisegundos % Data.HORA));
                }
                else
                    retorno.append(milisegundos/Data.MINUTO).append("m").append(formataTempo(milisegundos % Data.MINUTO));
            }
            else
                retorno.append(milisegundos/Data.SEGUNDO).append("s").append(formataTempo(milisegundos % Data.SEGUNDO));
        }
        else {
            retorno.append(milisegundos).append("ms");
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
