package spider.motorindia;

import org.json.JSONException;
import org.json.JSONObject;

import spider.motorindia.Retrivearticle.MyCallbackInterface;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Displayarticle extends Activity implements MyCallbackInterface {
	
	//Default VALUES TO MAKE THE LAYOUT OK
	//String title = "Volvo to expand in Africa with vocational training schools";
	//String body = "Together with United States Agency for International Aid (USAID), The United Nations Industrial Development Organization (UNIDO), the OCP Foundation and the Moroccan Ministry of national education and vocational training, the Volvo Group is starting a vocational training school for mechanics in Morocco. The school will be conducted in collaboration with local authorities and will train 150 students from Morocco, the Ivory Coast and Senegal every year. “Trained mechanics will have the opportunity to gain work in countries with high unemployment, while Volvo will gain access to the trained personnel that is required in order to expand in Africa. By training local manpower, we will contribute to sustainable growth in the countries in which Volvo operates,” says Mr. Niklas Gustavsson, Volvo Group Executive Vice President, Corporate Sustainability & Public Affairs. The Swedish OEM’s presence in Morocco dates back to the 50s. Its high share of the truck market in combination with the country’s investments in infrastructure makes Morocco a country where the Volvo Group can grow. Morocco is a country with high unemployment among young people and where the existing education system places focus on theoretical education, which does not reflect the needs of the industry. The consequence is that the shortage of adequate competency is impeding the growth of the country. “The distinctive feature of this training academy lies within its ability to produce skills and expertise that can directly be employed in the economic sectors that use heavy duty equipment and that work on the big projects that are undertaken by Morocco”, says Mr. Jamaleddine El Aloua, General Secretary for the Moroccan Department of Vocational Training.The trainee programs will commence in 2015 and the operation will be conducted on the same premises as the existing national vocational school Ecole des Métiers du Bâtiment et Travaux Publics, in the city of Settat. Last year, the Swedish major announced that vocational training schools for mechanics and drivers for trucks, buses and construction equipment will be established in ten African countries. In collaboration with national education authorities, the group will develop and finance the programs based on the industry’s local competency requirements. The programs will be conducted in the countries that are strategically important in terms of business for the Volvo Group, and also within the strategies for aid from SIDA and USAID.";
	
	String title = "LOADING...";
	String body = "Please wait..  the articles are being fetched..";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_displayarticle);
	
		//handle the incoming INTENT
		Intent intent = getIntent();
		int message = intent.getIntExtra(Home.EXTRA_MESSAGE,26479);
		
		new Retrivearticle(this).execute("http://www.motorindiaonline.in/mobapp/?a_id="+Integer.toString(message));
		
		TextView t = (TextView)findViewById(R.id.textView1);
		t.setText(title);
		TextView b = (TextView)findViewById(R.id.textView2);
		b.setText(body);
		b.setMovementMethod(new ScrollingMovementMethod());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.displayarticle, menu);
		return true;
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

	@Override
	public void onRequestCompleted(JSONObject result) {
		// SO here we get the Json object = result
		try {
			title=result.getString("title");
			body=result.getString("content");
		} catch (JSONException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		TextView t = (TextView)findViewById(R.id.textView1);
		t.setText(title);
		TextView b = (TextView)findViewById(R.id.textView2);
		b.setText(body);
		
		
	}
}
