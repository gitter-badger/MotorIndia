package spider.motorindia;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//THIS class is the one that defines the custom adapter
public class CustomList extends ArrayAdapter<String>{
	// the variables associated with the adapter
	private final Activity context;
	private final String[] web;
	private final Integer[] imageId;

	//constructor which sets the adapter 
	public CustomList(Activity context,String[] web, Integer[] imageId) {
	super(context, R.layout.single_line, web);
	this.context = context;
	this.web = web;
	this.imageId = imageId;
	}
	//this is called when the list is drawn up, sets the list line by line
	@Override
	public View getView(int position, View view, ViewGroup parent) {
			
	LayoutInflater inflater = context.getLayoutInflater();
	//TODO LOOK INTO THIS ?!? read this - http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
	View rowView= inflater.inflate(R.layout.single_line, null, true);
	TextView txtTitle = (TextView) rowView.findViewById(R.id.text);
	ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
	txtTitle.setText(web[position]);
	imageView.setImageResource(imageId[position]);
	
	return rowView;
	
	}

}
	


