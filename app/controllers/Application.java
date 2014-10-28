package controllers;

import models.Date;
import models.Holiday;
import models.User;
import models.Worker;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;

import java.util.List;

public class Application extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result index() {
        String username=session("name");
        User user = User.find.where().eq("name", session("name")).findUnique();
        if (user.getIsAdmin()) {
            return ok(index.render("You are logged in as " + username + ". You have administration privileges."));
        }
        else
            return ok(index.render("You are logged in as " + username + "."));
    }

    public static void clearDatabase() {
        List<Date> date=Date.find.all();
        for (Date d:date){
            d.delete();
            d.save();
        }
        List<Worker> workers=Worker.find.all();
        for (Worker w:workers){
            w.delete();
            w.save();
        }
        List<Holiday> holidays=Holiday.find.all();
        for (Holiday h:holidays){
            h.delete();
            h.save();
        }
        Worker.clearDates();
    }
}
