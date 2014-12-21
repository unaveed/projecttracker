package controller;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import edu.utah.cs4962.projecttracker.R;
import edu.utah.cs4962.projecttracker.model.SubTask;


public class ModifySubTaskActivity extends Activity
{
    EditText mTitle, mDescription, mLogTime;
    String mProgress;
    Spinner mSpinner;
    SubTask mSubTask;
    ImageView mImageView;
    Button mAddAttachments, mSave;
    static final int FILE_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_task);

        Bundle bundle = getIntent().getExtras();
        Gson gson = new Gson();
        Type subTaskType = new TypeToken<SubTask>(){}.getType();
        mSubTask = gson.fromJson(bundle.getString("subtask"), subTaskType);

        mImageView = (ImageView)findViewById(R.id.picture_view);
        mTitle = (EditText) findViewById(R.id.edit_task_title);
        mDescription = (EditText)findViewById(R.id.edit_task_description);
        mLogTime = (EditText) findViewById(R.id.log_time_input);

        mTitle.setText(mSubTask.getTitle());
        mDescription.setText(mSubTask.getDescription());

        mSpinner = (Spinner) findViewById(R.id.modify_task_progress);
        ArrayAdapter<CharSequence> progressAdapter = ArrayAdapter.createFromResource(
                this, R.array.progress_list, android.R.layout.simple_spinner_item);
        progressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(progressAdapter);

        mAddAttachments = (Button)findViewById(R.id.add_attachment_btn);
        mAddAttachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select a picture"), FILE_PICKER);
                    }
                });
                thread.start();
            }
        });

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

        mSave = (Button) findViewById(R.id.modify_task_submit);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        Intent intent = getIntent();
                        mSubTask.setTitle(mTitle.getText().toString());
                        mSubTask.setDescription(mDescription.getText().toString());
                        if(mProgress != null)
                            mSubTask.setProgress(mProgress);
                        double log = 0.0;
                        try
                        {
                            log = Double.parseDouble(mLogTime.getText().toString());
                        }
                        catch(Exception e)
                        {
                            log = 0.0;
                        }

                        if(log > 0)
                            mSubTask.addLogTime(log);

                        Gson gson = new Gson();
                        String subTask = gson.toJson(mSubTask);
                        intent.putExtra("Modify SubTask", subTask);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                thread.start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == FILE_PICKER)
            {
                Uri pickedUri = data.getData();
                Bitmap pic = null;
                String imgPath = "";

                String[] medData = { MediaStore.Images.Media.DATA };

                //query the data
                Cursor picCursor = managedQuery(pickedUri, medData, null, null, null);
                if(picCursor!=null)
                {
                    //get the path string
                    int index = picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    picCursor.moveToFirst();
                    imgPath = picCursor.getString(index);
                }
                else
                    imgPath = pickedUri.getPath();

                if(pickedUri != null)
                {
                    //set the width and height we want to use as maximum display
                    int targetWidth = 600;
                    int targetHeight = 400;

                    //create bitmap options to calculate and use sample size
                    BitmapFactory.Options bmpOptions = new BitmapFactory.Options();

                    //first decode image dimensions only - not the image bitmap itself
                    bmpOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(imgPath, bmpOptions);

                    //image width and height before sampling
                    int currHeight = bmpOptions.outHeight;
                    int currWidth = bmpOptions.outWidth;

                    //variable to store new sample size
                    int sampleSize = 1;

                    //calculate the sample size if the existing size is larger than target size
                    if (currHeight>targetHeight || currWidth>targetWidth)
                    {
                        //use either width or height
                        if (currWidth>currHeight)
                            sampleSize = Math.round((float)currHeight/(float)targetHeight);
                        else
                            sampleSize = Math.round((float)currWidth/(float)targetWidth);
                    }

                    //use the new sample size
                    bmpOptions.inSampleSize = sampleSize;
                    //now decode the bitmap using sample options
                    bmpOptions.inJustDecodeBounds = false;

                    //get the file as a bitmap
                    pic = BitmapFactory.decodeFile(imgPath, bmpOptions);
                    mImageView.setImageBitmap(pic);
                }
            }
        }
    }
}
