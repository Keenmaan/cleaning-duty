package controllers;

import models.Date;
import models.Holiday;
import models.Worker;
import play.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by keen on 10/25/14.
 */
public class RosterCalendar {

    public static String getClassColor(int i){
        String workDay="info";
        String currentDay="warning";
        String weekend="success";
        String holiday="danger";
        String old="action";
        i=transform(i);
        if(checkDay(i)){
            return currentDay;
        }
        Calendar cal= GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,i);
        List<Date> roster=Date.find.findList();
        if(roster!=null && i<roster.size() && i>=0){
            if(roster.get(i).workDay)
                return workDay;
            if(roster.get(i).holiday!=null)
                return holiday;
            //if(checkWeekend(cal))
                return weekend;
        }
        return old;
    }
    public static int transform(int i){
        //List<Date> roster=Date.find.findList();
        Calendar cal = GregorianCalendar.getInstance();
        int a=cal.get(Calendar.DAY_OF_WEEK);
        //a=1;
        if (a==1)
            a=8;
        return i-a+2;
    }

    public static Boolean checkDay(int i){
        if (i==0)
            return true;
        else
            return false;
    }

    public static String getDay(int i){
        List<Date> roster=Date.find.findList();
        Calendar cal = GregorianCalendar.getInstance();
        int a=transform(i);
        cal.add(Calendar.DAY_OF_YEAR, a);

        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }

    public static String getWorker(int i){
        String s="";
        List<Date> roster=Date.find.findList();
        //Calendar cal = GregorianCalendar.getInstance();
        i=transform(i);

        if(roster!=null && i<roster.size() && i>=0){
                if (roster.get(i).workDay && roster.get(i).worker!=null){
                    s=roster.get(i).worker.firstName+" "+roster.get(i).worker.lastName;
            }
            else{
                if(roster.get(i).holiday!=null){
                    if (roster.get(i).holiday.name!=null)
                        s=roster.get(i).holiday.name;
                }
            }
        }

        return s;
    }

    public static void calculate() {
        Calendar cal = GregorianCalendar.getInstance();
        List<Date> roster = Date.find.findList();

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        List<Worker> workerList = Worker.find.all();
        if(workerList==null){
            System.out.println("Dupa");
        }
        else{
            int i=0;
            Holiday holiday;
            while(i<50){
                for(int j=0;j<workerList.size();i++){
                Date date=createDate(cal);

                    if (!date.workDayLock){
                    holiday=checkHolidays(cal);
                    if(holiday!=null){
                        date.workDay=false;
                        date.holiday=holiday;
                        date.update();
                    } else if(checkWeekend(cal)){
                        date.workDay=false;
                        date.update();
                    }
                }
                if (date.workDay){
                        date.worker=workerList.get(j);
                        date.update();
                        Logger.info("worker: " +date.worker.firstName);
                        Logger.info("date: " + df.format(date.date.getTime()) + ", w.firstName: "+workerList.get(j).firstName + ", i=" + i);
                        j++;
                    }
                cal.add(Calendar.DAY_OF_YEAR, 1);
                }
            }
        }
    }
    public static Boolean checkWeekend(Calendar cal){
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }
    public static Holiday checkHolidays(Calendar cal){
        List<Holiday> holidays=Holiday.find.all();
        if (holidays!=null){
            for (Holiday h:holidays){
                if(h.year==cal.get(Calendar.YEAR) && h.dayOfTheYear==cal.get(Calendar.DAY_OF_YEAR)){
                    return h;
                }
            }
        }
        return null;
    }

    public static String currentTime(){
        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = GregorianCalendar.getInstance();
        //cal.set(Calendar.DAY_OF_MONTH,1);
        //cal.set(Calendar.MONTH, 0);
        //Calendar.DAY_OF_WEEK;
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        return date_format.format(cal.getTime());
    }

    public static Date createDate(Calendar cal){
        Date dateObject=new Date();
        dateObject.date=new java.sql.Date(cal.getTime().getTime());
        dateObject.locked=false;
        dateObject.workDay=true;
        dateObject.workDayLock=false;
        //dateObject.worker=w;
        dateObject.save();
        return dateObject;
    }

}
