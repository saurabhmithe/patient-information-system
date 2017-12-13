package projects.distributed.happypatients.springboot.utilities;

import java.util.Calendar;
import java.util.Date;

public class GeneralUtilities {
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        //Set time in milliseconds
        c.setTime(date);
        int mYear = c.get(Calendar.YEAR);
        return mYear;
    }
}
