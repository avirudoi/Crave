package crave.com.android.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import crave.com.android.R;
import crave.com.android.adapter.MenuAdapter;
import crave.com.android.face.MainPlaceInterface;
import crave.com.android.model.ResultMenuModel;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by avirudoi on 2/22/16.
 */
public class MenuPageFragment extends Fragment {

    MainPlaceInterface mainPlaceInterface;
    ImageView ivplaceImage;
    ImageButton ibCall, ibDirections, ibFavorites;
    String mPlaceId;

    private static final String ENDPOINT = "https://api.locu.com";
    private static final String SEARCH_PATH = "/v1_0/venue/search/";
    public static final String API_KEY  = "c04f4ff7ccc91b8634a22d90b468379165a4dc0a";

    HashMap<String,ArrayList<Object>> menuListFinal;

    public static final String TYPE_MENU = "menuName";
    public static final String TYPE_SECTION = "sectionName";

    MenuAdapter menuAdapter;

    private ActionBar actionBar;

    List<Object> menuList;

    ArrayList<Object> menuName;
    ArrayList<Object> sectionType;
    ArrayList<Object> typeName;

    private ProgressBar progressBar;

    ListView lvResPage;

    public static final String TYPE_NAME = "typeName";

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);


        if (context instanceof MainPlaceInterface){
            mainPlaceInterface=(MainPlaceInterface) context;
        }
    }

    /*
* Deprecated on API 23
* Use onAttachToContext instead
*/
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        if (activity instanceof MainPlaceInterface){
            mainPlaceInterface=(MainPlaceInterface) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //actionBar = getActivity().getActionBar();



        Bundle extras = getArguments();
        if (extras != null) {
            mPlaceId = (String) extras.get("placeId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.menu_page_fragment, container,
                false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Menu");
        setHasOptionsMenu(true);

        bindWidgetView(rootView, mPlaceId);


        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mainPlaceInterface.replacePlacePagesMainFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





    //data call object
    private void fetchPlaceMenu(String id){
        RestAdapter.Builder adapterBuilder = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ENDPOINT).setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("api_key", API_KEY);
                    }
                });

        GetPlaceMenu service = adapterBuilder.build().create(GetPlaceMenu.class);
        service.getLucaPlaceId(id, new Callback<ResultMenuModel>() {
            @Override
            public void success(ResultMenuModel resultMenuModel, Response response) {
//                    Toast.makeText(getActivity(), "success menu", Toast.LENGTH_LONG).show();
                Log.i("avitag", "get to the menu ");
                if(menuListFinal == null){
                    menuListFinal = new HashMap<String, ArrayList<Object>>();
                }

                if(resultMenuModel.getObjects()[0] != null){

                    if(menuList == null){
                        menuList = new ArrayList<Object>();
                    }

                    for(int k=0; k<resultMenuModel.getObjects()[0].getMenus().length ; k++){

                        //check for menu name
                        if(resultMenuModel.getObjects()[0].getMenus()[k].getMenuName() != null && !TextUtils.isEmpty(resultMenuModel.getObjects()[0].getMenus()[k].getMenuName())) {

                            if(menuName == null){
                                menuName = new ArrayList<Object>();
                            }

                            menuName.add(resultMenuModel.getObjects()[0].getMenus()[k]);
                            menuListFinal.put(TYPE_MENU, menuName);
                            menuList.add(resultMenuModel.getObjects()[0].getMenus()[k]);
                        }

                        //get sections
                        if(resultMenuModel.getObjects()[0].getMenus()[k].getSections() != null){
                            for(int j = 0; j< resultMenuModel.getObjects()[0].getMenus()[k].getSections().length ; j++){

                                //check for section name
                                if(resultMenuModel.getObjects()[0].getMenus()[k].getSections()[j].getSectionName() != null && !TextUtils.isEmpty(resultMenuModel.getObjects()[0].getMenus()[k].getSections()[j].getSectionName())) {

                                    if(sectionType == null){
                                        sectionType = new ArrayList<Object>();
                                    }

                                    sectionType.add(resultMenuModel.getObjects()[0].getMenus()[k].getSections()[j]);
                                    menuListFinal.put(TYPE_SECTION, sectionType);
                                    menuList.add(resultMenuModel.getObjects()[0].getMenus()[k].getSections()[j]);
                                }

                                //check for sub section
                                if(resultMenuModel.getObjects()[0].getMenus()[k].getSections()[j].getSubsections() != null){

                                    for(int y = 0; y<resultMenuModel.getObjects()[0].getMenus()[k].getSections()[j].getSubsections().length; y++){

                                        //check if the contect not null
                                        if(resultMenuModel.getObjects()[0].getMenus()[k].getSections()[j].getSubsections()[y].getContents() != null){

                                            for(int g = 0; g <resultMenuModel.getObjects()[0].getMenus()[k].getSections()[j].getSubsections()[y].getContents().length; g ++){

                                                if(typeName == null){
                                                    typeName = new ArrayList<Object>();
                                                }

                                                typeName.add(resultMenuModel.getObjects()[0].getMenus()[k].getSections()[j].getSubsections()[y].getContents()[g]);
                                                menuListFinal.put(TYPE_NAME, typeName);
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }

                    menuAdapter = new MenuAdapter(getActivity(),menuListFinal);
                    lvResPage.setAdapter(menuAdapter);
                }

                  /*  if(resultMenuModel.getObjects()[0] != null){
                        for(int k=0; k<resultMenuModel.getObjects()[0].getMenus().length ; k++){
                            menuList.add(resultMenuModel.getObjects()[0].getMenus()[k]);
                        }
                    }*/
                //menuAdapter = new MenuAdapter(getActivity(),menuList);
/*                    lvResPage.setAdapter(new SectionAdapter() {
                        @Override
                        public int numberOfSections() {
                            return 4;
                        }

                        @Override
                        public int numberOfRows(int section) {
                            return menuListFinal.get(TYPE_DESCRIPTION).size();
                        }



                        @Override
                        public boolean hasSectionHeaderView(int section) {
                            return true;
                        }

                        @Override
                        public int getSectionHeaderViewTypeCount() {
                            return 4;
                        }

                        @Override
                        public int getSectionHeaderItemViewType(int section) {
                            return 4;
                        }


                        @Override
                        public View getRowView(int section, int row, View convertView, ViewGroup parent) {

                            if (convertView == null) {
                                convertView = (TextView) getActivity().getLayoutInflater().inflate(getResources().getLayout(android.R.layout.simple_list_item_1), null);
                            }

                                ((TextView) convertView).setText(menuListFinal.get(TYPE_DESCRIPTION).get(row));


                            return convertView;
                        }

                        @Override
                        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

                            if (convertView == null) {
                                if (getSectionHeaderItemViewType(section) == 0) {
                                    convertView = (TextView) getActivity().getLayoutInflater().inflate(getResources().getLayout(android.R.layout.simple_list_item_1), null);
                                } else {
                                    convertView = getActivity().getLayoutInflater().inflate(getResources().getLayout(android.R.layout.simple_list_item_2), null);
                                }
                            }

                            if (getSectionHeaderItemViewType(section) == 0) {
                                    ((TextView) convertView).setText(menuListFinal.get(TYPE_MENU).get(section));


                            } else {
                                    ((TextView) convertView.findViewById(android.R.id.text1)).setText(menuListFinal.get(TYPE_SECTION).get(section));
                                    ((TextView) convertView.findViewById(android.R.id.text2)).setText(menuListFinal.get(TYPE_NAME).get(section));



                            }
                            return convertView;
                        }



                        @Override
                        public Object getRowItem(int section, int row) {
                            return null;
                        }

                    });*/
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "failed menu", Toast.LENGTH_LONG).show();
            }
        });
    }


    public  void bindWidgetView(View view, String placeId){
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        lvResPage = (ListView) view.findViewById(R.id.lvResPage);
        fetchPlaceMenu(placeId);
    }


    public interface GetPlaceMenu{
        @GET("/v1_0/venue/{id}/")
        void getLucaPlaceId(@Path("id") String id, Callback<ResultMenuModel> Response);
    }
}
