package code.fortomorrow.happyshopping.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import code.fortomorrow.happyshopping.R
import code.fortomorrow.happyshopping.prevalent.Prevalent
import code.fortomorrow.happyshopping.view_holder.ProductViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.paperdb.Paper

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit  var drawerLayout: DrawerLayout
    private var ProductsRef: DatabaseReference? = null
    private lateinit var recyclerView: RecyclerView
    var layoutManager: RecyclerView.LayoutManager? = null
    private val cart: ImageView? = null
    private lateinit var userNameTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Products")


        Paper.init(this)

        recyclerView = findViewById(R.id.recyler_menu)
        recyclerView.setHasFixedSize(true)
        //recyclerView.setHasFixedSize(true);
        layoutManager = GridLayoutManager(this, 2)
        //        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)


        recyclerView.setLayoutManager(staggeredGridLayoutManager)

        //        cart = findViewById(R.id.cart);
//        cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),CartActivity.class));
//            }
//        });
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Home"
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)
        val fab = findViewById<FloatingActionButton>(R.id.nav_cart)
        fab.setOnClickListener {
            val intent = Intent(this@HomeActivity, CartActivity::class.java)
            startActivity(intent)
        }
        val headerView = navigationView.getHeaderView(0)
        userNameTextView = headerView.findViewById(R.id.user_profile_name)
        val profileImageView = headerView.findViewById<CircleImageView>(R.id.user_profile_image)

        userNameTextView.setText(Prevalent.currentOnlineUser!!.name)
        Picasso.get().load(Prevalent.currentOnlineUser!!.image).placeholder(R.drawable.profile)
            .into(profileImageView)
        toggle.drawerArrowDrawable.color = getColor(R.color.colorPrimary)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val id = menuItem.itemId

        if (id == R.id.nav_cart) {
            val intent = Intent(this@HomeActivity, CartActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_search) {
            val intent = Intent(this@HomeActivity, SearchProductsActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_settings) {
            val intent = Intent(this@HomeActivity, SettinsActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_logout) {
            Paper.book().destroy()

            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        } else if (id == R.id.myordersss) {
            val intent = Intent(this@HomeActivity, MyOrdersActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.about) {
            val i = Intent(applicationContext, Aboutpage::class.java)
            startActivity(i)
        }

        val drawer = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onStart() {
        super.onStart()
        val options =
            FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef!!, Products::class.java)
                .build()


        val adapter: FirebaseRecyclerAdapter<Products, ProductViewHolder> =
            object : FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                override fun onBindViewHolder(
                    holder: ProductViewHolder,
                    position: Int,
                    model: Products
                ) {
                    holder.txtProductName.text = model.pname
                    //holder.txtProductDescription.setText(model.getDescription());
                    holder.txtProductPrice.text = model.price + " Taka"
                    Picasso.get().load(model.image).into(holder.imageView)

                    holder.itemView.setOnClickListener {
                        val intent = Intent(this@HomeActivity, ProductDetailsActivity::class.java)
                        intent.putExtra("pid", model.pid)
                        startActivity(intent)
                    }
                }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): ProductViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.home_products_layout, parent, false)
                    return ProductViewHolder(view)
                }
            }
        recyclerView!!.adapter = adapter
        adapter.startListening()
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
        super.onBackPressed()
    }
}
