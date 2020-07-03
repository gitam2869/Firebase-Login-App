package com.example.firebaseloginapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;

import java.util.Arrays;

//facebook


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "MainActivity";
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextView textViewForgotPassword;
    private MaterialButton materialButtonLogin;
    private MaterialButton materialButtonGmail;
    private MaterialButton materialButtonFacebook;
    private MaterialButton materialButtonGithub;
    private TextView textViewCreateAnAccount;

    private ProgressDialog progressDialog;

    public Toast toast;

    FirebaseAuth firebaseAuth;

    static Activity fa;

//    sign in with google
    GoogleSignInClient googleSignInClient;
    private static final int RESULT_CODE_SINGIN = 121;


//    facebook
    private LoginButton loginButton;
    private CallbackManager callbackManager;


//    sign in with github
    OAuthProvider.Builder oAuthProvider;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        fa = this;

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
            finish();
        }


        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        textInputLayoutEmail = findViewById(R.id.idTextInputLayoutEmailLoginActivity);
        textInputLayoutPassword = findViewById(R.id.idTextInputLayoutPasswordLoginActivity);
        materialButtonLogin = findViewById(R.id.idButtonLoginLoginActivity);
        materialButtonGmail = findViewById(R.id.idButtonGmailLoginActivity);
        materialButtonFacebook = findViewById(R.id.idButtonFacebookLoginActivity);
        materialButtonGithub = findViewById(R.id.idButtonGithubLoginActivity);

        loginButton = findViewById(R.id.login_button);

        textViewCreateAnAccount = findViewById(R.id.idTextViewCreateAnAccountLoginActivity);

        textInputLayoutEmail.getEditText().setOnTouchListener(new View.OnTouchListener()
        {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                textInputLayoutEmail.setErrorEnabled(false);
                return false;
            }
        });

        textInputLayoutPassword.getEditText().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                textInputLayoutPassword.setErrorEnabled(false);
                return false;
            }
        });

        materialButtonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                userLogin();
            }
        });

        textViewCreateAnAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.textview_animation);
                animation.reset();

                textViewCreateAnAccount.clearAnimation();
                textViewCreateAnAccount.startAnimation(animation);

                startActivity(new Intent(getApplicationContext(), NewAccountActivity.class));
//                finish();
            }
        });


//        sign in with google

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        materialButtonGmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signInWithGoogle();
            }
        });


//        sign in with facebook

        callbackManager = CallbackManager.Factory.create();

        materialButtonFacebook.setOnClickListener(this);

//        public void fbLogin(View view)
//        {
//            // Before Edit:
//            // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_photos", "email", "public_profile", "user_posts" , "AccessToken"));
//
//            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_photos", "email", "public_profile", "user_posts"));
//            LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
//            LoginManager.getInstance().registerCallback(callbackManager,
//                    new FacebookCallback<LoginResult>()
//                    {
//                        @Override
//                        public void onSuccess(LoginResult loginResult)
//                        {
//                            // App code
//                        }
//
//                        @Override
//                        public void onCancel()
//                        {
//                            // App code
//                        }
//
//                        @Override
//                        public void onError(FacebookException exception)
//                        {
//                            // App code
//                        }
//                    });
//        }

//        materialButtonFacebook.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_photos", "email", "public_profile", "user_posts"));
//
////                LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
//                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
//                {
//                    @Override
//                    public void onSuccess(LoginResult loginResult)
//                    {
//
//                    }
//
//                    @Override
//                    public void onCancel()
//                    {
//
//                    }
//
//                    @Override
//                    public void onError(FacebookException error)
//                    {
//
//                    }
//                });
//            }
//        });

//        loginButton.setReadPermissions("public_profile","email", "user_birthday");
//
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
//        {
//            @Override
//            public void onSuccess(LoginResult loginResult)
//            {
//                progressDialog.setMessage("Verifying...");
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//                //handling the token for Firebase Auth
//
//                handleFacebookAccessToken(loginResult.getAccessToken());
//
////                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
////                {
////                    @Override
////                    public void onCompleted(JSONObject object, GraphResponse response)
////                    {
////                        try {
////                            String email = object.getString("email");
////                            String birthday = object.getString("birthday");
////
////                            Log.i(TAG, "onCompleted: Email: " + email);
////                            Log.i(TAG, "onCompleted: Birthday: " + birthday);
////
////                        }
////                        catch (JSONException e)
////                        {
////                            e.printStackTrace();
////                            Log.i(TAG, "onCompleted: JSON exception");
////                        }
////                    }
////                });
////
////                Bundle parameters = new Bundle();
////                parameters.putString("fields", "id,name,email");
////                graphRequest.setParameters(parameters);
////                graphRequest.executeAsync();
//            }
//
//            @Override
//            public void onCancel()
//            {
//                Log.d(TAG, "facebook:onCancel");
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onError(FacebookException error)
//            {
//                Log.d(TAG, "facebook:onError", error);
//                progressDialog.dismiss();
//            }
//        });


