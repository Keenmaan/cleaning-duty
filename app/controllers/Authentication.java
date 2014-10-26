package controllers;

import models.User;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.login;
import views.html.logout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keen on 10/25/14.
 */
public class Authentication extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result logout() {
        session().clear();
        return ok(logout.render());
    }

    public static Result loginForm() {
        Form<User> form = Form.form(User.class);
        return ok(login.render(form));
    }

    public static Result login(){
        Form<User> form = Form.form(User.class).bindFromRequest();
        List<ValidationError> errors = new ArrayList<>();
        if (form.hasErrors()) {
            form.reject("You have entered incorrect data.");
            return badRequest(login.render(form));
        } else {
            String name=form.get().name;
            String password=form.get().password;
            if (User.authenticate(name, password) == null) {
                errors.add(new ValidationError(
                        "name",
                        "Wrong password or user name."));
                form.errors().put("global",errors
                );
                return badRequest(login.render(form));
            }
            session().clear();
            session("name", form.get().name);
            return redirect(
                    routes.Application.index()
            );
        }
    }
}
