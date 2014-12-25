package edu.utah.cs4962.projecttracker.model;


import java.text.DateFormat;
import java.util.Date;

public class LogTime {
    private Double mTime;
    private Date mLogDate;

    public LogTime(Double time)
    {
        mTime = time;
        mLogDate = new Date();
    }

    public Double getTime()
    {
        return mTime;
    }

    @Override
    public String toString()
    {
        return "Logged " + mTime + " hours on " + DateFormat.getDateInstance().format(mLogDate).toString();
    }
}
