package crave.com.android.bll;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import crave.com.android.model.LocalBusinesses;

/**
 * Created by avirudoi on 6/22/16.
 */
public enum BusinessManagerHelper {

    Instance;

    private LocalBusinesses localBusinesses = null;

    DatabaseReference mDataBase;



    public void initBusinessManager(Context context){
        mDataBase= AuthManagerHelper.Instance.returnReference().child("places").child("Food");
        Log.i("aviTag","data base instance");
    }


    public void getLocalDataBase(final Context context) {

        mDataBase.keepSynced(true);
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.w("aviTag", "going inside get bussnies");
                ArrayList<LocalBusinesses> itemsList = new ArrayList<LocalBusinesses>();


                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    postSnapshot.getValue();
                    LocalBusinesses localBusinesses = postSnapshot.getValue(LocalBusinesses.class);

                    itemsList.add(localBusinesses);
                }
                Log.w("aviTag", "going inside" + itemsList.size());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("aviTag", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

}
