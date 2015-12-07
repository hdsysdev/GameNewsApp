package com.hdudowicz.gamenewsapp;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public int BASE_HEIGHT = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GetFeed getFeed = new GetFeed();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final ListView listView = (ListView) findViewById(android.R.id.list);
        ListView selectionList = (ListView) findViewById(R.id.selection_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        getFeed.execute();

        Context context = getApplicationContext();
        Resources resources = context.getResources();

        String[] options = resources.getStringArray(R.array.rss_names);
        TypedArray icons = resources.obtainTypedArray(R.array.icon_array);
        selectionList.setAdapter(new CustomNavAdapter(context, R.layout.list_item, options, icons));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                String text = ((TextView) view.findViewById(R.id.description)).getText().toString();
                ValueAnimator animator;

                Log.v("USER_LOG", Integer.toString(view.getLayoutParams().height));

                if (view.getLayoutParams().height == BASE_HEIGHT && text.length() >= 200) {
                    animator = ValueAnimator.ofInt(view.getLayoutParams().height, 450);
                    animator.setDuration(250);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            view.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                            view.findViewById(R.id.childRelativeLayout).requestLayout();
                        }
                    });
                    animator.start();
                } else if (view.getLayoutParams().height != BASE_HEIGHT) {
                    animator = ValueAnimator.ofInt(view.getLayoutParams().height, BASE_HEIGHT);
                    animator.setDuration(250);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            view.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                            view.findViewById(R.id.childRelativeLayout).requestLayout();
                        }
                    });
                    animator.start();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                Message currentMessage = getFeed.messages.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentMessage.getLink()));
                startActivity(intent);
                return false;
            }
        });
    }

    public void populate(List<Message> messages, List<Message> images) {
        ListView listView = (ListView) findViewById(android.R.id.list);
        if (listView != null) {
            CustomAdapter adapter = new CustomAdapter(
                    this,
                    R.layout.list_entry,
                    messages,
                    images);

            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }

    private class GetFeed extends AsyncTask {
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        private String[] names = getResources().getStringArray(R.array.rss_names);
        private String[] url = getResources().getStringArray(R.array.rss_uris);
        public List<Message> messages;
        private List<Message> imageList;

        @Override
        protected Object doInBackground(Object[] params) {
            for (int i = 0; i < names.length; i++) {
                if (messages == null || imageList == null) {
                    messages = new RssHandler(url[i]).parse();
                    imageList = new RssHandler(url[i]).parseImages();
                } else {
                    messages.addAll(new RssHandler(url[i]).parse());
                    imageList.addAll(new RssHandler(url[i]).parseImages());
                }
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Loading Feed");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            populate(messages, imageList);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.valve) {
            // Handle the camera action
        } else if (id == R.id.gamespot) {

        } else if (id == R.id.ign) {

        } else if (id == R.id.gameinformer) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}


