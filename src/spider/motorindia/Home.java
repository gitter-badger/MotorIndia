package spider.motorindia;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import spider.motorindia.AsyncImageLoader.onImageLoaderListener;
//this import establishes the link between this class and the "Retrivejson" class
import spider.motorindia.Retrivejson.MyCallbackInterface;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;



public class Home extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MyCallbackInterface {

	

	//TODO
	/*
	 * use the links provided by rishi and GET the images dynamically
	for now we are using stored images for storage


	these titles, ie "savedtitles" are shown in case Internet is not available
	MAKE these savedtitles values persistent ie, store it in app cache.
	UPDATE these values with the latest NO_TITLES titles everytime Internet connection is established
	 */

	
	String[] defaulttitles = {
		      "	 Tata Motors joins hands with Microlise for advanced telematics and fleet management services",
		      "	 Green manufacturing, a major challenge for emerging companies: Dr. Wilfried G. Aulbur",
		      "	 JOST Group maintains technology leadership with new innovative products",
		      "	 ZF symbolishes CV competence for sustainable future transport",
		      "	 MAN presents a range of new truck & bus technologies",
		      "	 Meritor global axle and brake capabilities well demonstrated",
		      "	 Call-in-centre for reefer transport operators opened in Delhi",
		      "	 Apollo Tyres confirms Hungary as location for first greenfield facility outside India",
		      "	 Volvo Buses remains unchallenged                                                     ",
		  } ;
	// the number of hardcoded titles
	int notitles =9;
	

		  Integer[] imageId = {
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		      R.drawable.ic_launcher,
		  };

		  //Due to the requirement for dynamic lists, ie add titles as we go down we need to
		  ArrayList<String> titles = new ArrayList<String>();
		  //where the cache of titles are stored when initially 
		  Set<String> tempsavedtitles;
		  String[] savedtitles;


	//Global variables
		  //ListView for the list in
		  ListView list;
		  //Flag variable for keeping track whether the action bar is being drawn up for the First time
		  int first_time=1;
		  //Flag variable for keeping track whether the list should be cleared on next updation of the list
		  int list_clear=1;
		  //the variable which keeps track of how many titles have been fetched from the Internet
		  int i=0;
		  // placeholder string array used to get the adapter
		  String[] titlearray;
		  // values which determine the number of threads which are launched and how frequently - it refers to article numbers
		  int till=NO_TITLES,from=1;
		  // A variable to keep track of the number of JSON objects which have been received
		  int nojson=0;

		  
		  //Global constants
		  // this is the number of titles that are fetched in groups
		  final static int NO_TITLES = 10;
		  //build version
		  private final int SDK = Build.VERSION.SDK_INT;
		  //for cache of titles
		  public static final String TITLES = "savedtitles";
		  //for titles storage
		  SharedPreferences title;
		  //For WHAT??
		  public static String tmpResponseForUIDownload = "";
		  public static Bitmap image;
		  private FileOutputStream fos;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    public CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        //now we draw up the list
        //populatelist();
        //TODO
        /*
         * We are unable to draw up the list in Oncreate(), the error thrown is a
        runtime exception - nullpointerexeption
        ie we somehow tried to access a field or method of an object or an element
        of an array when there is no instance or array to use.
         */

