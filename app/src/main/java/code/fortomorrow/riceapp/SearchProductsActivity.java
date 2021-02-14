package code.fortomorrow.riceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

import code.fortomorrow.riceapp.Adapters.OrderAdapters;
import code.fortomorrow.riceapp.Model.Orders;

public class SearchProductsActivity extends AppCompatActivity {

    private EditText inputText;
    private RecyclerView search_list;
    private String SearchInput;
    private ImageView BackBtn;
    private ArrayList<Orders> itemlist;
    private ChildEventListener mChildEventListener;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        BackBtn = findViewById(R.id.Search_Back);
        BackBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
        itemlist = new ArrayList<>();
        inputText = findViewById(R.id.search_product_name);
        search_list = findViewById(R.id.search_list);
        search_list.setLayoutManager(new LinearLayoutManager(this));
        search_list.setHasFixedSize(true);
        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkavaility(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        reference = FirebaseDatabase.getInstance().getReference().child("Products");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Orders message = dataSnapshot.getValue(Orders.class);
                itemlist.add(message);

                search_list.setAdapter(new OrderAdapters(getApplicationContext(), itemlist,SearchProductsActivity.this));
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

    private void checkavaility(String s) {
        ArrayList<Orders> ordersList = new ArrayList<>();
        if (!s.isEmpty()) {
            ordersList.clear();
            for (int i = 0; i < itemlist.size(); i++) {
                if (itemlist.get(i).getPname().toLowerCase().startsWith(s.toLowerCase()) || itemlist.get(i).getPrice().toLowerCase().startsWith(s.toLowerCase())) {
                    ordersList.add(itemlist.get(i));
                }
            }
            search_list.setAdapter(new OrderAdapters(getApplicationContext(), ordersList, SearchProductsActivity.this));
        } else {
            search_list.setAdapter(new OrderAdapters(getApplicationContext(), itemlist, SearchProductsActivity.this));
        }
    }
}
