package edu.utah.cs4962.projecttracker.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.utah.cs4962.projecttracker.ProjectActivity;
import edu.utah.cs4962.projecttracker.R;
import edu.utah.cs4962.projecttracker.model.Project;
import edu.utah.cs4962.projecttracker.model.ProjectManger;

/**
 * Created by unaveed on 11/29/14.
 */
public class ProjectListFragment extends ListFragment
{
    private ArrayList<Project> mProjects;
    private static final String TAG = "ProjectListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mProjects = ProjectManger.get(getActivity()).getProjects();

        ProjectAdapter projectAdapter = new ProjectAdapter(mProjects);
        setListAdapter(projectAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        // Get the project from the adapter
        Project project = ((ProjectAdapter)getListAdapter()).getItem(position);
//        Log.d(TAG, project.getTitle() + " was clicked");

        // Start ProjectActivity
        Intent intent = new Intent(getActivity(), ProjectActivity.class);
        startActivity(intent);
    }

    private class ProjectAdapter extends ArrayAdapter<Project>
    {

        public ProjectAdapter(ArrayList<Project> projects)
        {
            super(getActivity(), 0, projects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_project, null);

            Project project = getItem(position);

            TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.project_list_item_title);
            titleTextView.setText(project.getTitle());

            TextView dateTextView =
                    (TextView) convertView.findViewById(R.id.project_list_item_dateTextView);
            dateTextView.setText(project.getDueDate().toString());

            View priorityView =
                    (View) convertView.findViewById(R.id.project_list_item_statusIndicator);
            priorityView.setBackgroundColor(project.getPriorityColor());

            return convertView;
        }
    }
}
