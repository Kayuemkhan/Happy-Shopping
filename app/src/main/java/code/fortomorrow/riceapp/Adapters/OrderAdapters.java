package code.fortomorrow.riceapp.Adapters;

import android.content.Context;
import android.media.tv.TvView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import code.fortomorrow.riceapp.Model.Orders;
import code.fortomorrow.riceapp.R;
import code.fortomorrow.riceapp.SearchProductsActivity;

public class OrderAdapters extends RecyclerView.Adapter<OrderAdapters.ViewHolder> {
    private ArrayList<Orders> messageList;
    private Context context;
    public OrderAdapters(SearchProductsActivity searchProductsActivity, ArrayList<Orders> messageList) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.products_items_layout,parent,false);
        return new OrderAdapters.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
