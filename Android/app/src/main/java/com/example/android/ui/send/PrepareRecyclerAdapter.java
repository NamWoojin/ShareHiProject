package com.example.android.ui.send;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;

import java.util.ArrayList;

public class PrepareRecyclerAdapter extends RecyclerView.Adapter<PrepareRecyclerAdapter.ItemViewHolder> {

    private ArrayList<String> listData = new ArrayList<>();

    @NonNull
    @Override
    public PrepareRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item을 inflate 시키기
        // return 인자는 ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_send_prepare_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrepareRecyclerAdapter.ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(String route) {
        // 외부에서 item을 추가
        listData.add(route);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView routeTextView;

        ItemViewHolder(View itemView) {
            super(itemView);

            routeTextView = itemView.findViewById(R.id.recycler_prepare_route_TextView);
        }

        void onBind(String route) {
            routeTextView.setText(route);
        }
    }
}