        //we set the shared preferences variable to the approppriate key value pair
    	title = getSharedPreferences(TITLES, 0);
        
        
        //TODO if orientation changed, then dont call the whole thing again!
        if(isNetworkConnected()){
        	// launch threads to get NO_TITLES titles from latest article
        	// As by default they are in Trucks section
        	if(first_time==1){
        		//no need to do this the task is launched from the action bar override 
        		launchthreadstogettitles(till,from,"Trucks");
        	}
            toast("Internet available, fetching latest articles");
        }
        else{
        	//get the cache from the app storage and keep it ready for display
            retrivecache();
        	toast("NO internet connection");
        	populatelist();
        }
 
    }
    
    
    // the function which retrieves the last saved titles and stores for use in case there is no internet
    private void retrivecache() {
    	
    	// Here we need to restore saved titles with the titles actually shared in memory
    	// get the default title list ready in case we don't get anything from the shared preferences
    	Set<String> defaultsettitles=new HashSet<String>();;
    	for(int i=1;i<notitles;i++){
    		defaultsettitles.add(defaulttitles[notitles-i]);
    	}
    	//thus now restore them into a temp variable
		tempsavedtitles=title.getStringSet("titles", defaultsettitles);
		savedtitles=tempsavedtitles.toArray(new String[tempsavedtitles.size()]);
	}



	//a function to check if Internet is available
    public boolean isNetworkConnected(){
    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo ni = cm.getActiveNetworkInfo();
    	if (ni == null) {
    	   // There are no active networks.
    	   return false;
    	  } else
    	   return true;
    }
    
    //a function to get the next NO_TITLES articles
    public void update(View v){
    	toast("Checking if Internet is available");
    	if(!isNetworkConnected()){
    		toast("Fetching of articles Failed! No Internet. ");
    		return;
    	}
    	toast("Fetching next "+Integer.toString(NO_TITLES)+" articles");
    	//start from 11th latest article when this is first called, then for subsequent calls
    	from=till+1;
    	// and get NO_TITLES articles from "till"
    	till=from+NO_TITLES-1;
    	//And start the threads
    	launchthreadstogettitles(till,from,mTitle.toString());
    }

    //a function to populate the list with titles from titles (as of now)
    public void populatelist(){
    	if(isNetworkConnected()){
    	   	//this sets up the dynamic array which is send to the adapter's constructor
            titlearray = titles.toArray(new String[titles.size()]);
            // Saves the fetched titles to the array for the future case when Internet is not available
            savedtitles=titlearray;
            //should we call the Storetocache(titlearray); in onstop so that speed is saved?
            Storetocache();
        	//calls the constructor to set the title and imageid
         	CustomList adapter = new CustomList(Home.this, titlearray, imageId);
         	//set "list" the handle, pointing to the respective views
            list=(ListView)findViewById(R.id.listView1);
            list.setAdapter(adapter);
    	}
    	else{
    		// As we don't have Internet connectivity
        	//calls the constructor to set the titles as the savedtitles and imageid
         	CustomList adapter = new CustomList(Home.this, savedtitles, imageId);
        	Log.i("debug", "no prob");
         	// set "list" the handle, pointing to the respective views
            list=(ListView)findViewById(R.id.listView1);
            list.setAdapter(adapter);
            // set a more appropriate footer
            TextView footer =(TextView)findViewById(R.id.footer_1);	
			footer.setText("No internet - Cannot Load Articles");
            
    	}
    	//As the first run is over
        first_time=0;
    }

    //Store to cache At the end of the activity's lifecycle
    //TODO and do it properly
    private void Storetocache() {
    	Log.i("debug","Storing to caache");
    	//BECAUSE WE ARE STOREING IN A SET, ORDER DOESNT MATTER THATS WHY 
    	//WHEN THEY RESTORE THE THE LIST ITS ALL JUMBLED UP! TODO
    	Set<String> tosaveset=new HashSet<String>();;
    	for(int i=1;i<savedtitles.length;i++){
    		tosaveset.add(savedtitles[i]);
    	}
		// This actually stores the latest titles to the shared preferences
    	SharedPreferences.Editor editor = title.edit();
        editor.putStringSet("titles", tosaveset);
        // Commit the edits!
        editor.commit();
		
	}

	//a function for toast
    public void toast(String display){
    	Toast.makeText(Home.this,display, Toast.LENGTH_SHORT).show();
    }

    //the function which starts the threads which in turn get the titles and set them as soon as we get them in the onrequestcompleted function
    public void launchthreadstogettitles(int end, int start, String category ){
    	String link = "http://motorindiaonline.in/mobapp/?s_i="+Integer.toString(start)+"&e_i="+Integer.toString(end)+"&cat_i="+category;
		new Retrivejson(this).execute(link);
		
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
    			mTitle = getString(R.string.title_section4);
    			break;
    		case 5:
    			mTitle = getString(R.string.title_section5);
    			break;
    		case 6:
    			mTitle = getString(R.string.title_section6);
    			break;
    		case 7:
    			mTitle = getString(R.string.title_section7);
    			break;
    		case 8:
    			mTitle = getString(R.string.title_section8);
    			break;
    		case 9:
                mTitle = getString(R.string.title_section9);
                break;
            case 10:
                mTitle = getString(R.string.title_section10);
                break;
            case 11:
                mTitle = getString(R.string.title_section11);
                break;
            case 12:
    			mTitle = getString(R.string.title_section12);
    			break;
    		case 13:
    			mTitle = getString(R.string.title_section13);
    			break;
    		
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);

		//TODO
        //AFTER A LOT OF WORK, @vishnugt has found a workaround, a fix to inflate the listview
        //different values will be used to populate the list when different options are selected

        //we also check if this is the first time, during oncreate when the action bar is set, if so, we shouldn't
        //trigger the toast prompting the user that a touch event has taken place
        if(mTitle==getString(R.string.title_section1) && first_time!=1){
        	//as the application initializes the action bar it checks which title is currently is on and due to the workaround alerts the user
        	//that a click has been registered
        }
        else if(mTitle==getString(R.string.title_section2)){
        	
        }
        else if(mTitle==getString(R.string.title_section3)){
        	
        }
        else if(mTitle==getString(R.string.title_section4)){
        	
        }
        else if(mTitle==getString(R.string.title_section5)){
        	
        }
        else if(mTitle==getString(R.string.title_section6)){
        	
        }
        else if(mTitle==getString(R.string.title_section7)){
        	
        }
        // because we dont want the last sections's titles to remain in the listview we set it to clear by
        list_clear=1;
        if(first_time==0){
        	// And we launch the thread to get the json for the
            launchthreadstogettitles(till, from, mTitle.toString());
            //We are already doing this in on create, no need to do it twice. Also in case of no internet
            // this line will cause the app to crash
        }
        
        //set "list" the handle, pointing to the respective views
        list=(ListView)findViewById(R.id.listView1);
        //TODO, find out WHY!!! passing null as the parent works.. i need to remove this link error OR explain it away
        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        list.addFooterView(footerView);	
        
        //as right now we have a single list for all these cases we call the function populatelist() (Right now no arguments because of that)
        //we only need to populate it once (on create the flag variable workaround is set thus using it we only call it the first time
        // the actionbar is drawn

        	//NOTE that this measure did not stop the issue #1 as predicted by me.
        	//i did not notice any noticeable change by bypassing the setting of the list over and over again.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Home) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    //This is the method inherited from Mycallbackinterface, which is called by retriveJSON after it receives the JSON object.
	@Override
	public void onRequestCompleted(JSONArray result) {
		//we have got a whole JSONArray, update the count
		nojson=nojson+NO_TITLES;
		
		//when the JSON array  titles has loaded, we can change the footer's text
		TextView footer =(TextView)findViewById(R.id.footer_1);	
		footer.setText("Fetch More Articles");
		
		
		// I got the JSON! i just used a interface! Communication complete!
		if(list_clear==1){
			// reset the titles arraylist, as we are getting the latest titles from their server
			titles.clear();
			list_clear=0;
			}
		for(int i=from-1;i<till;i++){
			try {
				//Just add the titles to the titles array
				titles.add(result.getJSONObject(i).getString("title"));
				//fetch the image
				//Retriveimage(result.getJSONObject(i).getString("image"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		
						
		//populate the list when the image comes in Retriveimage(link); ?
		populatelist();

			}



	//downloads the image and saves it to the SD card
	private void Retriveimage(String url) {
		AsyncImageLoader loader = new AsyncImageLoader(
				new onImageLoaderListener() {

					@Override
					public void onImageLoaded(Bitmap image,
							String response) {
						//btnSave.setEnabled(true);
						
						//TIME TO SAVE TO MEMORY
						/*--- this method will save your downloaded image to SD card ---*/

					    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					    /*--- you can select your preferred CompressFormat and quality. 
					     * I'm going to use JPEG and 100% quality ---*/
					    image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
					    /*--- create a new file on SD card ---*/
					    File file = new File(Environment.getExternalStorageDirectory()
					            + File.separator + "myDownloadedImage.jpg");
					    try {
					        file.createNewFile();
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
					    /*--- create a new FileOutputStream and write bytes to file ---*/
					    try {
					        fos = new FileOutputStream(file);
					    } catch (FileNotFoundException e) {
					        e.printStackTrace();
					    }
					    try {
					        fos.write(bytes.toByteArray());
					        fos.close();
					        
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
						toast("gggg");
					}

				});
		if (SDK >= 11)
			loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					url);
		else
			loader.execute(url);
	}
		

}



