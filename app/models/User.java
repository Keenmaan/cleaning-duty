package models;

import controllers.Hasher;
import play.Logger;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class User extends Model{
    @Id
    public Long id;

    @Constraints.Required
    @Column(unique=true)
    public String name;

    @Column(unique=true)
    @Constraints.Email
    public String email;

    //Password
    public static final int passwordLength=3;
    @Constraints.Required
    @Constraints.MinLength(passwordLength)
    public String password;

    private Boolean isAdmin;

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin() {
        this.isAdmin=true;
    }
    public void setIsNotAdmin() {
        this.isAdmin=false;
    }

    @OneToOne(targetEntity = Worker.class)
    public Worker worker;

    public static User authenticate(String name, String password) {
        User user = User.find.where().eq("name", name).findUnique();
        if (user != null && (user.password.equals(Hasher.getHash(password, "md5")))) {
            Logger.info("Authentication successful.");
            return user;
        } else {
            return null;
        }
    }

    public static Model.Finder<String,User> find = new Model.Finder<>(
            String.class, User.class
    );
}
