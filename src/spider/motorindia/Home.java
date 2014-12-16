package spider.motorindia;

import java.util.ArrayList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

//this import establishes the link between this class and the "Retrivejson" class
import spider.motorindia.Retrivejson.MyCallbackInterface;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;



public class Home extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MyCallbackInterface {





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
		  };

		  //Due to the requirement for dynamic lists, ie add titles as we go down we need to
		  ArrayList<String> titles = new ArrayList<String>();
		  // Same thing, we need a dynamic list for images
		  ArrayList<String> images = new ArrayList<String>();
		  // Similarly we need the list of id's for fetching the actual content
		  ArrayList<Integer> id = new ArrayList<Integer>();
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
		  int no=NO_TITLES,from=1;
		  // A variable to keep track of the number of JSON objects which have been received
		  int nojson=0;
		  // to keep track of orientation change
		  int orientation_same = 1;
		  int orientation = -1;
		  // to make sure only 10 article are called not more than that on scroll down
		  int oldtotal=10,once=1;
		  // webview handle for the loading animation
		  WebView load;


		  //Global constants
		  // this is the number of titles that are fetched in groups
		  final static int NO_TITLES = 10;
		  // for the ID we send
		  public final static String EXTRA_MESSAGE = "com.spider.motorindia.ID";
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

        Configuration config = getResources().getConfiguration();
        if(orientation==-1){
        	orientation = config.orientation;
        }
        else {
        	orientation_same=0;
        }

        //TODO
        /*
         * We are unable to draw up the list in Oncreate(), the error thrown is a
        runtime exception - nullpointerexeption
        ie we somehow tried to access a field or method of an object or an element
        of an array when there is no such instance or array to use.
         */

        //TODO if orientation changed, then dont call the whole thing again! this is part of caching the titles and corresponding article contents
        if(isNetworkConnected()){
        	/* this caused problem in orientation change lets keep it in action bar
        	// launch threads to get NO_TITLES titles from latest article
        	// As by default they are in Trucks section
        	if(first_time==1){
        		//no need to do this the task is launched from the action bar override
        		launchthreadstogettitles(no,from,"Trucks");
        	}
            */
        }
        else{
        	createNetErrorDialog();
        	toast("NO internet connection");
        }

    }

    protected void createNetErrorDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You need a network connection to use this application. Please turn on mobile network or Wi-Fi in Settings.")
            .setTitle("Unable to connect")
            .setCancelable(false)
            .setPositiveButton("Settings",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(i);
                }
            }
        )
        .setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Home.this.finish();
                }
            }
        );
        AlertDialog alert = builder.create();
        alert.show();
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

    /*
    //a function to get the next NO_TITLES articles
    public void update(View v){
    	toast("Checking if Internet is available");
    	if(!isNetworkConnected()){
    		toast("Fetching of articles Failed! No Internet. ");
    		return;
    	}
    	toast("Fetching next "+Integer.toString(NO_TITLES)+" articles");
    	//start from 11th latest article when this is first called, then for subsequent calls
    	from=from+10;
    	// and get NO_TITLES articles from "from"
    	no=NO_TITLES;
    	//And start the threads
        launchthreadstogettitles(no, from, mTitle.toString());

    }
*/
    //a function to populate the list with titles from titles (as of now)
    public void populatelist(){

    	if(isNetworkConnected()){
    	   	//this sets up the dynamic array which is send to the adapter's constructor
            titlearray = titles.toArray(new String[titles.size()]);
            // Saves the fetched titles to the array for the future case when Internet is not available
            savedtitles=titlearray;
            String[] imageurl = images.toArray(new String[images.size()]);
        	//calls the constructor to set the title and imageid
         	CustomList adapter = new CustomList(Home.this, titlearray, imageId, imageurl);
         	//set "list" the handle, pointing to the respective views
            list=(ListView)findViewById(R.id.listView1);
            if(list==null){
            	// fixed the crash on quick orientation change or basically when this function is called during the period of time when "listview" is not part of the layout
            	// this happens as this function is called asynchronously, thus its possible to be called during the time the listview is being drawn.
            	return;
            }
            list.setAdapter(adapter);

            // set the onclick listener
            list.setOnItemClickListener(new OnItemClickListener() {



				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// lets handle the click send the id to displayarticle activity
					Intent intent = new Intent(Home.this,Displayarticle.class);
					intent.putExtra(EXTRA_MESSAGE, id.get(position));
					startActivity(intent);

				}

            });

            // set the onscroll listener
            list.setOnScrollListener(new OnScrollListener(){
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                	Log.i("debug","first "+Integer.toString(firstVisibleItem)+" visible Item count "+Integer.toString(visibleItemCount)+" Total item count "+Integer.toString(totalItemCount));
                	// if we are in the "Batteries" category where we only have 4 articles
                	if(mTitle.toString()==getString(R.string.title_section9)){
                		//we should return as the list is always in such a position that we request more articles
                		// this will cause a overload of asynctasks being launched.
                		return;
                		//TODO I recommend removing this unimportant category.
                	}

					int check = totalItemCount/4;
					int delay;
					if(firstVisibleItem>=check){
						delay=1;
					}
					else{
						delay=0;
					}
					if(totalItemCount==(firstVisibleItem+visibleItemCount) && delay==1){

						//all so that it Doesn't do a lot of calls in a single call
						if(once==1){
							once=0;
							Log.i("debug", "ONCE");
	                    	toast("Checking if Internet is available");
	                    	if(!isNetworkConnected()){
	                    		toast("Fetching of articles Failed! No Internet. ");
	                    		return;
	                    	}
	                    	toast("Fetching next "+Integer.toString(NO_TITLES)+" articles");
	                    	//start from 11th latest article when this is first called, then for subsequent calls
	                    	from=from+10;
	                    	// and get NO_TITLES articles from "from"
	                    	no=NO_TITLES;
	                    	//And start the threads
	                        launchthreadstogettitles(no, from, mTitle.toString());

	                    	delay=0;
	                    	oldtotal=totalItemCount;
	                    	return;
	                    }
						if(oldtotal!=totalItemCount){
							once=1;
						}



                    }


                	//So let fetch more when the first item is total-6 ie when the 6th lat article is seen
                	/*
                	toast("Checking if Internet is available");
                	if(!isNetworkConnected()){
                		toast("Fetching of articles Failed! No Internet. ");
                		return;
                	}
                	toast("Fetching next "+Integer.toString(NO_TITLES)+" articles");
                	//start from 11th latest article when this is first called, then for subsequent calls
                	from=from+10;
                	// and get NO_TITLES articles from "from"
                	no=NO_TITLES;
                	//And start the threads
                	if(mTitle.toString()==getString(R.string.title_section3)){
                		launchthreadstogettitles(no, from, "ConstructionEquipment");
                	}
                	else{
                    launchthreadstogettitles(no, from, mTitle.toString());
                	}
                	*/

                }


				@Override
				public void onScrollStateChanged(AbsListView arg0, int arg1) {
					//Not being used!

				}
            });

    	}
    	else{
    		// As we don't have Internet connectivity
        	//calls the constructor to set the titles as the savedtitles and imageid
         	CustomList adapter = new CustomList(Home.this, savedtitles, imageId, null);
         	// set "list" the handle, pointing to the respective views
            list=(ListView)findViewById(R.id.listView1);
            list.setAdapter(adapter);


    	}
    	//As the first run is over
        first_time=0;
    }

	//a function for toast
    public void toast(String display){
    	Toast.makeText(Home.this,display, Toast.LENGTH_SHORT).show();
    }

    //the function which starts the threads which in turn get the titles and set them as soon as we get them in the onrequestcompleted function
    public void launchthreadstogettitles(int no, int start, String category ){
    	if(category==getString(R.string.title_section3)){
    		category="ConstructionEquipment";
    	}
    	else if(category==getString(R.string.title_section8)){
    		category="Lubes";
    	}
    	String link = "http://motorindiaonline.in/mobapp/?s_i="+Integer.toString(start)+"&e_i="+Integer.toString(no)+"&cat_i="+category;
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

        // to start the loading animation
     	load = (WebView)findViewById(R.id.webView2);
     	load.loadUrl("file:///android_asset/spiffygif.gif");

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

        if(isNetworkConnected()){
        // because we don't want the last sections's titles to remain in the listview we set it to clear by
        list_clear=1;

        // And we launch the thread to get the JSON for the
        launchthreadstogettitles(no, from, mTitle.toString());
        //We are already doing this in on create, no need to do it twice. Also in case of no internet
        // this line will cause the app to crash
    }
        else{
        	createNetErrorDialog();
           //populatelist();
        }


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
		if(result==null){
			return;
		}
		// stop loading gif
		load.setVisibility(View.GONE);
		//we have got a whole JSONArray, update the count
		nojson=nojson+NO_TITLES;


 		// I got the JSON! i just used a interface! Communication complete!
		if(list_clear==1){
			// reset the titles arraylist, as we are getting the latest titles from their server same with images and id, duh
			titles.clear();
			images.clear();
			id.clear();
			list_clear=0;
			}
		for(int i=0;i<no;i++){
			try {
				//Just add the titles to the titles array
				titles.add(result.getJSONObject(i).getString("title"));
				if(result.getJSONObject(i).getString("image") == null || result.getJSONObject(i).getString("image").isEmpty()){
					images.add("http://spider.nitt.edu/~adityap/resources/images/motorindia_site.png");
				}else{
					//fetch the image URL store it
					images.add(result.getJSONObject(i).getString("image"));
				}
				// get the ID for sending with the Intent
				id.add(result.getJSONObject(i).getInt("id"));


			} catch (JSONException e) {
				// nothing to here, *for now*
				e.printStackTrace();
			}
			}


		//populate the list with the fetched data
		populatelist();

			}

}
