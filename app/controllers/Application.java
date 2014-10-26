package controllers;

import models.Date;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;

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
        Date.find.where();
    }
}
