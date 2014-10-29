package controllers;

import models.Leave;
import models.User;
import models.Worker;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by keen on 10/25/14.
 */
public class Workers extends Controller {

    public static Worker createWorker(String firstName, String lastName, String email){
        User user=Users.getCurrentUser();
        Worker worker=new Worker();
        worker.firstName=firstName;
        worker.lastName=lastName;
        worker.email=email;
        worker.confirmed = user.getIsAdmin();
        worker.user=user;
        worker.save();
        user.worker=worker;
        user.update();
        if (worker.confirmed)
            RosterCalendar.calculate();
        return worker;
    }

    public static void updateWorker(String firstName, String lastName, String email) {
        User user=Users.getCurrentUser();
        user.worker.firstName=firstName;
        user.worker.lastName=lastName;
        user.worker.email=email;
        user.worker.update();
        if (user.worker.confirmed)
            RosterCalendar.calculate();
    }

    public static String getCurrentName(){
        User user=Users.getCurrentUser();
        if (user.worker!=null){
            if (user.worker.firstName!=null && user.worker.lastName!=null){
                return " " +user.worker.firstName + " " + user.worker.lastName;
            }
        }
        return " Please update your employee data.";
    }

    public static String getCurrentEmail(){
        User user=Users.getCurrentUser();
        if (user.worker!=null){
            if (user.worker.email!=null){
                return " " +user.worker.email;
            }
        }
        return " Please update your employee data.";
    }

    public static Leave createLeave(Calendar cal,Worker worker){
        Leave leave=findLeave(cal,worker);
        if(leave==null){
            leave=new Leave();
            leave.date=new java.sql.Date(cal.getTime().getTime());
            leave.worker=worker;
            leave.save();
            if(worker.leaves==null)
                worker.leaves=new ArrayList<Leave>();
            worker.leaves.add(leave);
            worker.update();
        }
        return leave;
    }
    public static List<Leave> createLeave(Calendar calStart,Calendar calEnd,Worker worker){
        List<Leave> leaves=new ArrayList<Leave>();
        int i=0;
        while((calStart.get(Calendar.DAY_OF_YEAR)<=calEnd.get(Calendar.DAY_OF_YEAR)
            || calStart.get(Calendar.YEAR)<calEnd.get(Calendar.YEAR))
            && i<300
            ){
            leaves.add(createLeave(calStart,worker));
            calStart.add(Calendar.DAY_OF_YEAR,1);
            i++;
        }
        return leaves;
    }
    public static Leave findLeave(Calendar cal,Worker worker){
        Leave leave=Leave.find.where()
                .eq("date", new java.sql.Date(cal.getTime().getTime()))
                .where().eq("worker",worker).findUnique();
        return leave;
    }

    public static String confirmedInfo(){
        User user=Users.getCurrentUser();
        if (user.worker!=null){
            if (user.worker.confirmed!=null){
                if (user.worker.confirmed)
                    return " Yes.";
                else
                    return " No. Please contact administrator for confirmation.";
            }
        }
        return " No. Please contact administrator for confirmation. Make sure you have your employee data updated.";
    }

    public static Boolean confirmed(User user) {
        if (user.worker != null) {
            if (user.worker.confirmed != null) {
                return user.worker.confirmed;
            }
        }
        return false;
    }
}
