package com.example.firebaseloginapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewAccountActivity extends AppCompatActivity
{
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private MaterialButton materialButtonRegister;
    private TextView textViewGoToLogin;

    private ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        getSupportActionBar().hide();

//        get firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();

//        textInputLayoutName = findViewById(R.id.idTextInputLayoutNameNewAccountActivity);
        textInputLayoutEmail = findViewById(R.id.idTextInputLayoutEmailNewAccountActivity);
        textInputLayoutPassword = findViewById(R.id.idTextInputLayoutPasswordNewAccountActivity);
        materialButtonRegister = findViewById(R.id.idButtonSubmitNewAccountActivity);
        textViewGoToLogin = findViewById(R.id.idTextViewGoToLoginNewAccountActivity);

        progressDialog = new ProgressDialog(this);


//        textInputLayoutName.getEditText().setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                textInputLayoutName.setErrorEnabled(false);
//                return false;
//            }
//        });


        textInputLayoutEmail.getEditText().setOnTouchListener(new View.OnTouchListener()
        {
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

        materialButtonRegister.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N_MR1)
            @Override
            public void onClick(View v)
            {
                    creatUser();
            }
        });

        textViewGoToLogin.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N_MR1)
            @Override
            public void onClick(View v)
            {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.textview_animation);
                animation.reset();

                textViewGoToLogin.clearAnimation();
                textViewGoToLogin.startAnimation(animation);

                onBackPressed();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        });

        progressDialog = new ProgressDialog(this);
    }

    private void creatUser()
    {
        textInputLayoutEmail.setErrorEnabled(false);
        textInputLayoutPassword.setErrorEnabled(false);


        final String email = textInputLayoutEmail.getEditText().getText().toString().trim();
        final String password = textInputLayoutPassword.getEditText().getText().toString().trim();


        if(email.length() == 0)
        {
            textInputLayoutEmail.setError("Email ID not blank");
            textInputLayoutEmail.requestFocus();
            return;
        }

        if(!isValidEmail(email))
        {
            textInputLayoutEmail.setError("Enter Valid Email ID");
            textInputLayoutEmail.requestFocus();
            return;
        }

        if(password.length() == 0)
        {
            textInputLayoutPassword.setError("Password not blank");
            textInputLayoutPassword.requestFocus();
            return;

        }

        if(password.length() < 8)
        {
            textInputLayoutPassword.setError("Enter Atleast 8 Characters");
            textInputLayoutPassword.requestFocus();
            return;
        }

        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(NewAccountActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        progressDialog.dismiss();

                        if(task.isSuccessful())
                        {
                            MainActivity.fa.finish();

                            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Account Creation is Failed, Try Different Email Id",
                                    Toast.LENGTH_SHORT
                                    ).show();
                        }
                    }
                });




    }


    public static boolean isValidEmail(CharSequence target)
    {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//
//    }
}