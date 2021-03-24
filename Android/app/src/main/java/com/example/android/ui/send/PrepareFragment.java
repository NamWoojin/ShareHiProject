package com.example.android.ui.send;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.data.model.dto.Event;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.data.viewmodelimpl.SendViewModelImpl;
import com.example.android.databinding.FragmentSendPrepareBinding;
import com.example.android.ui.main.BackdropActivity;

public class PrepareFragment extends Fragment {

    private FragmentSendPrepareBinding binding;
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_send_prepare, container, false);
        View view = binding.getRoot();

        mSendViewModel = new ViewModelProvider((BackdropActivity)getActivity()).get(SendViewModelImpl.class);
        binding.setViewModel(mSendViewModel);
        binding.setLifecycleOwner(this);

        mSendViewModel.getSwitchFragment().observe((BackdropActivity)getActivity(),this::switchFragment);


        // Inflate the layout for this fragment
        return view;
    }

    //send 내에서 화면전환
    private void switchFragment(Event<String> event){
        String dest = event.getContentIfNotHandled();
        if(dest == null)
            return;

        if(dest.equals("folder")){
            ((BackdropActivity)getActivity()).replaceFragment(FolderFragment.newInstance(),true);
        }
    }

}