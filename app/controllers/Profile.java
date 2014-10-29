package controllers;

import models.User;
import models.Worker;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Result;
import play.mvc.Security;
import views.html.profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static play.mvc.Results.*;

/**
 * Created by keen on 10/28/14.
 */
@Security.Authenticated(Secured.class)
public class Profile {

    public static Result profile(){
        return ok(profile.render(
                Form.form(Worker.class),
                Users.getCurrentUser()));
    }

    public static Result profileForm(){
        return ok(profile.render(
                Form.form(Worker.class),
                Users.getCurrentUser()));
    }

    public static Result dataUpdate(){
        Form<Worker> form = Form.form(Worker.class).bindFromRequest();
        List<ValidationError> errors = new ArrayList<>();
        if (form.hasErrors()){
            return badRequest(profile.render(form,Users.getCurrentUser()));
        }
        if (Users.getCurrentUser().worker!=null){
            Workers.updateWorker(
                    form.get().firstName,
                    form.get().lastName,
                    form.get().email);
        } else{
            //Safe validation:
            //Make sure there is no such username.
            List<Worker> workers= Worker.find.where()
                    .eq("lastName",form.get().lastName)
                    .findList();

            if (workers!=null){
                for(Worker w:workers){
                    if(w.firstName.equals(form.get().firstName)){
                        errors.add(new ValidationError(
                                "firstName",
                                "User of same first name and last name already exists",
                                new ArrayList<>()
                        ));
                        errors.add(new ValidationError(
                                "lastName",
                                "User of same first name and last name already exists",
                                new ArrayList<>()
                        ));
                        form.errors().put("firstName",errors);
                        form.errors().put("lastName",errors);
                    }
                }
            }
            //Check if proper password.
            if (form.hasErrors()){
                Logger.info("Errors:" + errors);
                return badRequest(profile.render(form,Users.getCurrentUser()));
            }
            else
                Workers.createWorker(
                        form.get().firstName,
                        form.get().lastName,
                        form.get().email);
        }
        return redirect(routes.Profile.profile());
    }

    public static Result addLeave(Long id,String dateStart,String dateEnd){
        Form<Worker> form = Form.form(Worker.class);
        if (form.hasErrors()){
            return badRequest(profile.apply(form, Users.getCurrentUser()));
        }
        int start=RosterCalendar.transform(Integer.valueOf(dateStart));
        int end=RosterCalendar.transform(Integer.valueOf(dateEnd));
        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.add(Calendar.DAY_OF_YEAR, start);
        Calendar cal2 = GregorianCalendar.getInstance();
        cal2.add(Calendar.DAY_OF_YEAR, end);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
        String dateInString = "Jun 7, 2013";
        User user=Users.getCurrentUser();
        if(user.worker!=null && user.id.equals(id)){
            Workers.createLeave(cal1,cal2,user.worker);
        }

        return ok(profile.apply(form,Users.getCurrentUser()));
    }

    public static Result removeLeave(Long id){
        Form<Worker> form=Form.form(Worker.class);
        return ok(profile.apply(form,Users.getCurrentUser()));
    }
}
