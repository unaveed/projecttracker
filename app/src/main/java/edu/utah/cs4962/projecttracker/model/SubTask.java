package edu.utah.cs4962.projecttracker.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class SubTask implements Parcelable
{
    private UUID mId;
    String mTitle, mDescription, mEstimatedTime;
    double mTime;
    ArrayList<LogTime> mLogTimes = new ArrayList<LogTime>();
    Date mDueDate;

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags)
    {
        parcel.writeString(mId.toString());
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
        parcel.writeString(mEstimatedTime);
        parcel.writeString(mDueDate.toString());
        parcel.writeString(mProgress.toString());
    }

    public enum Progress
    {
        TODO, InProgress, Completed
    }

    Progress mProgress;

    public static final Parcelable.Creator<SubTask> CREATOR = new Parcelable.Creator<SubTask>()
    {
        public SubTask createFromParcel(Parcel in)
        {
            return new SubTask(in);
        }

        @Override
        public SubTask[] newArray(int size)
        {
            return new SubTask[0];
        }
    };

    private SubTask(Parcel in)
    {
        mId = UUID.fromString(in.readString());
        mTitle = in.readString();
        mDescription = in.readString();
        mEstimatedTime = in.readString();

        try
        {
            mDueDate = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(in.readString());
        }
        catch(ParseException e)
        {
            Log.e("Subtask Parcel exception", e.getMessage());
            mDueDate = new Date();
        }

        mProgress = convertStringToProgress(in.readString());
    }

    public SubTask(String title, String description, String estTime, String date,
                   String progress)
    {
        mId = UUID.randomUUID();
        mTitle = title;
        mDescription = description;
        mEstimatedTime = estTime;
        mTime = Double.parseDouble(estTime);
        try
        {
            mDueDate = new SimpleDateFormat("MM-dd-yyyy").parse(date);
        }
        catch (ParseException e)
        {
            mDueDate = new Date();
        }
        mProgress = convertStringToProgress(progress);
    }

    public SubTask(String title, String description, String estTime, String date,
                   String progress, String uuid)
    {
        mId = UUID.fromString(uuid);
        mTitle = title;
        mDescription = description;
        mEstimatedTime = estTime;
        mTime = Double.parseDouble(estTime);
        try
        {
            mDueDate = new SimpleDateFormat("MM-dd-yyyy").parse(date);
        }
        catch (ParseException e)
        {
            mDueDate = new Date();
        }
        mProgress = convertStringToProgress(progress);
    }

    public SubTask(String title, String description, String estTime, Date date,
                   Progress progress)
    {
        mId = UUID.randomUUID();
        mTitle = title;
        mDescription = description;
        mEstimatedTime = estTime;
        mDueDate = date;
        mProgress = progress;
    }

    public SubTask(String title, String description, String estTime, Date date,
                   Progress progress, ArrayList<LogTime> times)
    {
        mTitle = title;
        mDescription = description;
        mEstimatedTime = estTime;
        mDueDate = date;
        mProgress = progress;
        mLogTimes = times;
    }

    private Progress convertStringToProgress(String s)
    {
        if(s.equals(Progress.InProgress.toString()) || s.equals("In Progress"))
            return Progress.InProgress;
        else if(s.equals(Progress.Completed.toString()))
            return Progress.Completed;
        else
            return Progress.TODO;
    }

    public void setTitle(String title)
    {
        this.mTitle = title;
    }

    public void setDescription(String description)
    {
        this.mDescription = description;
    }

    public void setEstimatedTime(String estimatedTime)
    {
        this.mEstimatedTime = estimatedTime;
    }

    public void addLogTime(double time)
    {
        mLogTimes.add(new LogTime(time));
    }

    public ArrayList<LogTime> getLogTimes()
    {
        return mLogTimes;
    }

    public void setProgress(String progress)
    {
        this.mProgress = convertStringToProgress(progress);
    }

    public UUID getId()
    {
        return mId;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public Date getDueDate()
    {
        return mDueDate;
    }

    public Progress getProgress()
    {
        return mProgress;
    }
}