//        sign in with github

        oAuthProvider = OAuthProvider.newBuilder("github.com");

        materialButtonGithub.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressDialog.setMessage("Verifying...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                signINWithGithub();
            }
        });


        progressDialog = new ProgressDialog(this);
        toast = new Toast(this);

    }

    private void userLogin()
    {
        textInputLayoutEmail.setErrorEnabled(false);
        textInputLayoutPassword.setErrorEnabled(false);

        final String email = textInputLayoutEmail.getEditText().getText().toString().trim();
        final String password = textInputLayoutPassword.getEditText().getText().toString().trim();

        Log.d("TAG", "userLogin: "+email.length());

        if(email.length() == 0)
        {
            textInputLayoutEmail.setError("Email ID not blank");
            textInputLayoutEmail.requestFocus();
            return;
        }

        if(password.length() == 0)
        {
            textInputLayoutPassword.setError("Password not blank");
            textInputLayoutPassword.requestFocus();
            return;
        }


        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        progressDialog.dismiss();

                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Invalid Email id or Password",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
    }

//    sign in with google

    private void signInWithGoogle()
    {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RESULT_CODE_SINGIN);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        //    facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);


//        for google sign in
        if(requestCode == RESULT_CODE_SINGIN)
        {
            Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            progressDialog.setMessage("Verifying...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            handleSignInResult(googleSignInAccountTask);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> googleSignInAccountTask)
    {
        try
        {
            GoogleSignInAccount googleSignInAccount = googleSignInAccountTask.getResult(ApiException.class);

            fireBaseGoogleAuth(googleSignInAccount);
        }
        catch (ApiException e)
        {
            progressDialog.dismiss();
//            fireBaseGoogleAuth(null);
        }
    }

    private void fireBaseGoogleAuth(GoogleSignInAccount googleSignInAccount)
    {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            updateUI(firebaseUser);
                        }
                        else
                        {
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser)
    {
        progressDialog.dismiss();

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if(googleSignInAccount != null)
        {
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
            finish();
        }
    }

//    sign in with facebook


    private void handleFacebookAccessToken(AccessToken token)
    {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.i(TAG, "onComplete: login completed with user: " + user.getDisplayName());

                            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {

                            LoginManager.getInstance().logOut();
                            progressDialog.dismiss();
                            alertMessageLogin("facebook");
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(MainActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
//    {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }




//    Sign in with github

    private void signINWithGithub()
    {
        Task<AuthResult> pendigResultTask = firebaseAuth.getPendingAuthResult();

        if(pendigResultTask == null)
        {
            firebaseAuth.startActivityForSignInWithProvider(this, oAuthProvider.build())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                    {
                        @Override
                        public void onSuccess(AuthResult authResult)
                        {
                            progressDialog.dismiss();
                            Log.d(TAG, "onSuccess: Gautam Pending Task Null "+authResult);

//                            handleSignInResultWithGithub();

                            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            alertMessageLogin("Github");
                            Log.d(TAG, "onFailure: Gautam  Pending Task Null "+e);
                        }
                    });
        }
        else
        {
            pendigResultTask
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                    {
                        @Override
                        public void onSuccess(AuthResult authResult)
                        {
                            Log.d(TAG, "onSuccess: Gautam Pending Task Not Null "+authResult);

                            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Log.d(TAG, "onFailure: Gautam  Pending Task Not Null "+e);
                        }
                    });
        }

    }

    private void handleSignInResultWithGithub()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        assert firebaseUser != null;

        firebaseUser
                .startActivityForLinkWithProvider(this, oAuthProvider.build())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                {
                    @Override
                    public void onSuccess(AuthResult authResult)
                    {
//
//                        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
//                        startActivity(intent);
//                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                    }
                });
    }

//    alert message for sign in with facebook and github
    public void alertMessageLogin(String medium)
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(MainActivity.this);


        // Set the message show for the Alert time
        if(medium.equals("facebook"))
        {
            builder.setMessage("Problem:\n1.Email id associated with facebook is already used.\n\nSolution:\n1.Try to login with gmail OR Try to login with email id associated with facebook");
        }
        else
        {
            builder.setMessage("Problem:\n1.Email id associated with Github is already used.\n2.May be your browser save github credentials.\n\nSolution:\n1.Try to login with gmail OR Try to login with email id associated with Github\n2.Clear browser history (cached) and try again");
        }
        // Set Alert Title
        builder.setTitle("Login Error!");

        // Set Cancelable false// Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.

        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Ok",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close

                                LoginManager.getInstance().logOut();
                            }
                        });


        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    //Mainactivity

    @Override
    protected void onStart()
    {
        super.onStart();

        NetworkConfiguration networkConfiguration = new NetworkConfiguration(this);

        if(!networkConfiguration.isConnected())
        {
            setContentView(R.layout.main_no_internet);
//            getSupportActionBar().hide();
            alertMessage();
            return;
        }

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null)
        {
            Log.i(TAG, "onStart: Someone logged in <3");


            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Log.i(TAG, "onStart: No one logged in :/");
        }
    }

    public void alertMessage()
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(MainActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Please connect with to working internet connection");

        // Set Alert Title
        builder.setTitle("Network Error!");

        // Set Cancelable false// Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.

        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Ok",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
                                finish();
                                startActivity(getIntent());
                            }
                        });


        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        if(v == materialButtonFacebook)
        {
            // Before Edit:
            // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_photos", "email", "public_profile", "user_posts" , "AccessToken"));

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList( "email"));

//            LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>()
                    {
                        @Override
                        public void onSuccess(LoginResult loginResult)
                        {
                            // App code

                            progressDialog.setMessage("Verifying...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            //handling the token for Firebase Auth

                            handleFacebookAccessToken(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel()
                        {
                            // App code

                            Log.d(TAG, "facebook:onCancel");
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError(FacebookException exception)
                        {
                            // App code

                            Log.d(TAG, "onError: "+exception);
                            progressDialog.dismiss();
                        }
                    });
        }
    }
}