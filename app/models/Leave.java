package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by keen on 10/23/14.
 */
@Entity
public class Leave extends Model {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public java.sql.Date date;

//    @Constraints.Required
//    public java.sql.Date dateEnd;

    @ManyToOne
    public Worker worker;

    public static Model.Finder<String,Leave> find = new Model.Finder<>(
            String.class, Leave.class
    );
}
