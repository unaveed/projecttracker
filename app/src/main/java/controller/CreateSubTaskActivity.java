package controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import edu.utah.cs4962.projecttracker.R;
import edu.utah.cs4962.projecttracker.model.SubTask;

public class CreateSubTaskActivity extends Activity
{
    EditText mTitle, mDescription, mEstimatedTime, mLogTime;
    Spinner mSpinner;
    Button mButton;
    DatePicker mDatePicker;
    TextView mDatePickerText;
    String mProgress;
    int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        initializeFormElements();
        setDateDialog();

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                mProgress = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        Intent intent = new Intent();
                        StringBuilder date = new StringBuilder()
                                .append(mDatePicker.getMonth()).append("-")
                                .append(mDatePicker.getDayOfMonth())
                                .append("-").append(mDatePicker.getYear());
                        SubTask subTask = new SubTask(mTitle.getText().toString(),
                                mDescription.getText().toString(),
                                mEstimatedTime.getText().toString(),
                                date.toString(), mProgress);
                        intent.putExtra("SubTask Parcel", subTask);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                thread.start();
            }
        });
    }

    /**
     * Initialize DatePicker with the current date.
     */
    private void setDateDialog()
    {
        mDatePickerText = (TextView)findViewById(R.id.date_picker_textView);
        mDatePicker = (DatePicker)findViewById(R.id.date_picker);

        // Get the current day
        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        // Set text view with current day
        mDatePickerText.setText(new StringBuilder()
                .append(month + 1)
                .append("-").append(day).append("-")
                .append(year).append(" "));
        mDatePicker.init(year, month, day, null);
    }

    /**
     * Initialize all form views with appropriate validation for each
     * of the required views.
     */
    private void initializeFormElements()
    {
        mTitle = (EditText) findViewById(R.id.task_title_edit);
        if(mTitle.getText().toString().length() == 0)
            mTitle.setError("Title is required!");

        mDescription = (EditText) findViewById(R.id.task_description);

        mEstimatedTime = (EditText) findViewById(R.id.estimate_time_input);
        if(mEstimatedTime.getText().toString().length() == 0)
            mEstimatedTime.setError("You must estimate a time");

        mLogTime = (EditText)findViewById(R.id.log_time_input);

        // Initialize values for the progress selector
        mSpinner = (Spinner)findViewById(R.id.progress_select_list);
        ArrayAdapter<CharSequence> progressAdapter = ArrayAdapter.createFromResource(
                this, R.array.progress_list, android.R.layout.simple_spinner_item);
        progressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(progressAdapter);

        mButton = (Button)findViewById(R.id.submit_button);
    }
}
