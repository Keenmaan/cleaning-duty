import controllers.RosterCalendar;
import controllers.Users;
import controllers.Workers;
import models.Roster;
import models.Worker;
import play.Application;
import play.GlobalSettings;
import play.Logger;

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
        Worker.clearDates();
        Roster.clearDates();
        Workers.createWorker("M", "F");
        Workers.createWorker("Mzxc","Fxzcz");
        RosterCalendar.calculate();
    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

}