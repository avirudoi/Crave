package crave.com.android.bll;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.HashMap;

import crave.com.android.db.UserDataSource;
import crave.com.android.face.CallResponse;
import crave.com.android.face.OnOperationComplete;
import crave.com.android.model.NewUser;

/**
 * Created by avirudoi on 6/22/16.
 */
public enum  AuthManagerHelper {

    Instance;

    private NewUser loginUser = null;

    /* Data from the authenticated user */
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    public void initAutoManager(Context context){
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        BusinessManagerHelper.Instance.initBusinessManager(context);
        Log.d("aviTag", "uth got init");
    }

    public DatabaseReference returnReference(){
        return mDatabase;
    }

    public void SignUpUser(final Context context, final String email, final String password, final String fname, final OnOperationComplete response) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    public void onFailure(@NonNull Exception e) {
                        response.onOperationComplete(CallResponse.failed, e.getMessage());
                        return;
                    }
                });

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //success, save auth data
                        HashMap<String, Object> user = new HashMap<String, Object>();
                        user.put("uid", task.getResult().getUser().getUid());
                        user.put("email", task.getResult().getUser().getEmail());
                        user.put("fname", fname);
                        user.put("lname", "");
                        user.put("city", "");

                        mDatabase.child("users").child(task.getResult().getUser().getUid()).setValue(user);

                        NewUser Datauser = new NewUser();
                        Datauser.setUserId(task.getResult().getUser().getUid());
                        Datauser.setEmail(email);
                        Datauser.setFirstName(fname);
                        UserDataSource.Instance.CreateUser(Datauser);
                        loginUser = UserDataSource.Instance.getOneUser(task.getResult().getUser().getUid());

                        response.onOperationComplete(CallResponse.Success, null);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            response.onOperationComplete(CallResponse.failed, task.getException().getMessage());
                        }
                    }
                });

    }

    public NewUser getOneUser(){
        if(loginUser != null){
            return UserDataSource.Instance.getOneUser(loginUser.getUserId());
        }
        return null;
    }

    public void LogInUser(final Context context, String email, String password, final OnOperationComplete response) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("aviTag", "signInWithEmail:onComplete:" + task.isSuccessful());

                        loginUser = UserDataSource.Instance.getOneUser(task.getResult().getUser().getUid());
                        response.onOperationComplete(CallResponse.Success, null);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            response.onOperationComplete(CallResponse.failed, task.getException().getMessage());
                        }

                        // ...
                    }
                });
    }

    public void updateProfile(final Context context, final String email, final String fname, final String lname, final OnOperationComplete response) {


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, Object> authMap = new HashMap<String, Object>();
        authMap.put("email", email);
        authMap.put("fname", fname);
        authMap.put("lname", lname);


        mDatabase.child("users").child(user.getUid()).updateChildren(authMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                NewUser dataUser = new NewUser();
                dataUser.setUserId(user.getUid());
                dataUser.setEmail(email);
                dataUser.setFirstName(fname);
                dataUser.setLastName(lname);
                UserDataSource.Instance.updateOneUser(dataUser);
                loginUser = UserDataSource.Instance.getOneUser(user.getUid());
                response.onOperationComplete(CallResponse.Success,null);
            }
        });

    }

    public void changePassword(final Context context, final String newPassword, final OnOperationComplete response) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            response.onOperationComplete(CallResponse.Success,null);
                        }else {
                            response.onOperationComplete(CallResponse.failed, task.getException().getMessage());
                        }
                    }
                });

    }

    public void forgotPassword(Context context, String email, final OnOperationComplete response) {

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            response.onOperationComplete(CallResponse.Success,null);
                        }else {
                            response.onOperationComplete(CallResponse.failed,task.getException().getMessage());
                        }
                    }
                });

    }

    public void facebookLogin(final Context context, LoginButton loginButton, CallbackManager callbackManager, final OnOperationComplete reponse){
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFacebookMeInfo(context,loginResult.getAccessToken(),reponse);

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    protected void getFacebookMeInfo(final Context context, final AccessToken accessToken, final OnOperationComplete reponse) {
        if(accessToken == null) {
            Log.i("FacebookTag", "Facebook accessToken is null");
            FacebookException exception = new FacebookException("accessToken is null");
            onFacebookLoginError(exception);
        }
        else {
            final AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject json, GraphResponse response) {
                            Toast.makeText(context,"successfully Login with Facebook", Toast.LENGTH_LONG).show();
                            onFacebookAccessTokenChange(context,credential,accessToken, json, response,reponse);

                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,gender,first_name,last_name, location, birthday,email, locale");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    private void onFacebookAccessTokenChange(final Context context, AuthCredential credential, AccessToken token, JSONObject json, GraphResponse response, final OnOperationComplete reponse) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("aviTag", "signInWithCredential:onComplete:" + task.isSuccessful());



                        if(UserDataSource.Instance.getOneUser(task.getResult().getUser().getUid()) == null){

                            //success, save auth data
                            HashMap<String, Object> user = new HashMap<String, Object>();
                            user.put("uid", task.getResult().getUser().getUid());
                            user.put("email", task.getResult().getUser().getEmail());
                            user.put("DisplayName", task.getResult().getUser().getDisplayName());
                            user.put("ProfileImage", task.getResult().getUser().getPhotoUrl());
                            user.put("lname", "");
                            user.put("city", "");

                            mDatabase.child("users").child(task.getResult().getUser().getUid()).setValue(user);

                            NewUser DataUser = new NewUser();
                            DataUser.setUserId(task.getResult().getUser().getUid());
                            DataUser.setEmail(task.getResult().getUser().getEmail());
                            DataUser.setDisplayName(task.getResult().getUser().getDisplayName());
                            DataUser.setPictureUrl(task.getResult().getUser().getPhotoUrl().toString());
                            UserDataSource.Instance.CreateUser(DataUser);
                        }

                        loginUser = UserDataSource.Instance.getOneUser(task.getResult().getUser().getUid());

                        reponse.onOperationComplete(CallResponse.Success,null);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("aviTag", "signInWithCredential", task.getException());
                            reponse.onOperationComplete(CallResponse.failed, task.getException().getMessage());

                        }
                        // ...
                    }
                });
    }

    public void unUothorizeFireBase(Context context){

        mAuth.getInstance().signOut();
    }

    private void onFacebookLoginCancelled() {
        Log.i("FacebookTag", "Warning: onFacebookLoginCancelled has not been overridden by derived class!");
    }

    private void onFacebookLoginError(FacebookException error) {
        Log.i("FacebookTag", "Warning: onFacebookLoginError has not been overridden by derived class!");
    }

    private void onFacebookLoginSuccess2(AccessToken accessToken, JSONObject json, GraphResponse response) {
        Log.i("FacebookTag", "Warning: onFacebookLoginSuccess has not been overridden by derived class!");
    }
}
