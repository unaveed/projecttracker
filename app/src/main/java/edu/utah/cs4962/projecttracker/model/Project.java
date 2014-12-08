package edu.utah.cs4962.projecttracker.model;


import android.graphics.Color;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Project {
    private UUID mId;
    private ArrayList<Task> mTasks;
    private Priority mPriority;
    private boolean mCompleted;
    private String mTitle;
    private Date mDueDate;
    private final static int HIGH_PRIORITY_COLOR = Color.rgb(255,0,0);
    private final static int MEDIUM_PRIORITY_COLOR = Color.rgb(255, 94, 0);
    private final static int LOW_PRIORITY_COLOR = Color.rgb(51, 255, 51);

    public enum Priority
    {
       High, Medium, Low
    }

    public Project()
    {
        mId = UUID.randomUUID();
        mTasks = new ArrayList<Task>();
        mCompleted = false;
        mDueDate = null;
    }

    public Project(String title, Priority priority)
    {
        mId = UUID.randomUUID();
        mTasks = new ArrayList<Task>();
        mCompleted = false;
        mPriority = priority;
        mTitle = title;
        mDueDate = null;
    }

    public Project(String title, Priority priority, Date dueDate)
    {
        mId = UUID.randomUUID();
        mTasks = new ArrayList<Task>();
        mCompleted = false;
        mPriority = priority;
        mTitle = title;
        mDueDate = dueDate;
    }

    public Project(String title, Priority priority, ArrayList<Task> tasks)
    {
        mId = UUID.randomUUID();
        mTasks = tasks;
        mCompleted = false;
        mPriority = priority;
        mTitle = title;
        mDueDate = null;
    }

    public Project(String title, Priority priority, ArrayList<Task> tasks, Date dueDate)
    {
        mId = UUID.randomUUID();
        mTasks = tasks;
        mCompleted = false;
        mPriority = priority;
        mTitle = title;
        mDueDate = dueDate;
    }

    public UUID getId()
    {
        return mId;
    }

    public ArrayList<Task> getTasks() {
        return mTasks;
    }

    /** TODO: May not need this, possibly delete it **/
    public void setTasks(ArrayList<Task> tasks) {
        mTasks = tasks;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void addTask(Task task)
    {
        mTasks.add(task);
    }

    public Date getDueDate()
    {
        return mDueDate;
    }

    public void setDueDate(Date date)
    {
        mDueDate = date;
    }

    public int getPriorityColor()
    {
        if(mPriority == Priority.High)
            return HIGH_PRIORITY_COLOR;
        else if(mPriority == Priority.Medium)
            return MEDIUM_PRIORITY_COLOR;
        else
            return LOW_PRIORITY_COLOR;
    }

    @Override
    public String toString()
    {
        return mTitle;
    }
}
