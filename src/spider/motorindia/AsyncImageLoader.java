package spider.motorindia;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class AsyncImageLoader extends AsyncTask<String, Integer, Bitmap> {

	private static String network_response;
	private static boolean doAcceptAllSSL = false, doUseCookie = false;
	private onImageLoaderListener mOnImageLoaderListener;
	//private int progress;
	//private onProgressUpdateListener mOnProgressUpdateListener;
	private Bitmap bmpResult;

	//Constructor
	public AsyncImageLoader(onImageLoaderListener mOnImageLoaderListener) {
		this.mOnImageLoaderListener = mOnImageLoaderListener;
		//this.mOnProgressUpdateListener = mOnProgressUpdateListener;
	}

	/**
	 * This is our interface that listens for image download completion.
	 */
	public interface onImageLoaderListener {
		/**
		 * This callback will be invoked when the image has finished
		 * downloading.
		 * 
		 * @param image
		 *            the image as Bitmap object or null in case of an error
		 * @param response
		 *            the network response
		 */
		void onImageLoaded(Bitmap image, String response);
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		bmpResult = downloadImage(params[0]);
		return bmpResult;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		/**
		 * called on the UI thread
		 */
		if (mOnImageLoaderListener != null) {
			mOnImageLoaderListener.onImageLoaded(result, network_response);
		}
	}

	public static Bitmap downloadImage(String url) {

		HttpParams hparams = new BasicHttpParams();
		/**
		 * You can also add timeouts to the settings menu in a real project
		 */
		HttpConnectionParams.setConnectionTimeout(hparams, 10000);
		HttpConnectionParams.setSoTimeout(hparams, 10000);
		HttpGet get = new HttpGet(url);
		DefaultHttpClient client;
		try {
			if (doAcceptAllSSL)
				client = (DefaultHttpClient) SSLErrorPreventer
						.setAcceptAllSSL(new DefaultHttpClient(hparams));
			else
				client = new DefaultHttpClient(hparams);
			HttpResponse response = null;
			if (doUseCookie) {
				CookieStore store = client.getCookieStore();
				HttpContext ctx = new BasicHttpContext();
				store.addCookie(Utils.sessionCookie);
				ctx.setAttribute(ClientContext.COOKIE_STORE, store);
			}
			response = client.execute(get);
			network_response = response.getStatusLine().toString();
			Home.tmpResponseForUIDownload = network_response;
			HttpEntity responseEntity = response.getEntity();
			BufferedHttpEntity httpEntity = new BufferedHttpEntity(
					responseEntity);
			InputStream imageStream = httpEntity.getContent();

			return BitmapFactory.decodeStream(imageStream);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
