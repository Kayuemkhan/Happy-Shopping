package code.fortomorrow.riceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import code.fortomorrow.riceapp.Adapters.MyAllOrdersAdapters;
import code.fortomorrow.riceapp.Model.AdminOrders;
import code.fortomorrow.riceapp.Model.AdminViewOrders;
import code.fortomorrow.riceapp.Model.Orders;
import code.fortomorrow.riceapp.Model.Users;
import code.fortomorrow.riceapp.Prevalent.Prevalent;
import io.paperdb.Paper;

public class MyOrdersActivity extends AppCompatActivity {
    private RecyclerView myordersRV;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String currentUser;
    private List<AdminViewOrders> ordersList;
    private List<String> ordersList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Paper.init(this);
        currentUser = Prevalent.currentOnlineUser.getPhone();
        ordersList2 = new ArrayList<>();
        myordersRV = findViewById(R.id.myordersRV);
        myordersRV.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin View").child(currentUser).child("Products");
        ordersList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(currentUser).child("Products");


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
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("aaaa111", "here" );

                AdminViewOrders orders = snapshot.getValue(AdminViewOrders.class);
                ordersList.add(orders);
                Log.d("aaaa111", "" + new Gson().toJson(ordersList));
                myordersRV.setAdapter(new MyAllOrdersAdapters(MyOrdersActivity.this,ordersList));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}