package edu.utah.cs4962.projecttracker.model;


import java.util.ArrayList;

public class Project {

    private ArrayList<Task> mTasks;
    private Priority mPriority;
    private boolean mCompleted;
    private String mTitle;

    public enum Priority
    {
       High, Medium, Low
    }

    public Project()
    {
        mTasks = new ArrayList<Task>();
        mCompleted = false;
    }

    public Project(String title, Priority priority)
    {
        mTasks = new ArrayList<Task>();
        mCompleted = false;
        mPriority = priority;
        mTitle = title;
    }

    public Project(String title, Priority priority, ArrayList<Task> tasks)
    {
        mTasks = tasks;
        mCompleted = false;
        mPriority = priority;
        mTitle = title;
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
}
