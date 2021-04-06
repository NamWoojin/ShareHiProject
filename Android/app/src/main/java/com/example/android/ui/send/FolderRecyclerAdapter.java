package com.example.android.ui.send;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.databinding.RecyclerSendFolderItemBinding;

/*
FolderRecyclerAdapter : 폴더 목록을 보여주기 위한 RecyclerAdapter
 */
public class FolderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private SendViewModel viewModel;

    public FolderRecyclerAdapter(SendViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerSendFolderItemBinding binding = RecyclerSendFolderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).bind(viewModel, position);
        }
    }

    @Override
    public int getItemCount() {
        return viewModel.getFolderItems() == null ? 0 : viewModel.getFolderItems().size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private RecyclerSendFolderItemBinding binding;

        public ItemViewHolder(RecyclerSendFolderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SendViewModel viewModel, int pos) {
            binding.setViewModel(viewModel);
            binding.setPos(pos);
            binding.executePendingBindings();
        }
    }
}
