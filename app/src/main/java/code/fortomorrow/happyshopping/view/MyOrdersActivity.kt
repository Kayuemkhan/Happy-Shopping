package code.fortomorrow.happyshopping.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.fortomorrow.happyshopping.R
import code.fortomorrow.happyshopping.adapters.MyAllOrdersAdapters
import code.fortomorrow.happyshopping.model.AdminViewOrders
import code.fortomorrow.happyshopping.prevalent.Prevalent
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import io.paperdb.Paper

class MyOrdersActivity : AppCompatActivity() {
    private  lateinit  var myordersRV: RecyclerView
    private val firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var currentUser: String? = null
    private var ordersList: ArrayList<AdminViewOrders> = ArrayList()
    private var ordersList2: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)
        Paper.init(this)
        currentUser = Prevalent.currentOnlineUser!!.phone
        ordersList2 = ArrayList()
        myordersRV = findViewById(R.id.myordersRV)
        myordersRV.setLayoutManager(LinearLayoutManager(this))
        databaseReference = FirebaseDatabase.getInstance().reference.child("Admin View").child(
            currentUser!!
        ).child("Products")
        ordersList = ArrayList()
    }

    override fun onStart() {
        super.onStart()
        databaseReference =
            FirebaseDatabase.getInstance().reference.child("Cart List").child("Admin View").child(
                currentUser!!
            ).child("Products")


        //        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                AdminViewOrders orders = snapshot.child("Products").getValue(AdminViewOrders.class);
//                ordersList.add(orders);
//                Log.d("aaaa111", "" + new Gson().toJson(ordersList));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        databaseReference!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("aaaa111", "here")

                val orders = snapshot.getValue(AdminViewOrders::class.java)
                ordersList.add(orders!!)
                Log.d("aaaa111", "" + Gson().toJson(ordersList))
                myordersRV!!.adapter = MyAllOrdersAdapters(this@MyOrdersActivity, ordersList)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}