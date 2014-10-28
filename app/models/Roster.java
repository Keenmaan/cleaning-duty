package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Roster extends Model{
    @Id
    public String name;

    public Boolean inUse;

    @OneToMany(targetEntity=Holiday.class, mappedBy="roster")
    public List<Holiday> holidays;

    public static Model.Finder<String,Roster> find = new Model.Finder<>(
            String.class, Roster.class
    );
}
