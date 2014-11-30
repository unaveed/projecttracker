package edu.utah.cs4962.projecttracker.model;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Task {

    private String mTitle;
    private Date mDueDate;
    private Time mEstimatedTime;
    private Time mActualTime;
    private ArrayList<Time> mLoggedTimes;

    public enum Status
    {
        TODO, InProgress, Completed
    }

    public Task()
    {
    }
}
