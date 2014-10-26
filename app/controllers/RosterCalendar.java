package controllers;

import models.Date;
import models.Roster;
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

    public static String getWorker(int i){
        String s="kup";
        Roster roster=Roster.find.findUnique();
        Calendar cal = GregorianCalendar.getInstance();
        if(roster!=null)
            s=roster.dates.get(i).worker.firstName+" "+roster.dates.get(i).worker.lastName;
        return s;
    }

    public static void calculate() {
        Calendar cal = GregorianCalendar.getInstance();
        Roster roster = Roster.find.findUnique();
        if (roster==null)
            roster=createRoster();

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        List<Worker> workerList = Worker.find.all();
        if(workerList==null){
            System.out.println("Dupa");
        }
        else{
            //Worker.clearDates();
            //Roster.clearDates();
            int i=0;
            while(i<31){
                for(Worker w: workerList){
                    Logger.info("worker"+w.firstName);
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    Date date=createDate(cal);
                    w.addDate(date);
                    //w.update();
                    roster.addDate(date);
                    //roster.update();
                    Logger.info("date: " + df.format(date.date.getTime()) + ", w.firstName: " + w.firstName + ", i=" + i);
                    i++;
                }
            }
        }
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
        //dateObject.save();
        return dateObject;
    }

    public static Roster createRoster(){
        Roster roster=new Roster();
        roster.save();
        return roster;
    }
}
