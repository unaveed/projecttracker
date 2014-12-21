package controller;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;

import edu.utah.cs4962.projecttracker.R;
import edu.utah.cs4962.projecttracker.model.SubTask;

public class ProjectActivity extends Activity
{
    ArrayList<SubTask> mTODOList, mProgressList, mCompletedList;
    ListView mToDoListView, mProgressListView, mCompletedView;
    TodoListAdapter      mToDoAdapter;
    ProgressListAdapter  mProgressAdapter;
    CompletedListAdapter mCompletedAdapter;
    Button mButton;
    static final int CREATE_SUB_TASK_CODE = 3;
    static final int MODIFY_SUB_TASK_CODE = 4;
    int mParentId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CREATE_SUB_TASK_CODE)
            createSubTaskResult(requestCode, resultCode, data);


        if (requestCode == MODIFY_SUB_TASK_CODE)
            modifySubTaskResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_activity);

        Bundle bundle = getIntent().getExtras();

        // Initialize sub-task lists for each progress list
        mTODOList = new ArrayList<SubTask>();
        mProgressList = new ArrayList<SubTask>();
        mCompletedList = new ArrayList<SubTask>();

        mButton = (Button)findViewById(R.id.create_subtask_btn);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(bundle.getString(MainActivity.TEXT_SERVICES_MANAGER_SERVICE));

        mParentId = bundle.getInt("Project ID");
        setupListView();

        String subTaskList = bundle.getString("subtasks");

        // Set Adapter and ListViews for the three progress lists
        mToDoAdapter = new TodoListAdapter(getApplicationContext(), mTODOList);
        mToDoListView.setAdapter(mToDoAdapter);

        mProgressAdapter = new ProgressListAdapter(getApplicationContext(), mProgressList);
        mProgressListView.setAdapter(mProgressAdapter);

        mCompletedAdapter = new CompletedListAdapter(getApplicationContext(), mCompletedList);
        mCompletedView.setAdapter(mCompletedAdapter);

        if(subTaskList.length() > 0)
            addGsonToLists(subTaskList);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ProjectActivity.this, CreateSubTaskActivity.class);
                startActivityForResult(intent, CREATE_SUB_TASK_CODE);
            }
        });

        mToDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), ModifySubTaskActivity.class);
                Gson gson = new Gson();
                String subTask = gson.toJson(mTODOList.get(position));
                intent.putExtra("subtask", subTask);
                startActivityForResult(intent, MODIFY_SUB_TASK_CODE);
            }
        });

        mProgressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), ModifySubTaskActivity.class);
                Gson gson = new Gson();
                String subTask = gson.toJson(mProgressList.get(position));
                intent.putExtra("subtask", subTask);
                startActivityForResult(intent, MODIFY_SUB_TASK_CODE);
            }
        });

        mCompletedView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), ModifySubTaskActivity.class);
                Gson gson = new Gson();
                String subTask = gson.toJson(mCompletedList.get(position));
                intent.putExtra("subtask", subTask);
                startActivityForResult(intent, MODIFY_SUB_TASK_CODE);
            }
        });
    }

    private void createSubTaskResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            SubTask subTask = data.getParcelableExtra("SubTask Parcel");
            addToList(subTask);

            // Return sub-task object to the activityResult in MainActivity
            Intent intent = getIntent();
            intent.putExtra("index", mParentId);
            Gson gson = new Gson();
            String jsonSubTask = gson.toJson(subTask);
            intent.putExtra("subtask", jsonSubTask);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void modifySubTaskResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            String bundle = data.getStringExtra("Modify SubTask");

            // Return sub-task object to the activityResult in MainActivity
            Intent intent = getIntent();
            intent.putExtra("index", mParentId);
            intent.putExtra("subtask", bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * Converts jsonArray to ArrayList<SubTask> and adds subtasks to their
     * appropriate lists.
     */
    private void addGsonToLists(String jsonArray)
    {
        Gson gson = new Gson();
        Type typeCollection = new TypeToken<ArrayList<SubTask>>(){}.getType();
        ArrayList<SubTask> subtaskList = gson.fromJson(jsonArray, typeCollection);

        for(SubTask task : subtaskList)
            addToList(task);
    }

    /**
     * Adds the given task to the correct list based on what it's progress is set to.
     */
    private void addToList(SubTask task)
    {
        if(task.getProgress().equals(SubTask.Progress.InProgress))
        {
            mProgressList.add(task);
            mProgressAdapter.notifyDataSetChanged();
        }
        else if(task.getProgress().equals(SubTask.Progress.Completed))
        {
            mCompletedList.add(task);
            mCompletedAdapter.notifyDataSetChanged();
        }
        else
        {
            mTODOList.add(task);
            mToDoAdapter.notifyDataSetChanged();
        }
    }

    private void setupListView()
    {
        mToDoListView = (ListView) findViewById(R.id.todo_list);
        TextView emptyTextForTODOList = (TextView) findViewById(R.id.empty_list_text);
        emptyTextForTODOList.setBackgroundColor(Color.parseColor("#fce40c"));
        mToDoListView.setEmptyView(emptyTextForTODOList);

        mProgressListView = (ListView) findViewById(R.id.progress_list);
        TextView emptyTextForProgressList = (TextView) findViewById(R.id.empty_list_text);
        emptyTextForProgressList.setBackgroundColor(Color.parseColor("#fce40c"));
        mProgressListView.setEmptyView(emptyTextForProgressList);

        mCompletedView = (ListView) findViewById(R.id.completed_list);
        TextView emptyTextForCompletedList = (TextView) findViewById(R.id.empty_list_text);
        emptyTextForCompletedList.setBackgroundColor(Color.parseColor("#fce40c"));
        mCompletedView.setEmptyView(emptyTextForCompletedList);
    }

    private class TodoListAdapter extends ArrayAdapter<SubTask>
    {
        TextView mLabel;
        View mView;

        public TodoListAdapter(Context context, ArrayList<SubTask> array)
        {
            super(context, android.R.layout.simple_list_item_1, array);
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            try
            {
                LayoutInflater inflater = getLayoutInflater();
                mView = inflater.inflate(R.layout.lv_rows, parent, false);
                mLabel = (TextView)mView.findViewById(R.id.item_title);
                mLabel.setText(mTODOList.get(position).getTitle());
                mLabel.setTextColor(Color.BLUE);
            }
            catch(Exception e)
            {
                Log.e("TodoListAdapter Exception", e.getMessage(), e);
            }

            return mView;
        }
    }

    private class ProgressListAdapter extends ArrayAdapter<SubTask>
    {
        TextView mLabel;
        View mView;

        public ProgressListAdapter(Context context, ArrayList<SubTask> array)
        {
            super(context, android.R.layout.simple_list_item_1, array);
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            try
            {
                LayoutInflater inflater = getLayoutInflater();
                mView = inflater.inflate(R.layout.lv_rows, parent, false);
                mLabel = (TextView)mView.findViewById(R.id.item_title);
                mLabel.setText(mProgressList.get(position).getTitle());
                mLabel.setTextColor(Color.BLUE);
            }
            catch(Exception e)
            {
                Log.e("ProgressList Exception", e.getMessage(), e);
            }

            return mView;
        }
    }

    private class CompletedListAdapter extends ArrayAdapter<SubTask>
    {
        TextView mLabel;
        View mView;

        public CompletedListAdapter(Context context, ArrayList<SubTask> array)
        {
            super(context, android.R.layout.simple_list_item_1, array);
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            try
            {
                LayoutInflater inflater = getLayoutInflater();
                mView = inflater.inflate(R.layout.lv_rows, parent, false);
                mLabel = (TextView)mView.findViewById(R.id.item_title);
                mLabel.setText(mCompletedList.get(position).getTitle());
                mLabel.setTextColor(Color.BLUE);
            }
            catch(Exception e)
            {
                Log.e("CompletedList Exception", e.getMessage(), e);
            }

            return mView;
        }
    }
}
