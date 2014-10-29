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

    public Boolean confirmed;

    @Column(unique=true)
    @Constraints.Email
    public String email;

    @OneToMany(targetEntity=Leave.class, mappedBy="worker", cascade = CascadeType.ALL)
    public List<Leave> leaves;

    @OneToMany(targetEntity=Date.class, mappedBy="worker")
    public List<Date> dates;

    @OneToOne(targetEntity = User.class)
    public User user;

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