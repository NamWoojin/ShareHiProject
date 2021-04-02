package com.example.android.ui.send;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.databinding.RecyclerSendFolderItemBinding;
import com.example.android.databinding.RecyclerSendPrepareMemberItemBinding;

import java.util.ArrayList;

public class PrepareMemberRecyclerAdapter extends RecyclerView.Adapter<PrepareMemberRecyclerAdapter.ItemViewHolder> {
    private SendViewModel sendViewModel;

    public PrepareMemberRecyclerAdapter(SendViewModel sendViewModel){
        this.sendViewModel = sendViewModel;
    }

    @NonNull
    @Override
    public PrepareMemberRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item을 inflate 시키기
        // return 인자는 ViewHolder
        RecyclerSendPrepareMemberItemBinding binding = RecyclerSendPrepareMemberItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PrepareMemberRecyclerAdapter.ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
        if(holder instanceof ItemViewHolder){
            ((ItemViewHolder)holder).bind(sendViewModel,position);
        }
    }

    @Override
    public int getItemCount() {
        return sendViewModel.getMemberItems() == null? 0 : sendViewModel.getMemberItems().size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private RecyclerSendPrepareMemberItemBinding binding;

        public ItemViewHolder(RecyclerSendPrepareMemberItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SendViewModel sendViewModel,int pos){
            binding.setViewModel(sendViewModel);
            binding.setPos(pos);
            binding.executePendingBindings();
        }
    }
}
