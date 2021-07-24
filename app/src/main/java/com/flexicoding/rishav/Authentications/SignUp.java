package com.flexicoding.rishav.Authentications;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.flexicoding.rishav.MainActivity;
import com.flexicoding.rishav.R;
import com.flexicoding.rishav.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

public class SignUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;

    private FirebaseAuth mAuth;

    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        //initialize Firebase
        initializeFirebase();


        //configure the google sign in
        configureGoogleSignIn();

        //check if user is Logged in or not
        checkLogin();

        binding.googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleSignInOption();
            }
        });
        
    }


    /**
     * Check if user is logged in or not*/
    private void checkLogin() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null){
            loginToMainActivity();
        }else{
            backToSignIn();
        }
    }

    /**
     * Configure Google Sign In*/
    private void configureGoogleSignIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    /**
     * Initialize Firebase*/
    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Open Google sign in screen*/
    private void openGoogleSignInOption(){
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    /**On Activity Result of Google sign in*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned after google sign in activity option chosen
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            }
            catch (Exception e){
                //failed to sign in
            }
        }

    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                getUserDetails(authResult);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
    }

    /**
     * After sucessfully choosing account checking if user exists or not and accordingly*/
    private void getUserDetails(AuthResult authResult) {
        //Get  user details
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        //user info

        //if user is new or already existing
        if(authResult.getAdditionalUserInfo().isNewUser()){
            //new user
        }else{
            //logged in successful user
        }
        //login user to main Activity
        loginToMainActivity();
    }

    /**
     * Login user to Main activity*/
    private void loginToMainActivity() {
    }

    private void backToSignIn() {}




}