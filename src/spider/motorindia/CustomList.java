package spider.motorindia;


import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class CustomList extends ArrayAdapter<String> implements OnItemClickListener{
private final Activity context;
private final String[] title;
private final Integer[] imageId;
private final String[] imageurl;

//constructor of this class, this called when we initilize the adapter
public CustomList(Activity context,String[] title, Integer[] imageId, String[] imageurl) {
super(context, R.layout.single_line, title);
this.context = context;
this.title = title;
this.imageId = imageId;
this.imageurl = imageurl;
}


@Override
public View getView(int position, View view, ViewGroup parent) {
	Log.i("debug","check");
LayoutInflater inflater = context.getLayoutInflater();
View rowView= inflater.inflate(R.layout.single_line, null, true);
TextView txtTitle = (TextView) rowView.findViewById(R.id.text);

if(imageurl==null){
	ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
	imageView.setImageResource(imageId[position]);
}
else{
	ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
	Picasso.with(context).load(imageurl[position]).placeholder(R.drawable.ic_launcher).error(R.drawable.error).resize(200, 150).into(imageView);
	
}
txtTitle.setText(title[position]);
return rowView;
}

@Override
public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
	// 
	Log.i("debug",v.toString());
}
	

}

