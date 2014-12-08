package edu.utah.cs4962.projecttracker;

import android.app.Activity;
import android.os.Bundle;


public class ProjectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_task);
    }
}
