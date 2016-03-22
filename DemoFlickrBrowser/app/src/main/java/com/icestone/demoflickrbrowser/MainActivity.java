package com.icestone.demoflickrbrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName(); // "MainActivity"
    private List<Photo> mPhotosList = new ArrayList<Photo>();
    private RecyclerView mRecyclerView;
    private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;

    private static String FLICKR_API_PUBLIC_PHOTO_URL = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GetFlickrJsonData flickrJsonData = new GetFlickrJsonData("android", true);
        flickrJsonData.execute();

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ProcessPhoto extends GetFlickrJsonData {
        public ProcessPhoto(String searchCriteria, boolean matchAll) {
            super(searchCriteria, matchAll);
        }

        public void execute(){
            super.execute();
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class processData extends DownloadJsonData {
            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(MainActivity.this, getMPhotos());
                mRecyclerView.setAdapter(flickrRecyclerViewAdapter);
            }
        }
    }
}
