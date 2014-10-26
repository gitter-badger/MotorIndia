package spider.motorindia;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class CustomList extends ArrayAdapter<String>{
private final Activity context;
private final String[] title;
private final Integer[] imageId;

//constructor of this class, this called when we initilize the adapter
public CustomList(Activity context,String[] title, Integer[] imageId) {
super(context, R.layout.single_line, title);
this.context = context;
this.title = title;
this.imageId = imageId;
}


@Override
public View getView(int position, View view, ViewGroup parent) {
LayoutInflater inflater = context.getLayoutInflater();
View rowView= inflater.inflate(R.layout.single_line, null, true);
TextView txtTitle = (TextView) rowView.findViewById(R.id.text);
ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
txtTitle.setText(title[position]);
imageView.setImageResource(imageId[position]);
return rowView;
}

}