package code.fortomorrow.happyshopping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import code.fortomorrow.happyshopping.Model.AdminViewOrders;
import code.fortomorrow.happyshopping.MyOrdersActivity;
import code.fortomorrow.happyshopping.R;

public class MyAllOrdersAdapters extends RecyclerView.Adapter<MyAllOrdersAdapters.ViewHolder> {
    private Context context;
    private List<AdminViewOrders>adminViewOrdersList;

    public MyAllOrdersAdapters(MyOrdersActivity myOrdersActivity, List<AdminViewOrders> ordersList) {
        this.context = myOrdersActivity;
        this.adminViewOrdersList = ordersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.singlemyorders,parent,false);
        return new MyAllOrdersAdapters.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.product_name.setText("Product Name: "+adminViewOrdersList.get(position).getPname());
        holder.product_price.setText("Product price: "+adminViewOrdersList.get(position).getPrice());
        holder.product_date.setVisibility(View.VISIBLE);
        holder.product_date.setText("Date: "+adminViewOrdersList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return adminViewOrdersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView product_name,product_price,product_date;

        private ImageView product_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_image = itemView.findViewById(R.id.product_image);
            product_date = itemView.findViewById(R.id.product_date);

        }
    }
}
