package crave.com.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import crave.com.android.R;
import crave.com.android.helper.PicassoHelper;
import crave.com.android.model.Businesses;

/**
 * Created by avirudoi on 1/31/16.
 */
public class CustomListViewAdapter extends ArrayAdapter<Businesses> {

    Context context;

    public CustomListViewAdapter(Context context, int resource, List<Businesses> object) {
        super(context, resource, object);

        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView, ivRatingImage;
        TextView tvTitle, tvDistance,tvIsOpen;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Businesses object = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.tvIsOpen = (TextView) convertView.findViewById(R.id.tvIsOpen);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
            holder.imageView = (ImageView) convertView.findViewById(R.id.ivplaceImage);
            holder.ivRatingImage = (ImageView) convertView.findViewById(R.id.ivRatingImage);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

       holder.tvTitle.setText(object.getTitle());
       holder.tvDistance.setText(object.getDistance() + " mi.");

        if(object.is_closed() == true){
            holder.tvIsOpen.setVisibility(View.VISIBLE);
        }else{
            holder.tvIsOpen.setVisibility(View.GONE);
        }

        if(object.getImageUrl() != null){
            PicassoHelper.Instance.displayMainImage(object.getHighResImage(),context,holder.imageView);
        }

        PicassoHelper.Instance.displayRatingImage(object.getRatingImgUrl(),context,holder.ivRatingImage);

        return convertView;
    }


}

