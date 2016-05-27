package com.footballmatch.live;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

/**
 * Created by David Fortunato on 26/05/2016
 */
public class BaseNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    // Views
    private NavigationView viewNavigationView;
    private FloatingActionButton viewFloatingActionButton;
    private Toolbar viewToolBar;
    private ViewGroup viewBaseContainer; // This is the container to set the new data
    private DrawerLayout viewDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find Views
        viewToolBar = (Toolbar) findViewById(R.id.toolbar);
        viewNavigationView = (NavigationView) findViewById(R.id.nav_view);
        viewFloatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonBaseContainer);
        viewBaseContainer = (ViewGroup) findViewById(R.id.layoutBaseContainer);
        viewDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Setup ToolBar
        setSupportActionBar(viewToolBar);

        // Setup ActionBarDrawer Toggle
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, viewToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Setup Navigation View
        viewNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed()
    {
        if (viewDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            viewDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera)
//        {
//            // Handle the camera action
//        }
//        else if (id == R.id.nav_gallery)
//        {
//
//        }
//        else if (id == R.id.nav_slideshow)
//        {
//
//        }
//        else if (id == R.id.nav_manage)
//        {
//
//        }
//        else if (id == R.id.nav_share)
//        {
//
//        }
//        else if (id == R.id.nav_send)
//        {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);

        // TODO Implement this
        return true;
    }

}
