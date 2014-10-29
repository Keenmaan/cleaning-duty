package controllers;

/**
 * Created by keen on 10/8/14.
 */

import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.users;

import java.util.List;

public class Users extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result userList() {
        User user = User.find.where().eq("name", session("name")).findUnique();
        if (user.getIsAdmin()){
            List<User> userList = User.find.all();
            return ok(users.render(userList));
        }
        else{
            return redirect(routes.Application.index());
        }
    }

    public static void createUser(String name, String password, boolean isAdmin){
        User user=new User();
        user.name=name;
        user.password= Hasher.getHash(password, "md5");
        if (isAdmin)
            user.setIsAdmin();
        else
            user.setIsNotAdmin();
        user.save();
    }

    public static boolean findAdmin(){
        List<User> users;
        users= User.find.where().like("isAdmin","true").findList();
        if (users!=null)
            for (User e:users){
                if (e.getIsAdmin())
                    return true;
            }
        return false;
    }

    public static boolean findName(String name){
        if(User.find.where().like("name",name).findUnique()==null)
            return false;
        else
            return true;
    }

    public static boolean checkIsAdmin(){
        User user = User.find.where().eq("name", session("name")).findUnique();
        if (user!=null)
            return user.getIsAdmin();
        else
            return false;
    }

    public static User getCurrentUser(){
        return User.find.where().eq("name", session("name")).findUnique();
    }

    @Security.Authenticated(Secured.class)
    public static Result confirm(Long id){
        List<User> userList = User.find.all();
        if(Users.getCurrentUser().getIsAdmin()){
            User user=User.find.where().eq("id",id).findUnique();
            Logger.info("user = " + user);
            user.worker.confirmed=true;
            user.worker.update();
            RosterCalendar.calculate();
            return ok(users.apply(userList));
        }
        else
            return badRequest(users.apply(userList));
    }

    @Security.Authenticated(Secured.class)
    public static Result unConfirm(Long id){
        List<User> userList = User.find.all();
        if(Users.getCurrentUser().getIsAdmin()){
            User user=User.find.where().eq("id",id).findUnique();
            Logger.info("user = "+user);
            user.worker.confirmed=false;
            user.worker.update();
            RosterCalendar.calculate();
            return ok(users.apply(userList));
        }
        else
            return badRequest(users.apply(userList));
    }
}