package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by keen on 10/23/14.
 */
@Entity
public class Roster extends Model {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long id;

    //TBA : various options for calculating the roster (eg. monday-friday or monday-saturday)

    @OneToMany(targetEntity=Date.class, mappedBy="roster", cascade = CascadeType.ALL)
    public List<Date> dates;

    public void addDate(Date date){
        this.dates.add(date);
        date.setRoster(this);
    }

    public static Model.Finder<String,Roster> find = new Model.Finder<>(
            String.class, Roster.class
    );

    public static void clearDates() {
        List<Roster> list = Roster.find.all();
        for(Roster l: list){
            for (Date d: l.dates){
                d.delete();
            }
        }
    }
}
