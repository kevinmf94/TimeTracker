package cat.uab.ds.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat FORMAT =
            new SimpleDateFormat("dd/MM/YYYY HH:mm:ss",
                    new Locale("en"));

    public static String dateToStr(Date date){
        return FORMAT.format(date);
    }

}
