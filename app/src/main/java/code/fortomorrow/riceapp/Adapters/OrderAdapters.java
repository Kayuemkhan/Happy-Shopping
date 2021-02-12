package code.fortomorrow.riceapp.Adapters;

import android.content.Context;
import android.media.tv.TvView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import code.fortomorrow.riceapp.Model.Orders;
import code.fortomorrow.riceapp.R;
import code.fortomorrow.riceapp.SearchProductsActivity;

public class OrderAdapters extends RecyclerView.Adapter<OrderAdapters.ViewHolder> {
    private ArrayList<Orders> messageList;
    private Context context;

    public OrderAdapters(Context applicationContext, ArrayList<Orders> messageLists) {
        this.context = applicationContext;
        this.messageList = messageLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.products_items_layout,parent,false);
        return new OrderAdapters.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(messageList.get(position).getImage()).placeholder(R.drawable.product_sample_image).into(holder.product_image);
        holder.product_name.setText(messageList.get(position).getPname());
        holder.product_price.setText(messageList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView product_image;
        private TextView product_name,product_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
        }
    }
}
