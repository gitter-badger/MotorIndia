package spider.motorindia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;

public class Retrivejson extends AsyncTask<String, Void, JSONArray> {

	//i define the callback interface
    public interface MyCallbackInterface {
    	//its supposed to send the JSON object on request completed
        public void onRequestCompleted(JSONArray result);
    }

    private MyCallbackInterface mCallback;

    public Retrivejson(MyCallbackInterface callback) {
        mCallback = callback;
    }

    public JSONArray getJSONFromUrl(String url) {

    	HttpClient client = new DefaultHttpClient();

	    HttpGet httpGet = new HttpGet(url);

	    HttpResponse response;

			try {
				//GET THE RESPONSE FROM THE GET REQUEST
				response = client.execute(httpGet);
				// MAKE A BUFFERREADER AND STORE THE RESPONSE IN IT
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
				//INITIALIZE THE BUILDER IN ORDER TO STORE EACH JSON OBJ
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
				    builder.append(line).append("\n");
				}
				JSONTokener tokener = new JSONTokener(builder.toString());
				// NOW FINAL result holds the JSONArray
				JSONArray finalResult = new JSONArray(tokener);

				//Log.i("debug",finalResult.getJSONObject(0).toString());
				//using .getJSONObject(0) we get the first JSON object
				//and we return this JSONObject, ie the title of the article
				return finalResult;



			} catch (IOException e) {
				// Can't do more than the Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// Can't do more than the Auto-generated catch block
				e.printStackTrace();
			}

		return null;
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        String url = params[0];
        return getJSONFromUrl(url);
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        //So that i have the JSON ready to use, i use the call back to send it to the 'customlist.java' ;)
        mCallback.onRequestCompleted(result);
    }
}



