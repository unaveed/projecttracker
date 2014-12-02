package edu.utah.cs4962.projecttracker.Controller;

import android.support.v4.app.Fragment;

import edu.utah.cs4962.projecttracker.SingleFragmentActivity;

/**
 * Created by unaveed on 11/30/14.
 */
public class ProjectListActivity extends SingleFragmentActivity
{

    @Override
    protected Fragment createFragment()
    {
        return new ProjectListFragment();
    }
}
