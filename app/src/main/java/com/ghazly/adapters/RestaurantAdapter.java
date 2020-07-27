package com.ghazly.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ghazly.R;
import com.ghazly.activities_fragments.activity_home.HomeActivity;
import com.ghazly.databinding.LoadmoreRowBinding;
import com.ghazly.databinding.RestaurantRowBinding;
import com.ghazly.models.RestuarantModel;
import com.ghazly.models.SingleRestaurantModel;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA_ROW =1;
    private final int LOAD_ROW =2;

    private List<SingleRestaurantModel> list;
    private Context context;
    private LayoutInflater inflater;
    private HomeActivity activity;

    public RestaurantAdapter(List<SingleRestaurantModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (HomeActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA_ROW){
            RestaurantRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_row, parent, false);
            return new MyHolder(binding);
        }else {
            LoadmoreRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.loadmore_row, parent, false);
            return new LoadMoreHolder(binding);
        }




    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder){
            MyHolder myHolder = (MyHolder) holder;

            myHolder.binding.setModel(list.get(position));
myHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        activity.setrestauant(list.get(holder.getLayoutPosition()).getId()+"");
    }
});



        }else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            loadMoreHolder.binding.progBar.setIndeterminate(true);
        }





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public RestaurantRowBinding binding;

        public MyHolder(@NonNull RestaurantRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;



        }
    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        public LoadmoreRowBinding binding;

        public LoadMoreHolder(@NonNull LoadmoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;



        }
    }


    @Override
    public int getItemViewType(int position) {
     SingleRestaurantModel model = list.get(position);
        if (model ==null){
            return LOAD_ROW;
        }else {
            return DATA_ROW;
        }
    }
}