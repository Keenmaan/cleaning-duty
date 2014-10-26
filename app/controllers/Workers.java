package controllers;

import models.Worker;

/**
 * Created by keen on 10/25/14.
 */
public class Workers {

    public static void createWorker(String firstName, String lastName){
        Worker worker=new Worker();
        worker.firstName=firstName;
        worker.lastName=lastName;
        //Users.getCurrentUser().worker=worker;
        worker.save();
        //RosterCalendar.calculate();
    }
}
