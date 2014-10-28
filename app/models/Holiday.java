package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by keen on 10/27/14.
 */
@Entity
public class Holiday extends Model{
    @Id
    Long id;

    public String name;

    public int dayOfTheYear;

    public int year;

    @ManyToOne
    public Roster roster;

    public static Model.Finder<String,Holiday> find = new Model.Finder<>(
            String.class, Holiday.class
    );
}
