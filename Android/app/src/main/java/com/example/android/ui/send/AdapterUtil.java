package com.example.android.ui.send;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*
AdapterUtil : folder recycler view 구현을 위한 커스텀 Adapter
 */
public class AdapterUtil {

    @BindingAdapter("bindAdapter")
    public static void verAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
