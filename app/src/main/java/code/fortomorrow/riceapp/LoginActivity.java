package code.fortomorrow.riceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import code.fortomorrow.riceapp.Model.Users;
import code.fortomorrow.riceapp.Prevalent.Prevalent;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText InputNumber, InputPassword;
    private Button LoginButton;
    ProgressDialog loadingBar1,loadingBar2;
    private TextView signupbtn;
    private String parentDbName ="Users";
    private CheckBox chkBoxRememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Paper.init(this);
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if(UserPhoneKey != "" && UserPasswordKey != ""){
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAcrees(UserPhoneKey,UserPasswordKey);
                loadingBar2 = new ProgressDialog(this);
                loadingBar2.setTitle("Already Logged in");
                loadingBar2.setMessage("Please Wait...");
                loadingBar2.setCanceledOnTouchOutside(false);
                loadingBar2.show();
            }
        }
        LoginButton = findViewById(R.id.login_btn);
        InputNumber = findViewById(R.id.login_phone_number_input);
        InputPassword = findViewById(R.id.login_password_input);
        loadingBar1 = new ProgressDialog(this);
        signupbtn = findViewById(R.id.sign_up);

        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);

        Paper.init(this);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


    }

    private void loginUser() {
        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone)){
            InputNumber.setError("Phone Field can't be Blank");
            return;
        }
        else if(TextUtils.isEmpty(password)){
            InputPassword.setError("Password Field can't be Blank");
            return;
        }
        else {
            loadingBar1.setTitle("Login Account");
            loadingBar1.setMessage("Please Wait, While we are checking the credentials");
            loadingBar1.setCanceledOnTouchOutside(false);
            loadingBar1.show();
            AllowAccssAccount(phone,password);
        }
    }

    private void AllowAccssAccount(final String phone, final String password) {
        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phone).exists()){
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(password)){
                       if (parentDbName.equals("Users")) {
                                loadingBar1.dismiss();
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                                finish();
                            }

                        }
                        else {
                            loadingBar1.dismiss();
                            Toast.makeText(LoginActivity.this,"password is not Correct",Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this,"Account with this "+ phone+" number do not exists ",Toast.LENGTH_SHORT).show();
                    loadingBar1.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void AllowAcrees(final String phone, final String password) {
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phone).exists()){
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(password)){
                            loadingBar1.dismiss();
                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);

                        }
                        else {
                            loadingBar1.dismiss();
                            Toast.makeText(getApplicationContext(),"password is not Correct",Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Account With this"+ phone+" number do not exists",Toast.LENGTH_SHORT).show();
                    loadingBar1.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
