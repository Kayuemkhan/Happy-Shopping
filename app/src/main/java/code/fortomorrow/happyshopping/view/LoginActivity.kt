package code.fortomorrow.happyshopping.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import code.fortomorrow.happyshopping.R
import code.fortomorrow.happyshopping.model.Users
import code.fortomorrow.happyshopping.prevalent.Prevalent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper

class LoginActivity : AppCompatActivity() {
    private var InputNumber: EditText? = null
    private var InputPassword: EditText? = null
    private lateinit var LoginButton: Button
    var loadingBar1: ProgressDialog? = null
    var loadingBar2: ProgressDialog? = null
    private lateinit var signupbtn: TextView
    private val parentDbName = "Users"
    private var chkBoxRememberMe: CheckBox? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Paper.init(this)
        val UserPhoneKey = Paper.book().read<String>(Prevalent.UserPhoneKey)
        val UserPasswordKey = Paper.book().read<String>(Prevalent.UserPasswordKey)
        if (UserPhoneKey !== "" && UserPasswordKey !== "") {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                AllowAcrees(UserPhoneKey, UserPasswordKey)
                loadingBar2 = ProgressDialog(this)
                loadingBar2!!.setTitle("Already Logged in")
                loadingBar2!!.setMessage("Please Wait...")
                loadingBar2!!.setCanceledOnTouchOutside(false)
                loadingBar2!!.show()
            }
        }
        LoginButton = findViewById(R.id.login_btn)
        InputNumber = findViewById(R.id.login_phone_number_input)
        InputPassword = findViewById(R.id.login_password_input)
        loadingBar1 = ProgressDialog(this)
        signupbtn = findViewById(R.id.sign_up)

        chkBoxRememberMe = findViewById(R.id.remember_me_chkb)

        Paper.init(this)

        signupbtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        })

        LoginButton.setOnClickListener(View.OnClickListener { loginUser() })
    }

    private fun loginUser() {
        val phone = InputNumber!!.text.toString()
        val password = InputPassword!!.text.toString()

        if (TextUtils.isEmpty(phone)) {
            InputNumber!!.error = "Phone Field can't be Blank"
            return
        } else if (TextUtils.isEmpty(password)) {
            InputPassword!!.error = "Password Field can't be Blank"
            return
        } else {
            loadingBar1!!.setTitle("Login Account")
            loadingBar1!!.setMessage("Please Wait, While we are checking the credentials")
            loadingBar1!!.setCanceledOnTouchOutside(false)
            loadingBar1!!.show()
            AllowAccssAccount(phone, password)
        }
    }

    private fun AllowAccssAccount(phone: String, password: String) {
        if (chkBoxRememberMe!!.isChecked) {
            Paper.book().write(Prevalent.UserPhoneKey, phone)
            Paper.book().write(Prevalent.UserPasswordKey, password)
        }
        val Rootref = FirebaseDatabase.getInstance().reference
        Rootref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()) {
                    val usersData = dataSnapshot.child(parentDbName).child(phone).getValue(
                        Users::class.java
                    )

                    if (usersData!!.phone == phone) {
                        if (usersData.password == password) {
                            if (parentDbName == "Users") {
                                loadingBar1!!.dismiss()
                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                Prevalent.currentOnlineUser = usersData
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            loadingBar1!!.dismiss()
                            Toast.makeText(
                                this@LoginActivity,
                                "password is not Correct",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Account with this $phone number do not exists ",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingBar1!!.dismiss()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun AllowAcrees(phone: String?, password: String?) {
        val Rootref = FirebaseDatabase.getInstance().reference
        Rootref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("Users").child(phone!!).exists()) {
                    val usersData = dataSnapshot.child("Users").child(phone).getValue(
                        Users::class.java
                    )

                    if (usersData!!.phone == phone) {
                        if (usersData.password == password) {
                            loadingBar1!!.dismiss()
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            Prevalent.currentOnlineUser = usersData
                            startActivity(intent)
                        } else {
                            loadingBar1!!.dismiss()
                            Toast.makeText(
                                applicationContext,
                                "password is not Correct",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Account With this$phone number do not exists",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingBar1!!.dismiss()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
