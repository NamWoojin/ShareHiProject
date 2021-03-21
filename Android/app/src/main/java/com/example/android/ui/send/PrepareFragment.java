package com.example.android.ui.send;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.data.viewmodel.LoginViewModel;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.data.viewmodelimpl.LoginViewModelImpl;
import com.example.android.data.viewmodelimpl.SignUpViewModelImpl;
import com.example.android.ui.main.BackdropActivity;

public class PrepareFragment extends Fragment {

    private SendViewModel mSendViewModel;

    private static PrepareRecyclerAdapter adapter;

    public PrepareFragment() {
        // Required empty public constructor
    }


    public static PrepareFragment newInstance() {
        PrepareFragment fragment = new PrepareFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_prepare, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.fragment_prepare_choice_folders_RecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new PrepareRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        Button goFolderButton = (Button) view.findViewById(R.id.fragment_prepare_go_folder_Button);

        goFolderButton.setOnClickListener(v -> {
            ((BackdropActivity)getActivity()).replaceFragment(FolderFragment.newInstance());
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void setChildFragment(Fragment child){
        FragmentTransaction childFt = getChildFragmentManager().beginTransaction();

        if(!child.isAdded()){
            childFt.replace(R.id.activity_backdrop_fragment, child);
            childFt.addToBackStack(null);
            childFt.commit();
        }
    }
}