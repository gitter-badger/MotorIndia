package spider.motorindia;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

//this import establishes the link between this class and the "Retrivejson" class
import spider.motorindia.Retrivejson.MyCallbackInterface;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
	UPDATE these values with the latest 10 titles everytime Internet connection is established
	 */

	String[] savedtitles = {
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
		  };

		  //Due to the requirement for dynamic lists, ie add titles as we go down we need to
		  ArrayList<String> titles = new ArrayList<String>();



	//Global variables
		  //ListView for the list in
		  ListView list;
		  //Flag variable for keeping track whether the action bar is being drawn up for the First time
		  int first_time=1;
		  //the variable which keeps track of how many titles have been fetched from the Internet
		  int i=0;
		  // placeholder string array used to get the adapter
		  String[] titlearray;
		  // values which determine the number of threads which are launched and how frequently - it refers to article numbers
		  int till=11,from=1;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


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

        // this is PART OF THE WORKAROUND (check out the TODOs)
        // we need to make sure that even if the screen orientation changes the workaround integer variable is set back to 0
        //in order to avoid the printing that a click has taken place.
        //first_time=1;

        if(isNetworkConnected()){
        	// launch threads to get 10 titles from latest article
            launchthreadstogettitles(till,from);
            toast("Internet available, fetching latest articles");
        }
        else{
        	toast("NO internet connection");
        }
        
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
    //a function to get the next 10 articles
    public void update(View v){
    	if(!isNetworkConnected()){
    		toast("Fetching of articles Failed! No internet. ");
    		return;
    	}
    	toast("Fetching next 10 articles");
    	//start from 11th latest article when this is first called, then for subsequent calls
    	from=till;
    	// and get 10 articles from "till"
    	till=from+10;
    	//And start the threads
    	launchthreadstogettitles(till,from);
    }

    //a function to populate the list with titles from titles (as of now)
    public void populatelist(){
    	if(isNetworkConnected()){
    	   	//this sets up the dynamic array which is send to the adapter's constructor
            titlearray = titles.toArray(new String[titles.size()]);
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
         	// set "list" the handle, pointing to the respective views
            list=(ListView)findViewById(R.id.listView1);
            list.setAdapter(adapter);
    	}
    	//As the first run is over
        first_time=0;
    }

    //a function for toast
    public void toast(String display){
    	Toast.makeText(Home.this,display, Toast.LENGTH_LONG).show();
    }

    //the function which starts the threads which in turn get the titles and set them as soon as we get them in the onrequestcompleted function
    public void launchthreadstogettitles(int number, int start){
    	for(int i=start;i<number;i++){
			String link = "http://motorindiaonline.in/android/?s_i="+Integer.toString(i)+"&e_i="+Integer.toString(i);
			new Retrivejson(this).execute(link);
		}
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
        	toast(getString(R.string.title_section1));
        	//as the application initializes the action bar it checks which title is currently is on and due to the workaround alerts the user
        	//that a click has been registered
        }
        else if(mTitle==getString(R.string.title_section2)){
        	toast(getString(R.string.title_section2));
        }
        else if(mTitle==getString(R.string.title_section3)){
        	toast(getString(R.string.title_section3));
        }
        else if(mTitle==getString(R.string.title_section4)){
        	toast(getString(R.string.title_section4));
        }
        else if(mTitle==getString(R.string.title_section5)){
        	toast(getString(R.string.title_section5));
        }
        else if(mTitle==getString(R.string.title_section6)){
        	toast(getString(R.string.title_section6));
        }
        else if(mTitle==getString(R.string.title_section7)){
        	toast(getString(R.string.title_section7));
        }
        //set "list" the handle, pointing to the respective views
        list=(ListView)findViewById(R.id.listView1);
        //TODO, find out WHY!!! passing null as the parent works.. i need to remove this link error OR explain it away
        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        list.addFooterView(footerView);

        //as right now we have a single list for all these cases we call the function populatelist() (Right now no arguments because of that)
        //we only need to populate it once (on create the flag variable workaround is set thus using it we only call it the first time
        // the actionbar is drawn
        populatelist();
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
	public void onRequestCompleted(JSONObject result) {
		//As atleast one title has loaded, we can change the footer's text
		TextView footer =(TextView)findViewById(R.id.footer_1);
		footer.setText("Fetch More Articles");
		// I got the JSON! i just used a interface! Communication complete!
				if(result==null){
					Log.i("debug","Json object's value is null");
				}
				else{
					try {
						if(first_time==1){
							titles.clear();
						}
						//Just add the title to the titles array
						titles.add(result.getString("title"));
						//populate the list
				        populatelist();
					} catch (JSONException e) {
						// thats all folks
						e.printStackTrace();
						Log.i("debug","it aint null but its a JSONException");
					}
				}

			}

}


