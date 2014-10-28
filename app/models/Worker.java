package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Worker extends Model{
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public String firstName;

    @Constraints.Required
    public String lastName;

    @OneToMany(targetEntity=Leave.class, mappedBy="worker", cascade = CascadeType.ALL)
    public List<Leave> leaves;

    @OneToMany(targetEntity=Date.class, mappedBy="worker")
    public List<Date> dates;

//    public void addDate(Date date){
//        this.dates.add(date);
//        date.setWorker(this);
//    }

    public static Model.Finder<String,Worker> find = new Model.Finder<>(
            String.class, Worker.class
    );

    public static void clearDates() {
        List<Worker> workerList = Worker.find.all();
        for(Worker w: workerList){
            for (Date d: w.dates){
                d.delete();
            }
        }
    }
}