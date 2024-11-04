package code.fortomorrow.happyshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import code.fortomorrow.happyshopping.model.Cart;
import code.fortomorrow.happyshopping.prevalent.Prevalent;
import code.fortomorrow.happyshopping.view_holder.CartViewHolder;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount,txtMsg1;
    private int overTotalPrice =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtMsg1 = findViewById(R.id.msg1);

        NextProcessBtn = findViewById(R.id.next_btn);
        txtTotalAmount = findViewById(R.id.total_price);

        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total price",String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options= new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.phone)
                                .child("Products"),
                        Cart.class).build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                        holder.txtProductQuantity.setText("Quantity: "+ model.quantity);
                        holder.txtProductPrice.setText("Price: "+ model.price +"$");
                        holder.txtProductName.setText("Name: "+ model.pname);

                        // Calculating the product with respect to quantity
                        int oneTypeProductPrice = ((Integer.valueOf(model.price))) * ((Integer.valueOf(model.quantity)));
                        overTotalPrice = overTotalPrice + oneTypeProductPrice;
                        txtTotalAmount.setText("Total Price = "+String.valueOf(overTotalPrice)+"$");

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // charsequence is the
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Edit",
                                                "Remove"

                                        };
                                AlertDialog.Builder builder  = new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Cart Options:");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if(i ==0){
                                            Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                            intent.putExtra("pid", model.pid);
                                            startActivity(intent);
                                        }
                                        if(i==1){
                                            cartListRef.child("User View")
                                                    .child(Prevalent.currentOnlineUser.phone)
                                                    .child("Products")
                                                    .child(model.pid)
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(CartActivity.this,"Item Removed Successfully",Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
    private void CheckOrderState(){
        DatabaseReference orderRefs;
        orderRefs = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.phone);
        orderRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    if(shippingState.equals("shipped")){
                        txtTotalAmount.setText("Dear" +userName +"\n order is shipped Successfully.") ;
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulation! Your final Order has been placed Successfully!\n" +
                                " Soon you will be verified!");
                        NextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"You can purchased more products , once you received your final order",Toast.LENGTH_LONG).show();

                    }
                    else if(shippingState.equals("not shipped")){
                        txtTotalAmount.setText("Shipping State = Not Shipped") ;
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"You can purchased more products , once you received your final order",Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
