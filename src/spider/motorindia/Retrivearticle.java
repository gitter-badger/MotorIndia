package spider.motorindia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;



import android.os.AsyncTask;

public class Retrivearticle extends AsyncTask<String, Void, JSONObject> {
	
	// variable used to send the data back
	private MyCallbackInterface mCallback;

	public Retrivearticle(MyCallbackInterface callback) {
		// this is the contructor for this class
		mCallback = callback;
	}

	@Override
	protected JSONObject doInBackground(String... arg0) {
		// this is where the actual asynchronous "work" happens
		
		// the first and only string is the url - i hope
		String url = arg0[0];
		
		// make this client, httpget and response, we will use them later
		HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet(url);
	    HttpResponse response;
	    // and the result where we will have the JSON as a string
	    String result = null;
	    
	    //use the client to send the get request store the response in duh response
	    try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// A Simple JSON Response Read
	            InputStream instream = entity.getContent();
	            result = convertStreamToString(instream);
	            // now you have the string representation of the HTML request in result so,
	            JSONObject myObject;
				try {
					myObject = new JSONObject(result);
					return myObject;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            
			}

	            
	            
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// If all fails then
		return null;
	}
	    
	    private static String convertStreamToString(InputStream is) {

	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();

	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }
	
	//i define the callback interface for sending data back when we get it.
    public interface MyCallbackInterface {
    	//its supposed to send the JSON object on request completed
        public void onRequestCompleted(JSONObject result);
    }
    
    @Override
    protected void onPostExecute(JSONObject result) {
        //So that i have the JSON ready to use, i use the call back to send it to the 'displayactivity.java' ;)
        mCallback.onRequestCompleted(result);
    }

}
