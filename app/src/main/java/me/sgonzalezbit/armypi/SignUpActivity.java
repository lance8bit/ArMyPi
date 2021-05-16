package me.sgonzalezbit.armypi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEt, passwordEt, passwordEt2;
    private Button SignUpBtn;
    private TextView SignInTv;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        emailEt = findViewById(R.id.email_input);
        passwordEt = findViewById(R.id.password_input);
        passwordEt2 = findViewById(R.id.password_input2);
        SignUpBtn = findViewById(R.id.register_btn);
        progressDialog = new ProgressDialog(this);
        SignInTv = findViewById(R.id.signinLink);
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
        SignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Register(){
        String email = emailEt.getText().toString();
        String password1 = passwordEt.getText().toString();
        String password2 = passwordEt2.getText().toString();
        if (TextUtils.isEmpty(email)){
            emailEt.setError("Enter your email");
            return;
        }
        else if(TextUtils.isEmpty(password1)){
            passwordEt.setError("Enter your password");
            return;
        }
        else if(TextUtils.isEmpty(password2)){
            passwordEt2.setError("Confirm your password");
            return;
        }
        else if(!password1.equals(password2)){
            passwordEt2.setError("Different password");
            return;
        }
        else if(password1.length()<4){
            passwordEt2.setError("Lenght should be > 4");
            return;
        }
        else if(!isValidEmail(email)){
            emailEt.setError("Invalid email");
            return;
        }
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Successully registered", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, FragmentContainer.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(SignUpActivity.this, "Sign up fail!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private Boolean isValidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
