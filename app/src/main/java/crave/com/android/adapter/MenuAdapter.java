package crave.com.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import crave.com.android.R;
import crave.com.android.fragment.MenuPageFragment;
import crave.com.android.model.Contents;
import crave.com.android.model.Menu;
import crave.com.android.model.Sections;


public class MenuAdapter extends BaseAdapter {

    HashMap<String,ArrayList<Object>> menuListFinal;
    ArrayList<Contents> menuDetails;
    private Context context;

    public static final String SECTION_TEXT  = "SECTION_TEXT";
    public static final String ITEM  = "ITEM";

    Menu menu;
    Sections sections;
    Contents contents;





    public MenuAdapter(Context c, HashMap<String,ArrayList<Object>> menulist){

        context = c;
        menuListFinal = menulist;

    }

    private class ViewHolder{

        TextView tvMenuName, tvTypeSection, tvName, tvPrice, tvDescription, tvSpecial;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.menu_adapter,
                    null);

            bindView(convertView, holder);


            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        displayData(holder,position);

        return convertView;
    }

    public void displayData(ViewHolder holder, int row){




        if(menuListFinal.get(MenuPageFragment.TYPE_MENU).size() >row){
            menu = (Menu) menuListFinal.get(MenuPageFragment.TYPE_MENU).get(row);
            if(!menu.getMenuName().equals("") && !menu.getMenuName().equalsIgnoreCase("menu")){
                holder.tvMenuName.setVisibility(View.VISIBLE);
                holder.tvMenuName.setText(menu.getMenuName());
            }else{
                holder.tvMenuName.setVisibility(View.GONE);
            }
        }

        if(menuListFinal.get(MenuPageFragment.TYPE_SECTION).size() >row){
            sections = (Sections) menuListFinal.get(MenuPageFragment.TYPE_SECTION).get(row);
            if(!sections.getSectionName().equals("")){
                holder.tvTypeSection.setVisibility(View.VISIBLE);
                holder.tvTypeSection.setText(sections.getSectionName());
            }else{
                holder.tvTypeSection.setVisibility(View.GONE);
            }
        }

        if(menuListFinal.get(MenuPageFragment.TYPE_NAME).size() >row){

            contents = (Contents) menuListFinal.get(MenuPageFragment.TYPE_NAME).get(row);

            //menu name
            String type = contents.getType();
            String text = contents.getText();
            String price =contents.getPrice();
            String name = contents.getName();
            String description = contents.getDescription();


            if (type != null && type.equals(SECTION_TEXT) && text != null && !text.equals("")) {
                holder.tvSpecial.setVisibility(View.VISIBLE);
                holder.tvSpecial.setText(text);
            }else{
                holder.tvSpecial.setVisibility(View.GONE);
            }
            if (name != null && !name.equals("")) {
                holder.tvName.setVisibility(View.VISIBLE);
                holder.tvName.setText(name);
            }else{
                holder.tvName.setVisibility(View.GONE);
            }
            if (price != null && !price.equals("")) {
                holder.tvPrice.setVisibility(View.VISIBLE);
                holder.tvPrice.setText(price + "$");
            }else{
                holder.tvPrice.setVisibility(View.GONE);
            }
            if (description != null && !description.equals("")) {
                holder.tvDescription.setVisibility(View.VISIBLE);
                holder.tvDescription.setText(description);
            }else{
                holder.tvDescription.setVisibility(View.GONE);
            }
        }


    }

    public void bindView(View convertView, ViewHolder holder){
        holder.tvMenuName = (TextView) convertView
                .findViewById(R.id.tvMenuName);
        holder.tvTypeSection = (TextView) convertView
                .findViewById(R.id.tvTypeSection);
        holder.tvName = (TextView) convertView
                .findViewById(R.id.tvName);
        holder.tvPrice = (TextView) convertView
                .findViewById(R.id.tvPrice);
        holder.tvDescription = (TextView) convertView
                .findViewById(R.id.tvDescription);
        holder.tvSpecial = (TextView) convertView
                .findViewById(R.id.tvSpecial);
    }

    @Override
    public int getCount() {
        return menuListFinal.get(MenuPageFragment.TYPE_MENU).size() + menuListFinal.get(MenuPageFragment.TYPE_SECTION).size() + menuListFinal.get(MenuPageFragment.TYPE_NAME).size();
    }

    @Override
    public Object getItem(int position) {
        return menuListFinal.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


}
