package controller;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.utah.cs4962.projecttracker.R;

public class ProjectActivity extends Activity
{
    String[] mAlphabets = {"A","B","C","D","E","F","G","H","I","J","K","L"};
    ListView L1, L2;
    Button mButton;
    DeadlineListAdapter mDeadlineListAdapter;
    ProjectListAdapter mProjectListAdapter;
    ArrayList<String> mProducts;
    static final int REQUEST_CODE = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Ensure the proper request code and result are being returned from activity
        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Bundle bundle = data.getExtras();
                if(bundle != null)
                {
                    // Add new project to the list
                    mProducts.add(bundle.getString(
                            DisplayMessageActivity.INPUT_METHOD_SERVICE
                    ));

                    // Refresh the list
                    mProjectListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProducts = new ArrayList<String>();
        mButton = (Button)findViewById(R.id.addBtn);

        L2 = (ListView)findViewById(R.id.list2);
        L1 = (ListView)findViewById(R.id.list1);

        TextView emptyText = (TextView)findViewById(R.id.empty_list_text);
        emptyText.setTextColor(Color.parseColor("#000000"));
        emptyText.setBackgroundColor(Color.parseColor("#fce40c"));

        L1.setEmptyView(emptyText);

        mDeadlineListAdapter = new DeadlineListAdapter(this, mAlphabets);
        L2.setAdapter(mDeadlineListAdapter);

        setProducts(0);

        L1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), ProjectActivity.class);
                intent.putExtra(ProjectActivity.TEXT_SERVICES_MANAGER_SERVICE, mProducts.get(position));
                startActivity(intent);
            }
        });

        L2.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                setProducts(arg2);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ProjectActivity.this, AddProjectActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                Log.i("Submit button", "What is going on?");
            }
        });
    }

    public void setProducts(int number){
        mProjectListAdapter = new ProjectListAdapter(ProjectActivity.this,mProducts);
        L1.setAdapter(mProjectListAdapter);
    }

    /**
     * Represents the ArrayAdapter for the list that holds upcoming
     * deadlines.
     */
    class DeadlineListAdapter extends ArrayAdapter<String>
    {
        TextView label;
        ImageView image;
        View row;
        public DeadlineListAdapter(Context context, String[] arr)
        {
            super(context, android.R.layout.simple_list_item_1, arr);
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            try{
                LayoutInflater inflater=getLayoutInflater();
                row = inflater.inflate(R.layout.lv_rows, parent, false);
                label = (TextView)row.findViewById(R.id.item_title);
                label.setText(mAlphabets[position]);
                label.setTextColor(Color.YELLOW);
            }catch(Exception e){

            }
            return row;
        }
    }

    /**
     * ArrayAdapter for the list that holds projects that are
     * in progress.
     */
    class ProjectListAdapter extends ArrayAdapter<String>
    {
        TextView label;
        ImageView image;
        View row;
        public ProjectListAdapter(Context context, ArrayList<String> arr)
        {
            super(context, android.R.layout.simple_list_item_1, arr);
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            try{
                LayoutInflater inflater=getLayoutInflater();
                row = inflater.inflate(R.layout.lv_rows, parent, false);
                label = (TextView)row.findViewById(R.id.item_title);
                label.setText(mProducts.get(position));
                label.setTextColor(Color.BLUE);
            }catch(Exception e){

            }
            return row;
        }
    }
}
