package code.fortomorrow.happyshopping.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import code.fortomorrow.happyshopping.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterActivity : AppCompatActivity() {
    private lateinit var createAccountButton: Button
    private var InputName: EditText? = null
    private var InputPhoneNumber: EditText? = null
    private var InputPassword: EditText? = null
    private var loadingBar: ProgressDialog? = null
    private lateinit var login_back: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        createAccountButton = findViewById(R.id.register_btn)
        InputName = findViewById(R.id.register_username_input)
        InputPhoneNumber = findViewById(R.id.register_phone_number_input)
        InputPassword = findViewById(R.id.register_password_input)
        loadingBar = ProgressDialog(this)
        login_back = findViewById(R.id.login_back)

        login_back.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    applicationContext, LoginActivity::class.java
                )
            )
        })

        createAccountButton.setOnClickListener(View.OnClickListener { createAccount() })
    }

    private fun createAccount() {
        val name = InputName!!.text.toString()
        val phone = InputPhoneNumber!!.text.toString()
        val password = InputPassword!!.text.toString()

        if (TextUtils.isEmpty(name)) {
            InputName!!.error = "Name Field can't be Blank"
            return
        } else if (TextUtils.isEmpty(phone)) {
            InputPhoneNumber!!.error = "Phone Field can't be Blank"
            return
        } else if (TextUtils.isEmpty(password)) {
            InputPassword!!.error = "Password Field can't be Blank"
            return
        } else {
            loadingBar!!.setTitle("Create Account")
            loadingBar!!.setMessage("Please Wait, While we are checking the credentials")
            loadingBar!!.setCanceledOnTouchOutside(false)
            loadingBar!!.show()

            validatephoneNumber(name, phone, password)
        }
    }

    private fun validatephoneNumber(name: String, phone: String, password: String) {
        val databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())) {
                    val userDataMap = HashMap<String, Any>()
                    userDataMap["phone"] = phone
                    userDataMap["password"] = password
                    userDataMap["name"] = name

                    databaseReference.child("Users").child(phone).updateChildren(userDataMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Congratulations , Your account is created",
                                    Toast.LENGTH_SHORT
                                ).show()
                                loadingBar!!.dismiss()

                                val intent =
                                    Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                loadingBar!!.dismiss()
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "NetWork error! Please, try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "This " + phone + "Already exists",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please try again using another Phone Number",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}