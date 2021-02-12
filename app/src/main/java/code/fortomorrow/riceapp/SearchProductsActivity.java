package code.fortomorrow.riceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import code.fortomorrow.riceapp.Adapters.OrderAdapters;
import code.fortomorrow.riceapp.Model.Orders;
import code.fortomorrow.riceapp.ViewHolder.ProductViewHolder;

public class SearchProductsActivity extends AppCompatActivity {

    private EditText inputText;
    private RecyclerView searchList;
    private String SearchInput;
    private ImageView BackBtn;
    private ArrayList<Orders> messageList;
    private ChildEventListener mChildEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        BackBtn = findViewById(R.id.Search_Back);
        BackBtn.setOnClickListener(v->{
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        });
        messageList = new ArrayList<>();
        inputText = findViewById(R.id.search_product_name);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(this));
        searchList.setHasFixedSize(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Orders message = dataSnapshot.getValue(Orders.class);
                messageList.add(message);
                OrderAdapters orderAdapters = new OrderAdapters(SearchProductsActivity.this,messageList);
                searchList.setAdapter(orderAdapters);

                Log.e("AG", "onChildAdded:" + new Gson().toJson(message));
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
        };
        reference.addChildEventListener(childEventListener);
        mChildEventListener = childEventListener;
    }
}
