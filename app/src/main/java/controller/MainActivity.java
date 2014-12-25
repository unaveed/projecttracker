package controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;

import edu.utah.cs4962.projecttracker.R;
import edu.utah.cs4962.projecttracker.model.Project;
import edu.utah.cs4962.projecttracker.model.SubTask;


public class MainActivity extends Activity {
    private ListView projectListView;
    private Button mButton;
    private ProjectListAdapter mProjectListAdapter;
    private ArrayList<Project> mProjects;
    static final int    NEW_PROJECT_REQUEST_CODE = 2;
    static final int    NEW_TASK_REQUEST_CODE = 1;
    static final String FILE_NAME                = "projects.txt";
    static final String SHARED_PREF              = "prefsfile";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Ensure the proper request code and result are being returned from activity
        if (requestCode == NEW_PROJECT_REQUEST_CODE)
            addProjectFromIntent(requestCode, resultCode, data);
        if (requestCode == NEW_TASK_REQUEST_CODE)
            addTasksFromIntent(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button)findViewById(R.id.addBtn);
        mProjects = new ArrayList<Project>();

        projectListView = (ListView)findViewById(R.id.list1);

        TextView emptyText = (TextView)findViewById(R.id.empty_list_text);
        emptyText.setTextColor(Color.parseColor("#000000"));
        emptyText.setBackgroundColor(Color.parseColor("#fce40c"));

        projectListView.setEmptyView(emptyText);

        mProjectListAdapter = new ProjectListAdapter(MainActivity.this, mProjects);
        projectListView.setAdapter(mProjectListAdapter);

        loadState();

        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.i("What the fuck", "Is this bullshit son?");

                Intent intent = new Intent(getApplicationContext(), ProjectActivity.class);
                intent.putExtra(MainActivity.TEXT_SERVICES_MANAGER_SERVICE,
                        mProjects.get(position).getTitle());
                intent.putExtra("Project ID", position);

                if(mProjects.get(position).getTasks().size() > 0)
                {
                    Gson gson = new Gson();
                    String subTasks = gson.toJson(mProjects.get(position).getTasks());
                    intent.putExtra("subtasks", subTasks);
                }
                else
                {
                    intent.putExtra("subtasks", "");
                }
                startActivityForResult(intent, NEW_TASK_REQUEST_CODE);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                        startActivityForResult(intent, NEW_PROJECT_REQUEST_CODE);
                    }
                });
                thread.start();
            }
        });
    }

    private void saveState()
    {
        Gson gson = new Gson();
        String jsonProjectList = gson.toJson(mProjects);

        SharedPreferences settings = getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("jsonArray", jsonProjectList);
        editor.commit();

//        if(mProjects != null)
//        {
//            Gson gson = new Gson();
//            String jsonProjectList = gson.toJson(mProjects);
//            try
//            {
//                File file = new File(getFilesDir(), FILE_NAME);
//                FileWriter writer = new FileWriter(file);
//                BufferedWriter bufferedWriter = new BufferedWriter(writer);
//                bufferedWriter.write(jsonProjectList);
//                bufferedWriter.close();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
    }

    private void loadState()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, 0);
        String jsonProjectList = sharedPreferences.getString("jsonArray", "DEFAULT");

        if(!jsonProjectList.equals("DEFAULT") && jsonProjectList.length() > 2)
        {
            Gson gson = new Gson();
            Type typeCollection = new TypeToken<ArrayList<Project>>() {}.getType();
            mProjects = gson.fromJson(jsonProjectList, typeCollection);
            mProjectListAdapter.addAll(mProjects);
        }

//        try
//        {
//            File file = new File(getFilesDir(), FILE_NAME);
//            FileReader reader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(reader);
//
//            String jsonProjectList = null;
//            jsonProjectList = bufferedReader.readLine();
//            if(!jsonProjectList.equals("null") && !jsonProjectList.equals("[]"))
//            {
//                Gson gson = new Gson();
//                Type typeCollection = new TypeToken<ArrayList<Project>>() {}.getType();
//                mProjects = gson.fromJson(jsonProjectList, typeCollection);
//                mProjectListAdapter.notifyDataSetChanged();
//            }
//
//            bufferedReader.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }

    /**
     * Adds a newly created project to mProjects after being created in another activity.
     */
    private void addProjectFromIntent(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            if (bundle != null)
            {
                String title = bundle.getString("title");
                Project.Priority priority = Project.parsePriority(bundle.getString("priority"));

                // Only create a new project if a title was entered
                if (title.length() > 0)
                {
                    if (mProjects == null)
                        mProjects = new ArrayList<Project>();

                    // Add project to list and refresh the adapter.
                    mProjectListAdapter.add(new Project(title, priority));
                }
            }
        }
    }

    /**
     * Adds one or more sub-tasks to a project after being created in another activity.
     */
    private void addTasksFromIntent(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            if (bundle != null)
            {
                int index = bundle.getInt("index");
                String tasks = bundle.getString("subtask");

                Gson gson = new Gson();
                Type type = new TypeToken<SubTask>(){}.getType();
                SubTask subTask= gson.fromJson(tasks, type);

                mProjects.get(index).addTask(subTask);
                mProjectListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("projects", mProjects);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        mProjects = savedInstanceState.getParcelableArrayList("projects");
        loadState();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        saveState();
    }
//    @Override
//    public void onPause()
//    {
//        super.onPause();
//        saveState();
//    }

//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        loadState();
//    }

    /**
     * ArrayAdapter for the list that holds projects that are
     * in progress.
     */
    class ProjectListAdapter extends ArrayAdapter<Project>
    {
        TextView label;
        TextView dueDateLabel;
        View status;
        View row;

        public ProjectListAdapter(Context context, ArrayList<Project> arr)
        {
            super(context, android.R.layout.simple_list_item_1, arr);
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            try{
                LayoutInflater inflater=getLayoutInflater();
                row = inflater.inflate(R.layout.list_item_project, parent, false);
                label = (TextView)row.findViewById(R.id.project_list_item_title);
                dueDateLabel = (TextView) row.findViewById(R.id.project_list_item_dateTextView);
                status = row.findViewById(R.id.project_list_item_status);

                label.setText(mProjects.get(position).getTitle());
                label.setTextColor(getResources().getColor(R.color.list_text_color));

                SubTask subTask = mProjects.get(position).getEarliestDueDate();
                StringBuilder dueDate = new StringBuilder();

                if(subTask != null)
                {
                    dueDate.append(subTask.getTitle())
                            .append(" due on ")
                            .append(
                                    DateFormat
                                            .getDateInstance()
                                            .format(subTask.getDueDate()));
                }
                else
                    dueDate.append("No tasks to complete");

                dueDateLabel.setText(dueDate.toString());

                status.setBackgroundColor(mProjects.get(position).getPriorityColor());
            }catch(Exception e){

            }
            return row;
        }
    }
}
