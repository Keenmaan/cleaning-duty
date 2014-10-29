package controllers;

import models.Date;
import models.Holiday;
import models.Leave;
import models.Worker;
import play.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by keen on 10/25/14.
 */
public class RosterCalendar {

    public static String getClassColor(int i){
        String workDay="workday";
        String currentDay="currentday";
        String weekend="weekend";
        String holiday="holiday";
        String old="old";
        i=transform(i);
        if(checkDay(i)){
            return currentDay;
        }
        Calendar cal= GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,i);
        Date date=Date.find.where()
                .eq("date",new java.sql.Date(cal.getTime().getTime())).findUnique();
        if(date!=null){
            if(date.workDay)
                return workDay;
            if(date.holiday!=null)
                return holiday;
            if(checkWeekend(cal))
                return weekend;
        }
        return old;
    }
    public static int transform(int i){
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
        Calendar cal = GregorianCalendar.getInstance();
        int a=transform(i);
        cal.add(Calendar.DAY_OF_YEAR, a);

        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }

    public static String getWorker(int i){
        Calendar cal = GregorianCalendar.getInstance();
        String s="";
        //List<Date> roster=Date.find.findList();
        i=transform(i);
        cal.add(Calendar.DAY_OF_YEAR, i);
        Date date=Date.find.where()
                .eq("date",new java.sql.Date(cal.getTime().getTime())).findUnique();
        if(date!=null){
            if(date.worker!=null)
                s=date.worker.firstName+" "+date.worker.lastName;
            else if(date.holiday!=null && date.holiday.name!=null)
                s=date.holiday.name;
        }

        return s;
    }

    public static void populate(){
        //List<Date> dates=Date.find.findList();
        Calendar cal=new GregorianCalendar();
        for (int i=0;i<100;i++){
            if(Date.find.where()
              .eq("date",new java.sql.Date(cal.getTime().getTime())).findUnique()!=null){
                Logger.info("date exists "+cal.getTime().toString());
            }
            else {
                createDate(cal);
            }
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    public static void calculate() {
        Logger.info("Calculate().");
        //Calendar days and holidays population
        populate();
        Calendar cal = GregorianCalendar.getInstance();
        List<Date> roster = Date.find.findList();

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        List<Date> workDays=new ArrayList<Date>();

        Holiday holiday;
        if(roster!=null)
        for(Date date:roster){
            Logger.info("date: "+df.format(date.date));
            cal.setTime(date.date);
            if(!date.workDayLock){
                holiday=checkHolidays(cal);
                if(holiday!=null){
                    date.workDay=false;
                    date.holiday=holiday;
                    Logger.info("holiday: "+date.holiday.name);
                    date.update();
                } else if(checkWeekend(cal)){
                    Logger.info("weekend: "+checkWeekend(cal));
                    date.workDay=false;
                    date.update();
                } else {
                    Logger.info("workday");
                    date.workDay=true;
                    date.update();
                }
            }
            if(date.workDay){
                workDays.add(date);
            }
            Logger.info(".workDay= "+date.workDay);
            Logger.info("**");
        }

        //Workers and their days
        List<Worker> workerList = Worker.find.all();
        workerList=properList(workerList);
        int workerListSize=1;
        if(workerList==null){
            Logger.info("workerList (proper) is empty");
        }
        else{
            workerListSize=workerList.size();
            Logger.info("workerList.size():"+workerList.size());
        }
        if(workerList!=null){
            int i=0,n=workerListSize;
            for(Date date:workDays){
                Worker worker=getWorkerNotOnLeave(workerList,i,date);
                if(worker!=null){
                    date.worker=worker;
                    worker.dates.add(date);
                    date.update();
                    worker.update();
                }
                i++;
            }
        }
    }
    public static Worker getWorkerNotOnLeave(List<Worker> workers,int i,Date date){
        for (int j=0;j<workers.size();j++){
            if(!checkIfWorkerOnLeave(workers.get((i+j)%workers.size()),date)){
                return workers.get((i+j)%workers.size());
            }
        }
        return null;
    }
    public static Boolean checkIfWorkerOnLeave(Worker worker,Date date){
        return Leave.find.where().eq("worker",worker)
                .where().eq("date",date.date)
                .findUnique()!=null;
    }
    public static Date dateExists(Calendar cal){
        return Date.find.where().eq("date",new java.sql.Date(cal.getTime().getTime())).findUnique();
    }
    public static void calculateHolidays(){

    }
    public static Boolean calculateWorkDay(Date date){
        return false;
    }

    public static List<Worker> properList(List<Worker> workerList){
        List<Worker> workers=null;
        for (Worker w:workerList){
            if(w.confirmed){
                if (workers==null)
                    workers=new ArrayList<Worker>();
                workers.add(w);
            }
        }
        if (workers==null)
            Logger.info("workers is null");
        return workers;
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

    public static Date createDate(Calendar cal){
        Date dateObject=new Date();
        dateObject.date=new java.sql.Date(cal.getTime().getTime());
        dateObject.locked=false;
        dateObject.workDay=true;
        dateObject.workDayLock=false;
        dateObject.save();
        return dateObject;
    }
}
