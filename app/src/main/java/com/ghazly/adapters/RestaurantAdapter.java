package com.ghazly.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ghazly.R;
import com.ghazly.activities_fragments.activity_home.HomeActivity;
import com.ghazly.activities_fragments.activity_my_favorite.MyFavoriteActivity;
import com.ghazly.activities_fragments.activity_restuarant_filter_result.RestuarantFilterActivity;
import com.ghazly.activities_fragments.activity_search.SearchActivity;
import com.ghazly.databinding.LoadmoreRowBinding;
import com.ghazly.databinding.RestaurantRowBinding;
import com.ghazly.models.RestuarantModel;
import com.ghazly.models.SingleRestaurantModel;
import com.ghazly.preferences.Preferences;
import com.ghazly.share.Common;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA_ROW = 1;
    private final int LOAD_ROW = 2;

    private List<SingleRestaurantModel> list;
    private Context context;
    private LayoutInflater inflater;
    private HomeActivity homeActivity;
    private RestuarantFilterActivity restuarantFilterActivity;
    private MyFavoriteActivity myFavoriteActivity;
    private SearchActivity searchActivity;
    private int i = -1;

    public RestaurantAdapter(List<SingleRestaurantModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA_ROW) {
            RestaurantRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_row, parent, false);
            return new MyHolder(binding);
        } else {
            LoadmoreRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.loadmore_row, parent, false);
            return new LoadMoreHolder(binding);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;

            myHolder.binding.setModel(list.get(position));
            if (list.get(position).getBranch_data_count().equals("0")) {
                myHolder.binding.llnum.setVisibility(View.GONE);
            }
            if (list.get(position).getFavorite() != null) {
                Log.e("sssssss", list.get(position).getFavorite().getId() + "");
                myHolder.binding.checkbox.setChecked(true);
            }
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof HomeActivity) {
                        homeActivity = (HomeActivity) context;
                        homeActivity.setrestauant(list.get(holder.getLayoutPosition()).getId() + "");

                    } else if (context instanceof RestuarantFilterActivity) {
                        restuarantFilterActivity = (RestuarantFilterActivity) context;
                        restuarantFilterActivity.setrestauant(list.get(holder.getLayoutPosition()).getId() + "");

                    } else if (context instanceof MyFavoriteActivity) {
                        myFavoriteActivity = (MyFavoriteActivity) context;
                        myFavoriteActivity.setrestauant(list.get(holder.getLayoutPosition()).getId() + "");
                    } else {
                        searchActivity = (SearchActivity) context;
                        searchActivity.setrestauant(list.get(holder.getLayoutPosition()).getId() + "");
                    }
                }
            });
            myHolder.binding.imgshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof HomeActivity) {
                        homeActivity = (HomeActivity) context;
                        homeActivity.share(list.get(myHolder.getAdapterPosition()));
                    } else if (context instanceof RestuarantFilterActivity) {
                        restuarantFilterActivity = (RestuarantFilterActivity) context;

                        restuarantFilterActivity.share(list.get(myHolder.getAdapterPosition()));
                    } else {
                        searchActivity = (SearchActivity) context;
                        searchActivity.share(list.get(myHolder.getAdapterPosition()));

                    }
                }
            });
            myHolder.binding.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Preferences.getInstance().getUserData(context) != null) {
                        if (context instanceof HomeActivity) {
                            homeActivity = (HomeActivity) context;
                            homeActivity.like_dislike(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition());
                        } else if (context instanceof RestuarantFilterActivity) {
                            restuarantFilterActivity = (RestuarantFilterActivity) context;

                            restuarantFilterActivity.like_dislike(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition());
                        }
//                    } else if(context instanceof MyFavoriteActivity){
//                        myFavoriteActivity = (MyFavoriteActivity) context;
//                        myFavoriteActivity.like_dislike(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition());
//                    }
                        else {
                            searchActivity = (SearchActivity) context;
                            searchActivity.like_dislike(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition());

                        }
                    } else {
                        myHolder.binding.checkbox.setChecked(false);
                        Common.CreateNoSignAlertDialog(context);
                    }
                }
            });


        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
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
        if (model == null) {
            return LOAD_ROW;
        } else {
            return DATA_ROW;
        }
    }
}