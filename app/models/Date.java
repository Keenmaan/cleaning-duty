package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Created by keen on 10/23/14.
 */
@Entity
public class Date extends Model{
    @Id
    public java.sql.Date date;

    //True - if changed manually by admin and therefor locked. Can be cancelled.
    //False - otherwise. If its unlocked, system can automatically change the shift on this day
    public Boolean locked;

    //True - if there are no holidays etc. Can be set-up manually
    public Boolean workDay;

    //True - if manually changed as a work day, even when it shouldn't be
    public Boolean workDayLock;

    @ManyToOne
    public Worker worker;

    @OneToOne
    public Holiday holiday;

    public static Model.Finder<String,Date> find = new Model.Finder<>(
            String.class, Date.class
    );

}
