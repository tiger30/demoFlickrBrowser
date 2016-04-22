package com.icestone.demoflickrbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName(); // "MainActivity"
    private List<Photo> mPhotosList = new ArrayList<Photo>();
    private RecyclerView mRecyclerView;
    private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;

    private static String FLICKR_API_PUBLIC_PHOTO_URL = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        activateToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(new ArrayList<Photo>(), MainActivity.this);
        mRecyclerView.setAdapter(flickrRecyclerViewAdapter);

//        /**moved to onResume()**/
//        ProcessPhotos processPhotos = new ProcessPhotos("witcher3, wildhunt", true);
//        processPhotos.execute();
//        /**------------------**/

//        GetFlickrJsonData flickrJsonData = new GetFlickrJsonData("android", true);
//        flickrJsonData.execute();

//        //Test for getting raw data
//        GetRawData theRawData = new GetRawData(FLICKR_API_PUBLIC_PHOTO_URL);
//        theRawData.execute();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (flickrRecyclerViewAdapter != null){
// because onResume always start after onCreate, and flickrRecyclerViewAdapter was moved to onCreate, flickrRecycleViewAdapter always exist.
        String query = getSavePreferenceData(FLICKR_QUERY);
        if (query.length() > 0) {
            ProcessPhotos processPhotos = new ProcessPhotos(query, true);
            processPhotos.execute();
        }
//        }
    }

    private String getSavePreferenceData(String key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPref.getString(key, "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Toast.makeText(MainActivity.this, "setting menu CLICKED", Toast.LENGTH_SHORT).show();
            return true;
        }
        //search icon
        if (id == R.id.menu_search) {
//            Toast.makeText(MainActivity.this, "Search clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public class ProcessPhotos extends GetFlickrJsonData {
        public ProcessPhotos(String searchCriteria, boolean matchAll) {
            super(searchCriteria, matchAll);
        }

        public void execute() {
            super.execute();
            processData processData = new processData();
            processData.execute();
        }

        public class processData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                flickrRecyclerViewAdapter.loadNewData(getPhotos());
//                flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(getMPhotos(), MainActivity.this);
//                mRecyclerView.setAdapter(flickrRecyclerViewAdapter);
            }
        }
    }
}
