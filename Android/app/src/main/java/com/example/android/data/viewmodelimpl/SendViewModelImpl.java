package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.android.R;
import com.example.android.data.connection.APIRequest;
import com.example.android.data.connection.RetrofitClient;
import com.example.android.data.model.SocketRepository;
import com.example.android.data.model.dto.APIResponse;
import com.example.android.data.model.dto.Event;
import com.example.android.data.model.dto.Folder;
import com.example.android.data.modelImpl.SocketRepositoryImpl;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.ui.main.BackdropActivity;
import com.example.android.ui.send.FolderFragment;
import com.example.android.ui.send.FolderRecyclerAdapter;
import com.example.android.ui.send.ShareFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SendViewModelImpl extends ViewModel implements SendViewModel {

    private WeakReference<Activity> mActivityRef;

    private MutableLiveData<String> selectedPathLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> folderTitleLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> folderPathLiveData = new MutableLiveData<>("");
    private List<Folder> folderItems = new ArrayList<>();
    private FolderRecyclerAdapter folderRecyclerAdapter = new FolderRecyclerAdapter(this);
    private String mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();

    private MutableLiveData<Boolean> canShareLiveData = new MutableLiveData<>(false);

    private ShareFragment shareFragment;
    private SocketRepository mSocketRepository;

    @Override
    public void setParentContext(Activity parentContext) {
        this.mActivityRef = new WeakReference<>(parentContext);
    }

    @Override
    public void setSocketRepository(SocketRepository repository, Activity parentContext) {
        mSocketRepository = repository;
        mSocketRepository.setParentContext(parentContext);
        setSocketObserver();
//        mSocketRepository.deleteFolder(mRoot,"sample.mp4");
    }

    @Override
    public void switchPage(String page) {
        if (page.equals("folder")) {
            getDir("Root", mRoot);
            ((BackdropActivity) mActivityRef.get()).replaceFragment(FolderFragment.newInstance(), true);
        }
    }

    //share
    //공유 중단
    @Override
    public void stopShare() {
        mSocketRepository.stopSocket();
    }

    //prepare
    //공유 시작
    @Override
    public void startShare() {
        mSocketRepository.startSocket(selectedPathLiveData.getValue());
    }

    private void setSocketObserver(){
        mSocketRepository.getIsConnecting().observe((BackdropActivity) mActivityRef.get(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                switch (s){
                    case "successConnect":
                        shareFragment = ShareFragment.newInstance();
                        shareFragment.setCancelable(false);
                        shareFragment.show(mActivityRef.get().getFragmentManager(), "START_SHARE");
                        break;
                    case "failConnect":
                        new AlertDialog.Builder(mActivityRef.get())
                                .setTitle("통신 중지")
                                .setMessage("소켓 연결이 끊어졌습니다.")
                                .setPositiveButton("확인", (dialog, whichButton) -> {
                                    shareFragment.dismiss();
                                })
                                .show();
                        break;
                    case "successClose":
                        shareFragment.dismiss();
                        Toast.makeText(mActivityRef.get(), R.string.toast_socket_stop_message, Toast.LENGTH_SHORT).show();
                        break;
                    case "failClose":
                        Toast.makeText(mActivityRef.get(), R.string.toast_socket_close_fail_message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    //선택 폴더 경로 삭제
    @Override
    public void deleteSelectedFolderPath() {
        selectedPathLiveData.setValue("");
    }

    //folder
    //폴더 list 클릭
    @Override
    public void clickFolderList(int pos) {
        File file = new File(folderItems.get(pos).getPath());
        if (file.isDirectory()) {
            if (file.canRead()) {
                getDir(file.getName(), file.getAbsolutePath());
            } else {
                Toast.makeText(mActivityRef.get(), "No files in this folder.", Toast.LENGTH_SHORT).show();
            }
        } else {
//            mFileName = file.getName();
//            Log.i("Test", "ext:" + mFileName.substring(mFileName.lastIndexOf('.') + 1, mFileName.length()));
        }
    }

    //폴더 목록 생성
    private void getDir(String name, String dirPath) {
        if (name.equals(new File(mRoot).getName()))
            name = "Root";
        folderTitleLiveData.setValue(name);
        folderPathLiveData.setValue(dirPath.replace(mRoot, "Root"));
        List<Folder> newList = new ArrayList<>();

        File f = new File(dirPath);
        File[] files = f.listFiles();

        if (!dirPath.equals(mRoot)) {
            Folder folder = new Folder(0, mActivityRef.get().getString(R.string.fragment_folder_nothing_text), f.getParent());
            newList.add(folder);
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            Folder folder = new Folder();
            folder.setType(file.isDirectory() ? 1 : 2);
            folder.setName(file.getName());
            folder.setPath(file.getAbsolutePath());

            newList.add(folder);
        }
        Collections.sort(newList);
        folderItems = new ArrayList<>(newList);
        folderRecyclerAdapter.notifyDataSetChanged();
    }

    //폴더 경로 선택
    @Override
    public void choiceFolderPath() {
        Log.i("TAG", "choiceFolderPath: 들어옴");
        selectedPathLiveData.setValue(folderPathLiveData.getValue().replace("Root", mRoot));
        mActivityRef.get().onBackPressed();
    }

    @Override
    public FolderRecyclerAdapter getFolderRecyclerAdapter() {
        return folderRecyclerAdapter;
    }

    @Override
    public List<Folder> getFolderItems() {
        return folderItems;
    }

    @Override
    public int getType(int pos) {
        return folderItems.get(pos).getType();
    }

    @Override
    public String getName(int pos) {
        return folderItems.get(pos).getName();
    }


    //getter setter
    @Override
    public void setFolderPathLiveData(MutableLiveData<String> folderPath) {
        this.folderPathLiveData = folderPath;
    }

    @Override
    public MutableLiveData<String> getFolderPathLiveData() {
        return folderPathLiveData;
    }

    @Override
    public MutableLiveData<Boolean> getCanShareLiveData() {
        return canShareLiveData;
    }

    @Override
    public void setCanShareLiveData(MutableLiveData<Boolean> canShareLiveData) {
        this.canShareLiveData = canShareLiveData;
    }

    @Override
    public void setFolderTitleLiveData(MutableLiveData<String> folderTitleLiveData) {
        this.folderTitleLiveData = folderTitleLiveData;
    }

    @Override
    public MutableLiveData<String> getFolderTitleLiveData() {
        return folderTitleLiveData;
    }

    @Override
    public void setSelectedPathLiveData(MutableLiveData<String> selectedPathLiveData) {
        this.selectedPathLiveData = selectedPathLiveData;
    }

    @Override
    public MutableLiveData<String> getSelectedPathLiveData() {
        return selectedPathLiveData;
    }
}
