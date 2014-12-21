package controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import edu.utah.cs4962.projecttracker.R;

public class DisplayMessageActivity extends Activity
{
    Button mButton;
    EditText mEditText;
    Spinner mSpinner;
    String mPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_project);

        mEditText = (EditText) findViewById(R.id.task_title);
        mSpinner = (Spinner) findViewById(R.id.priority_spinner);

        mButton = (Button) findViewById(R.id.submit_button);
        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra("title", mEditText.getText().toString());
                intent.putExtra("priority", mPriority);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        ArrayAdapter<CharSequence> progressAdapter = ArrayAdapter.createFromResource(
                this, R.array.priority_list, android.R.layout.simple_spinner_item);
        progressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(progressAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                mPriority = mSpinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }
}
