package code.fortomorrow.happyshopping.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.fortomorrow.happyshopping.R
import code.fortomorrow.happyshopping.adapters.OrderAdapters
import code.fortomorrow.happyshopping.model.Orders
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import java.util.Locale

class SearchProductsActivity : AppCompatActivity() {
    private lateinit var inputText: EditText
    private lateinit var searchList: RecyclerView
    private lateinit var backBtn: ImageView
    private var itemlist: ArrayList<Orders> = ArrayList()
    private var mChildEventListener: ChildEventListener? = null
    private var reference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_products)

        backBtn = findViewById(R.id.Search_Back)
        backBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        itemlist = ArrayList()
        inputText = findViewById(R.id.search_product_name)
        searchList = findViewById(R.id.search_list)
        searchList.setLayoutManager(LinearLayoutManager(this))
        searchList.setHasFixedSize(true)
        inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkavaility(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }


    override fun onStart() {
        super.onStart()
        reference = FirebaseDatabase.getInstance().reference.child("Products")
        val childEventListener: ChildEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val message = dataSnapshot.getValue(Orders::class.java)
                if (message != null) {
                    itemlist.add(message)
                }

                searchList.adapter =
                    OrderAdapters(applicationContext, itemlist, this@SearchProductsActivity)
                Log.e("AG", "onChildAdded:" + Gson().toJson(message))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        reference!!.addChildEventListener(childEventListener)
        mChildEventListener = childEventListener
    }

    private fun checkavaility(s: String) {
        val ordersList = ArrayList<Orders>()
        if (s.isNotEmpty()) {
            ordersList.clear()
            for (i in itemlist.indices) {
                if (itemlist[i].pname!!.lowercase(Locale.getDefault()).startsWith(
                        s.lowercase(
                            Locale.getDefault()
                        )
                    ) || itemlist[i].price!!.lowercase(Locale.getDefault()).startsWith(
                        s.lowercase(
                            Locale.getDefault()
                        )
                    )
                ) {
                    ordersList.add(itemlist[i])
                }
            }
            searchList.adapter =
                OrderAdapters(applicationContext, ordersList, this@SearchProductsActivity)
        } else {
            searchList.adapter =
                OrderAdapters(applicationContext, itemlist, this@SearchProductsActivity)
        }
    }
}
