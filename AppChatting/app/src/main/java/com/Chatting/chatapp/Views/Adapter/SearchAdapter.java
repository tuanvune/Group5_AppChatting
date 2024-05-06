package com.Chatting.chatapp.Views.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Chatting.chatapp.Models.UserModel;
import com.Chatting.chatapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserModel> listUser;

    public SearchAdapter(Context context, ArrayList<UserModel> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_row_search, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        UserModel userModel = listUser.get(position);
        if (userModel != null) {
            if (userModel.getImageURL().equals("default")) {
                holder.ciUserSearch.setImageResource(R.drawable.ic_launcher_background);
            } else {
                Picasso.get().load(userModel.getImageURL()).into(holder.ciUserSearch);
            }
            holder.txtItemUserSearchName.setText(userModel.getUsername());
            holder.txtItemUserSearchStatus.setText(userModel.getStatus());
        }
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ciUserSearch;
        private TextView txtItemUserSearchName, txtItemUserSearchStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ciUserSearch = itemView.findViewById(R.id.ciUserSearch);
            txtItemUserSearchName = itemView.findViewById(R.id.txtItemUserSearchName);
            txtItemUserSearchStatus = itemView.findViewById(R.id.txtItemUserSearchStatus);
        }
    }
}
