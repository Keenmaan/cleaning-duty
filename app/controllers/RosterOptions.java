package controllers;

import models.Holiday;
import models.Roster;

import java.util.Calendar;

/**
 * Created by keen on 10/27/14.
 */
public class RosterOptions {
    public static void createNewRoster(String name){
        Roster roster=new Roster();
        roster.inUse=false;
        roster.name=name;
        roster.save();
    }
    public static void useRoster(String name){
        Roster rosterOld=Roster.find.where().eq("inUse", true).findUnique();
        Roster rosterNew=Roster.find.where().eq("name", name).findUnique();
        if (rosterNew!=null){
            rosterNew.inUse=true;
            rosterNew.update();
            if (rosterOld!=null){
                rosterOld.inUse=false;
                rosterOld.update();
            }
        }
    }
    public static Holiday addHoliday(Calendar date){
        Roster roster=Roster.find.where().eq("inUse", true).findUnique();
        Holiday holiday = new Holiday();
        holiday.dayOfTheYear=date.get(Calendar.DAY_OF_YEAR);
        holiday.year=date.get(Calendar.YEAR);
        roster.holidays.add(holiday);
        holiday.roster=roster;
        holiday.save();
        roster.update();
        return holiday;
    }
    public static Holiday addHoliday(Calendar date,String name){
        Holiday holiday=addHoliday(date);
        holiday.name=name;
        holiday.update();
        return holiday;
    }

    public static Roster checkIfRosterExists(){
        Roster roster=Roster.find.findUnique();
        if (roster!=null){
            Roster roster2=Roster.find.where().eq("inUse",true).findUnique();
            if (roster2!=null){
                return roster2;
            }
            else {
                roster.inUse=true;
                roster.update();
                return roster;
            }
        }
        else
            return null;
    }
}
