package models;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by keen on 10/27/14.
 */
public class PolishHolidays {
    public static Calendar wielkanoc(int year){
        int a,b,c,d,e,f,g,h,i,k,L,m,n,p,y=year;
        a = year % 19;
        b = year/100;
        c = year % 100;
        d = b/4;
        e = b % 4;
        f = (b + 8)/25;
        g = (b - f + 1)/3;
        h = (19 * a+ b - d - g + 15) % 30;
        i = c / 4;
        k = c % 4;
        L = (32 + 2 * e + 2 * i - h - k) % 7;
        m = (a + 11 * h + 22*L) / 451;
        n = (h + L - 7 * m + 114)/ 31;
        p = (h + L - 7 * m + 114) % 31;
        return new GregorianCalendar(y,n,p+1);
    }
}
