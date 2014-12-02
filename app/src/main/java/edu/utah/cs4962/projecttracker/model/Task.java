package edu.utah.cs4962.projecttracker.model;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Task {

    private UUID mId;
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
        mId = UUID.randomUUID();
    }

    public UUID getId()
    {
        return mId;
    }
}
