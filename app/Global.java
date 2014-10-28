import controllers.RosterCalendar;
import controllers.RosterOptions;
import controllers.Users;
import controllers.Workers;
import models.PolishHolidays;
import play.Application;
import play.GlobalSettings;
import play.Logger;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        Logger.info("Application has started.");
      Logger.info("Looking for admin account...");
        if (!Users.findAdmin()){
            String name="admin";
            String password="admin";
            Users.createUser(
                    name,
                    password,
                    true);
            Logger.info("No admin user found." + name + "/" + password + " user created.");
        }
        else
            Logger.info("Admin found.");
        controllers.Application.clearDatabase();

        if(RosterOptions.checkIfRosterExists()==null){
            RosterOptions.createNewRoster("DefaultRoster");
            RosterOptions.useRoster("DefaultRoster");

        }
        Calendar calendar=new GregorianCalendar(2014, 10, 01);
        RosterOptions.addHoliday(PolishHolidays.wielkanoc(GregorianCalendar.getInstance().get(Calendar.YEAR)),"Wielkanoc");
        RosterOptions.addHoliday(calendar,"Wszystkich Świętych");
        RosterOptions.addHoliday(new GregorianCalendar(2014,10,05));

        Workers.createWorker("Marcin", "Kowalski");
        Workers.createWorker("Mariusz","Iksiński");
        Workers.createWorker("Łucja","Iksińska");
        Workers.createWorker("Mateusz","Horwacy-Dębicki");
        RosterCalendar.calculate();
    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

}