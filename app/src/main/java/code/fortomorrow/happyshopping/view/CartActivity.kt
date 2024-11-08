package code.fortomorrow.happyshopping.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.fortomorrow.happyshopping.R
import code.fortomorrow.happyshopping.model.Cart
import code.fortomorrow.happyshopping.prevalent.Prevalent
import code.fortomorrow.happyshopping.view_holder.CartViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var NextProcessBtn: Button
    private var txtTotalAmount: TextView? = null
    private var txtMsg1: TextView? = null
    private var overTotalPrice = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.cart_list)
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        txtMsg1 = findViewById(R.id.msg1)

        NextProcessBtn = findViewById(R.id.next_btn)
        txtTotalAmount = findViewById(R.id.total_price)

        NextProcessBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@CartActivity, ConfirmFinalOrderActivity::class.java)
            intent.putExtra("Total price", overTotalPrice.toString())
            startActivity(intent)
            finish()
        })
    }

    override fun onStart() {
        super.onStart()

        CheckOrderState()

        val cartListRef = FirebaseDatabase.getInstance().reference.child("Cart List")
        val options = FirebaseRecyclerOptions.Builder<Cart>()
            .setQuery(
                cartListRef.child("User View")
                    .child(Prevalent.currentOnlineUser!!.phone!!)
                    .child("Products"),
                Cart::class.java
            ).build()
        val adapter: FirebaseRecyclerAdapter<Cart, CartViewHolder> =
            object : FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                override fun onBindViewHolder(holder: CartViewHolder, position: Int, model: Cart) {
                    holder.txtProductQuantity.text = "Quantity: " + model.quantity
                    holder.txtProductPrice.text = "Price: " + model.price + "$"
                    holder.txtProductName.text = "Name: " + model.pname

                    // Calculating the product with respect to quantity
                    val oneTypeProductPrice =
                        ((model.price!!.toInt())) * ((model.quantity!!.toInt()))
                    overTotalPrice = overTotalPrice + oneTypeProductPrice
                    txtTotalAmount!!.text = "Total Price = $overTotalPrice$"

                    holder.itemView.setOnClickListener { // charsequence is the
                        val options = arrayOf<CharSequence>(
                            "Edit",
                            "Remove"

                        )
                        val builder = AlertDialog.Builder(this@CartActivity)
                        builder.setTitle("Cart Options:")
                        builder.setItems(options) { dialog, i ->
                            if (i == 0) {
                                val intent =
                                    Intent(this@CartActivity, ProductDetailsActivity::class.java)
                                intent.putExtra("pid", model.pid)
                                startActivity(intent)
                            }
                            if (i == 1) {
                                cartListRef.child("User View")
                                    .child(Prevalent.currentOnlineUser!!.phone!!)
                                    .child("Products")
                                    .child(model.pid!!)
                                    .removeValue()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this@CartActivity,
                                                "Item Removed Successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val intent =
                                                Intent(this@CartActivity, HomeActivity::class.java)
                                            startActivity(intent)
                                        }
                                    }
                            }
                        }
                        builder.show()
                    }
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.cart_items_layout, parent, false)
                    val holder = CartViewHolder(view)
                    return holder
                }
            }
        recyclerView!!.adapter = adapter
        adapter.startListening()
    }

    private fun CheckOrderState() {
        val orderRefs =
            FirebaseDatabase.getInstance().reference.child("Orders")
                .child(Prevalent.currentOnlineUser!!.phone!!)
        orderRefs.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val shippingState = dataSnapshot.child("state").value.toString()
                    val userName = dataSnapshot.child("name").value.toString()
                    if (shippingState == "shipped") {
                        txtTotalAmount!!.text = "Dear$userName\n order is shipped Successfully."
                        recyclerView!!.visibility = View.GONE

                        txtMsg1!!.visibility = View.VISIBLE
                        txtMsg1!!.text =
                            """Congratulation! Your final Order has been placed Successfully!
 Soon you will be verified!"""
                        NextProcessBtn!!.visibility = View.GONE
                        Toast.makeText(
                            this@CartActivity,
                            "You can purchased more products , once you received your final order",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (shippingState == "not shipped") {
                        txtTotalAmount!!.text = "Shipping State = Not Shipped"
                        recyclerView!!.visibility = View.GONE

                        txtMsg1!!.visibility = View.VISIBLE
                        NextProcessBtn!!.visibility = View.GONE
                        Toast.makeText(
                            this@CartActivity,
                            "You can purchased more products , once you received your final order",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
