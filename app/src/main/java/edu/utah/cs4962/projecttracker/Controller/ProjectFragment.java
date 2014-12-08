package edu.utah.cs4962.projecttracker.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import edu.utah.cs4962.projecttracker.R;
import edu.utah.cs4962.projecttracker.model.Project;

/**
 * Created by unaveed on 11/30/14.
 */
public class ProjectFragment extends Fragment
{
    Project mProject;
    EditText mTitleField;
    Button mDateButton;
    CheckBox mCompletedCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProject = new Project();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_project, parent, false);

        mTitleField = (EditText)v.findViewById(R.id.log_time_input);
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mProject.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        mDateButton = (Button)v.findViewById(R.id.submit_button);
        mDateButton.setText(mProject.getId().toString());
        mDateButton.setEnabled(false);

        mCompletedCheckBox = (CheckBox)v.findViewById(R.id.project_solved);
        mCompletedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                // set the project's completed property
                mProject.setCompleted(isChecked);
            }
        });

        return v;
    }
}
