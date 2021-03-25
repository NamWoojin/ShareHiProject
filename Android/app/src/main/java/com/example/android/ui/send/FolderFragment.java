package com.example.android.ui.send;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.data.viewmodelimpl.SendViewModelImpl;
import com.example.android.databinding.FragmentSendFolderBinding;
import com.example.android.databinding.FragmentSendPrepareBinding;
import com.example.android.ui.main.BackdropActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderFragment extends Fragment {

    private FragmentSendFolderBinding binding;
    private SendViewModel mSendViewModel;

    private String mFileName;
    private ListView lvFileControl;

    private List<String> lItem = null;
    private List<String> lPath = null;
    private String mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String currentRoot = mRoot;
    private TextView mPath;

    public FolderFragment() {
        // Required empty public constructor
    }


    public static FolderFragment newInstance() {
        FolderFragment fragment = new FolderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_send_folder, container, false);
        View view = binding.getRoot();

        mSendViewModel = new ViewModelProvider((BackdropActivity)getActivity()).get(SendViewModelImpl.class);
        binding.setViewModel(mSendViewModel);

        mPath = (TextView) view.findViewById(R.id.fragment_folder_path_textView);
        lvFileControl = (ListView) view.findViewById(R.id.fragment_folder_file_listView);

        getDir(currentRoot);
        lvFileControl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                File file = new File(lPath.get(position));
                if (file.isDirectory()) {
                    if (file.canRead()) {
                        currentRoot = lPath.get(position);
                        getDir(currentRoot);
                    }
                    else {
                        Toast.makeText(getActivity(), "No files in this folder.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mFileName = file.getName();
                    Log.i("Test", "ext:" + mFileName.substring(mFileName.lastIndexOf('.') + 1, mFileName.length()));
                }
            }
        });

        Button folderSelectButton = (Button) view.findViewById(R.id.fragment_folder_select_button);

        folderSelectButton.setOnClickListener(v -> {
            mSendViewModel.getFolderPathLiveData().setValue(currentRoot);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(FolderFragment.this).commit();
            fragmentManager.popBackStack();
        });

        return view;
    }

    private void getDir(String dirPath) {
        mPath.setText("Location: " + dirPath);
        lItem = new ArrayList<String>();
        lPath = new ArrayList<String>();

        File f = new File(dirPath);
        File[] files = f.listFiles();

        if (!dirPath.equals(mRoot)) {
            //item.add(root); //to root.
            //path.add(root);
            lItem.add("../"); //to parent folder
            lPath.add(f.getParent());
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            lPath.add(file.getAbsolutePath());

            if (file.isDirectory())
                lItem.add(file.getName() + "/");
            else
                lItem.add(file.getName());
        }
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lItem);
        lvFileControl.setAdapter(fileList);
    }
}