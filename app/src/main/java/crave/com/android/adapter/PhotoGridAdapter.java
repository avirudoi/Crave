package crave.com.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import crave.com.android.R;
import crave.com.android.helper.GlideHelper;
import crave.com.android.helper.HelperMethod;
import crave.com.android.model.Businesses;

public class PhotoGridAdapter extends BaseAdapter {


    private Context context;

    private List<Businesses> business ;

    private double stringLatitude, stringLongitude;

    public PhotoGridAdapter(Context c, List<Businesses> temp, double myLat, double myLong) {

        context = c;
        business = temp;
        stringLatitude = myLat;
        stringLongitude = myLong;

    }

    public long getItemId(int position) {
        return position;
    }


    //returns the number of images
    @Override
    public int getCount() {
        return business.size();
    }



    @Override
    public Object getItem(int position) {
        return business.get(position);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;



        if (row == null) {
            LayoutInflater infalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = infalter.inflate(R.layout.gallery_photo, null);
            holder = new ViewHolder();
            holder.ivGalleryPhoto = (ImageView) row.findViewById(R.id.ivGalleryPhoto);
            holder.tvTitle = (TextView) row.findViewById(R.id.tvTitle);
            holder.tvDistance = (TextView) row.findViewById(R.id.tvDistance);

            row.setTag(holder);

        } else {

            holder = (ViewHolder) row.getTag();
        }


        if(holder.ivGalleryPhoto != null) {

            String imagePath = business.get(position).getHighResImage();

            GlideHelper.Instance.displayImageGallery(imagePath, context, holder.ivGalleryPhoto);

            holder.tvTitle.setText(business.get(position).getTitle());

           holder.tvDistance.setText(HelperMethod.Instance.returnDistance(stringLatitude,stringLongitude,business.get(position).getLatitude(),business.get(position).getLongitude()) + " mi.");


        }

        return row;
    }

    static class ViewHolder {

        ImageView ivGalleryPhoto;

        TextView tvTitle, tvDistance;

    }
}
