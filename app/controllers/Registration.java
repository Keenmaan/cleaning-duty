package controllers;

import models.User;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.register;

import java.util.ArrayList;
import java.util.List;

public class Registration extends Controller {
    public static Result registerForm(){
        return ok(register.render(Form.form(User.class)));
    }

    public static Result register(){
        Form<User> form = Form.form(User.class).bindFromRequest();
        List<ValidationError> errors = new ArrayList<>();
        if (form.hasErrors()){
            return badRequest(register.render(form));
        }
        //Safe validation:
        //Make sure there is no such username.
        if (Users.findName(form.get().name)){
            errors.add(new ValidationError(
                    "name",
                    "User already exists.",
                    new ArrayList<>()
            ));
            form.errors().put("name",errors);
        }
        //Check if proper password.
        if (form.hasErrors()){
            Logger.info("Errors:" + errors);
            return badRequest(register.render(form));
        }
        else
            Users.createUser(
                    form.get().name,
                    form.get().password,
                    false);
        return redirect(routes.Authentication.loginForm());
    }
}
