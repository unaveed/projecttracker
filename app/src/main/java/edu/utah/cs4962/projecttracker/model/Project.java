package edu.utah.cs4962.projecttracker.model;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Project implements Parcelable {
    private ArrayList<SubTask> mTasks;
    private HashMap<UUID, Integer> mSubTaskLookupMap;
    private int mIndex;
    private Priority        mPriority;
    private boolean         mCompleted;
    private String          mTitle;
    private Date            mDueDate;
    private final static int HIGH_PRIORITY_COLOR   = Color.rgb(255, 0, 0);
    private final static int MEDIUM_PRIORITY_COLOR = Color.rgb(255, 94, 0);
    private final static int LOW_PRIORITY_COLOR    = Color.rgb(51, 255, 51);

    public enum Priority
    {
        High, Medium, Low
    }

    public Project()
    {
        mIndex = 0;
        mTasks = new ArrayList<SubTask>();
        mSubTaskLookupMap = new HashMap<UUID, Integer>();
        mCompleted = false;
        mDueDate = null;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mTitle);
        dest.writeString(mPriority.toString());
        dest.writeValue(mDueDate);
        dest.writeValue(mCompleted);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("tasks", mTasks);
        dest.writeBundle(bundle);
        dest.writeInt(mIndex);
    }

    public static final Creator<Project> CREATOR = new Creator<Project>()
    {
        @Override
        public Project createFromParcel(Parcel source)
        {
            String title = source.readString();
            Priority priority = parsePriority(source.readString());
            Date date = (Date) source.readValue(Project.class.getClassLoader());
            boolean completed = (Boolean) source.readValue(Project.class.getClassLoader());
            Bundle bundle = source.readBundle(SubTask.class.getClassLoader());
            ArrayList<SubTask> tasks = bundle.getParcelableArrayList("tasks");
            int index = source.readInt();
            Project project = new Project(title, priority, tasks, date, completed);
            project.setIndex(index);
            return project;
        }

        @Override
        public Project[] newArray(int size)
        {
            return new Project[size];
        }
    };

    public Project(Parcel in)
    {
        CREATOR.createFromParcel(in);
    }

    public Project(String title)
    {
        mTitle = title;
        mTasks = new ArrayList<SubTask>();
        mSubTaskLookupMap = new HashMap<UUID, Integer>();
        mCompleted = false;
        mDueDate = null;
    }

    public Project(String title, Priority priority)
    {
        mTasks = new ArrayList<SubTask>();
        mSubTaskLookupMap = new HashMap<UUID, Integer>();
        mCompleted = false;
        mPriority = priority;
        mTitle = title;
        mDueDate = null;
    }

    public Project(String title, Priority priority, Date dueDate)
    {
        mTasks = new ArrayList<SubTask>();
        mSubTaskLookupMap = new HashMap<UUID, Integer>();
        mCompleted = false;
        mPriority = priority;
        mTitle = title;
        mDueDate = dueDate;
    }

    public Project(String title, Priority priority, ArrayList<SubTask> tasks)
    {
        mTasks = tasks;
        mCompleted = false;
        mPriority = priority;
        mTitle = title;
        mDueDate = null;
    }

    public Project(String title, Priority priority, ArrayList<SubTask> tasks, Date dueDate, boolean completed)
    {
        mTasks = tasks;
        mCompleted = completed;
        mPriority = priority;
        mTitle = title;
        mDueDate = dueDate;
    }

    public ArrayList<SubTask> getTasks()
    {
        return mTasks;
    }

    /** TODO: May not need this, possibly delete it **/
    public void setTasks(ArrayList<SubTask> tasks)
    {
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

    public void addTask(SubTask task)
    {
        if(mSubTaskLookupMap.containsKey(task.getId()))
        {
            int index = mSubTaskLookupMap.get(task.getId());
            mTasks.set(index, task);
        }
        else
        {
            mTasks.add(task);
            mSubTaskLookupMap.put(task.getId(), mIndex++);
        }
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

    public static Priority parsePriority(String s)
    {
        if(s.equals(Priority.High.toString()))
            return Priority.High;
        else if (s.equals(Priority.Medium.toString()))
            return Priority.Medium;
        else
            return Priority.Low;
    }

    public int getNumberOfTasks()
    {
        if(mTasks == null || mTasks.size() == 0)
            return 0;

        return mTasks.size();
    }

    @Override
    public String toString()
    {
        return mTitle;
    }

    /**
     * Get the task that's due earliest.
     */
    public SubTask getEarliestDueDate()
    {
        if(mTasks.size() == 0)
            return null;

        if(mTasks.size() == 1)
        {
            if(!mTasks.get(0).mProgress.equals(SubTask.Progress.Completed))
                return mTasks.get(0);
            else
                return null;
        }
        Date earliestDate = mTasks.get(0).mDueDate;
        int index = -1;
        for(int i = 1; i < mTasks.size(); i++)
        {
            // Make sure you don't compare a completed sub-task
            if(!mTasks.get(i).mProgress.equals(SubTask.Progress.Completed))
            {
                if(mTasks.get(i).mDueDate.before(earliestDate))
                {
                    earliestDate = mTasks.get(i).mDueDate;
                    index = i;
                }
            }
        }
        return index > -1 ? mTasks.get(index) : null;
    }

    /**
     * Determines whether the project is complete based on the status
     * of the sub-tasks
     */
    public boolean isProjectComplete()
    {
        if(mTasks.size() > 0)
        {
            if(mTasks.size() == 1)
            {
                return mTasks.get(0).mProgress.equals(SubTask.Progress.Completed);
            }

            for (int i = 0; i < mTasks.size(); i++)
            {
                if(!mTasks.get(i).mProgress.equals(SubTask.Progress.Completed))
                    return false;
            }

            return true;
        }
        return false;
    }

    private void setIndex(int i)
    {
        mIndex = i;
    }
}