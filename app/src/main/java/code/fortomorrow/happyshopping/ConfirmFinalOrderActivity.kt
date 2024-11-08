package code.fortomorrow.happyshopping

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import code.fortomorrow.happyshopping.model.Users
import code.fortomorrow.happyshopping.prevalent.Prevalent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar

class ConfirmFinalOrderActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private var phoneEditText: EditText? = null
    private var addressEditText: EditText? = null
    private var cityEditText: EditText? = null
    private lateinit var confirmOrderBtn: Button
    private  var totalAmount: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_final_order)

        totalAmount = intent.getStringExtra("Total price")

        Toast.makeText(this, "Total Ammount :  $totalAmount$", Toast.LENGTH_LONG).show()
        confirmOrderBtn = findViewById(R.id.confirm_final_order_btn)
        nameEditText = findViewById(R.id.shippment_name)
        phoneEditText = findViewById(R.id.shippment_phone_number)
        addressEditText = findViewById(R.id.shippment_address)
        cityEditText = findViewById(R.id.shipment_city)
        val databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(
            Prevalent.currentOnlineUser!!.phone!!
        )
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.getValue(Users::class.java)!!.name
                nameEditText.setText(name)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        confirmOrderBtn.setOnClickListener(View.OnClickListener { check() })
    }


    private fun check() {
        if (TextUtils.isEmpty(nameEditText!!.text.toString())) {
            nameEditText!!.error = "Please provide your full name"
        } else if (TextUtils.isEmpty(phoneEditText!!.text.toString())) {
            phoneEditText!!.error = "Please provide your phone number"
        } else if (TextUtils.isEmpty(addressEditText!!.text.toString())) {
            addressEditText!!.error = "Please provide your phone number"
        } else if (TextUtils.isEmpty(cityEditText!!.text.toString())) {
            cityEditText!!.error = "Please provide your phone number"
        } else {
            confirmOrder()
        }
    }

    private fun confirmOrder() {
        val saveCurrentDate: String
        val saveCurrentTime: String
        val callForDate = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentDate.format(callForDate.time)
        val currentTime = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentDate.format(callForDate.time)

        val ordersRef = FirebaseDatabase.getInstance().reference
            .child("Orders")
            .child(Prevalent.currentOnlineUser!!.phone!!)
        val ordersMap = HashMap<String, Any?>()
        ordersMap["totalAmount"] = totalAmount
        ordersMap["name"] = nameEditText!!.text.toString()
        ordersMap["phone"] = phoneEditText!!.text.toString().toString()
        ordersMap["adress"] = addressEditText!!.text.toString()
        ordersMap["city"] = cityEditText!!.text.toString()
        ordersMap["date"] = saveCurrentDate
        ordersMap["time"] = saveCurrentTime
        ordersMap["state"] = "not shipped"


        ordersRef.updateChildren(ordersMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseDatabase.getInstance().reference
                    .child("Cart List")
                    .child("User View")
                    .child(Prevalent.currentOnlineUser!!.phone!!)
                    .removeValue()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@ConfirmFinalOrderActivity,
                                "Your final Order has been placed successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent =
                                Intent(this@ConfirmFinalOrderActivity, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            finish()
                        }
                    }
            }
        }
    }
}
