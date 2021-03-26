package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.R;
import com.example.android.data.model.dto.Event;
import com.example.android.data.model.dto.Folder;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.ui.main.BackdropActivity;
import com.example.android.ui.send.FolderFragment;
import com.example.android.ui.send.FolderRecyclerAdapter;
import com.example.android.ui.send.ShareFragment;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
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


    @Override
    public void setParentContext(Activity parentContext) {
        this.mActivityRef = new WeakReference<>(parentContext);
    }

    @Override
    public void switchPage(String page) {
        if (page.equals("folder")) {
            getDir("Root", mRoot);
            ((BackdropActivity)mActivityRef.get()).replaceFragment(FolderFragment.newInstance(),true);
        }
    }

    @Override
    public void startShare() {
        ShareFragment shareFragment = ShareFragment.newInstance();
        shareFragment.setCancelable(false);
        shareFragment.show(mActivityRef.get().getFragmentManager(), "START_SHARE");
    }

    //prepare
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
                getDir(file.getName(),file.getAbsolutePath());
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
        if(name.equals(new File(mRoot).getName()))
            name = "Root";
        folderTitleLiveData.setValue(name);
        folderPathLiveData.setValue(dirPath.replace(mRoot,"Root"));
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
        folderItems = new ArrayList<>(newList);
        folderRecyclerAdapter.notifyDataSetChanged();
    }
    //폴더 경로 선택
    @Override
    public void choiceFolderPath(){
        Log.i("TAG", "choiceFolderPath: 들어옴");
        selectedPathLiveData.setValue(folderPathLiveData.getValue().replace("Root",mRoot));
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
