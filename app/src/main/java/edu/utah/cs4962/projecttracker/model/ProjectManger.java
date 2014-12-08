package edu.utah.cs4962.projecttracker.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by unaveed on 11/29/14.
 */
public class ProjectManger
{
    private ArrayList<Project> mProjects;
    private static ProjectManger mProjectManger = null;
    private Context sAppContext;

    public static ProjectManger get(Context context)
    {
        if(mProjectManger == null)
            mProjectManger = new ProjectManger(context.getApplicationContext());

        return mProjectManger;
    }
    private ProjectManger(Context context)
    {
        sAppContext = context;
        mProjects = new ArrayList<Project>();

        for(int i = 0; i < 10; i++)
        {
            Project p;
            if(i % 3 == 0)
                p = new Project("Project" + i, Project.Priority.High, new Date(114, i, (i+1) * 2));
            else if (i % 2 == 0)
                p = new Project("My project" + i, Project.Priority.Medium, new Date(114, i, (i+1) * 2));
            else
                p = new Project("CS 4962 project - " + i, Project.Priority.Low, new Date(114, i, (i+1) * 2));

            mProjects.add(p);
        }
    }

    public ArrayList<Project> getProjects()
    {
        return mProjects;
    }

    public Project getProject(UUID id)
    {
        for(Project project : mProjects)
        {
            if(project.getId().equals(id))
                return project;
        }

        return null;
    }
}
