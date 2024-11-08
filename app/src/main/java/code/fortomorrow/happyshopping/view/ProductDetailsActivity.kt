package code.fortomorrow.happyshopping.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import code.fortomorrow.happyshopping.R
import code.fortomorrow.happyshopping.prevalent.Prevalent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar

class ProductDetailsActivity : AppCompatActivity() {
    private var productImage: ImageView? = null
    private var numberButton: Button? = null
    private var productPrice: TextView? = null
    private var productDescription: TextView? = null
    private var productName: TextView? = null
    private lateinit var addToCartBtn: Button
    private var productID: String? = ""
    private var state = "normal"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        productID = intent.getStringExtra("pid")
        addToCartBtn = findViewById(R.id.pd_add_to_cart_button)
        numberButton = findViewById(R.id.number_btn)
        productImage = findViewById(R.id.product_image_details)
        productName = findViewById(R.id.product_name_details)
        productDescription = findViewById(R.id.product_description_details)
        productPrice = findViewById(R.id.product_price_details)

        getproductDetails(productID)
        addToCartBtn.setOnClickListener {
            if (state == "Order Placed" || state == "Order Shipped") {
                Toast.makeText(
                    applicationContext,
                    "You can add purchase More products, once Your order is shipped or confirmed ! ",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                addingToCartList()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        CheckOrderState()
    }

    @SuppressLint("SimpleDateFormat")
    private fun addingToCartList() {
        val saveCurrentTime: String
        val saveCurrentDate: String

        val callForDate = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentDate.format(callForDate.time)
        SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentDate.format(callForDate.time)

        val cartListRef = FirebaseDatabase.getInstance().reference.child("Cart List")
        val cartMap = HashMap<String, Any?>()
        cartMap["pid"] = productID
        cartMap["pname"] = productName!!.text.toString()
        cartMap["price"] = productPrice!!.text.toString().toString()
        cartMap["date"] = saveCurrentDate
        cartMap["time"] = saveCurrentTime
        cartMap["quantity"] = numberButton!!.text.toString()
        cartMap["discount"] = ""

        cartListRef.child("User View").child(Prevalent.currentOnlineUser!!.phone!!)
            .child("Products")
            .child(productID!!)
            .updateChildren(cartMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cartListRef.child("Admin View").child(Prevalent.currentOnlineUser!!.phone!!)
                        .child("Products")
                        .child(productID!!)
                        .updateChildren(cartMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@ProductDetailsActivity, "Added to cart",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this@ProductDetailsActivity, HomeActivity::class.java)
                                startActivity(intent)
                            }
                        }
                }
            }
    }

    private fun getproductDetails(productID: String?) {
        val productRef = FirebaseDatabase.getInstance().reference.child("Products")
        productRef.child(productID!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val products = dataSnapshot.getValue(Products::class.java)
                    productName!!.text = products!!.pname
                    productPrice!!.text = products.price
                    productDescription!!.text = products.description
                    Picasso.get().load(products.image).into(productImage)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun CheckOrderState() {
        val orderRefs =
            FirebaseDatabase.getInstance().reference.child("Orders")
                .child(Prevalent.currentOnlineUser!!.phone!!)
        orderRefs.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val shippingState = dataSnapshot.child("state").value.toString()

                    if (shippingState == "shipped") {
                        state = "Order Shipped"
                    } else if (shippingState == "not shipped") {
                        state = "Order Placed"
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
