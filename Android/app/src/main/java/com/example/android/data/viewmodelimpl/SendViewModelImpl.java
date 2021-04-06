package com.example.android.data.viewmodelimpl;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.R;
import com.example.android.data.model.SocketRepository;
import com.example.android.data.model.dto.Folder;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.ui.main.BackdropActivity;
import com.example.android.ui.send.CreateFolderFragment;
import com.example.android.ui.send.FolderFragment;
import com.example.android.ui.send.FolderRecyclerAdapter;
import com.example.android.ui.send.ShareFragment;
import com.example.android.ui.send.ShareNameFragment;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SendViewModelImpl extends ViewModel implements SendViewModel {

    private final int OPEN_DIRECTORY_REQUEST_CODE = 1000;

    private WeakReference<Activity> mActivityRef;

    //공유 폴더 선택
    private MutableLiveData<String> selectedPathLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> folderTitleLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> folderPathLiveData = new MutableLiveData<>("");
    private List<Folder> folderItems = new ArrayList<>();
    private FolderRecyclerAdapter folderRecyclerAdapter = new FolderRecyclerAdapter(this);
    private String mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();

    //새 폴더 생성
    private MutableLiveData<String> newFolderNameLiveData;

    //공유 정보
    private MutableLiveData<String> shareNameLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> shareTitleLiveData = new MutableLiveData<>("");

    //로딩
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    private ShareNameFragment shareNameFragment;
    private ShareFragment shareFragment;
    private CreateFolderFragment createFolderFragment;
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
    }

    @Override
    public void switchPage(String page) {
        if (page.equals("folder")) {
            getDir("Root", mRoot);
            ((BackdropActivity) mActivityRef.get()).replaceFragment(FolderFragment.newInstance(), true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                Log.i("TAG", "onActivityResult: " + uri.getPath());
                String[] pathSplit = uri.getPath().split(":");
                selectedPathLiveData.setValue(mRoot + (pathSplit.length == 1 ? "" : pathSplit[1]));
                Log.i("TAG", "onActivityResult: " + selectedPathLiveData.getValue());
            } else {
                Toast.makeText(mActivityRef.get(), "해당 폴더는 공유할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //share
    //공유 중단
    @Override
    public void stopShare() {
        loadingLiveData.setValue(true);
        mSocketRepository.stopSocket();

    }

    //prepare
    //별명 지정 dialog 열기
    @Override
    public void openShareNameDialog(){
        shareNameFragment = ShareNameFragment.newInstance();
        shareNameFragment.show(mActivityRef.get().getFragmentManager(), "SET_SHARE_NAME");
    }

    //별명 지정 dialog 닫기
    @Override
    public void closeShareNameDialog(){
        shareNameFragment.dismiss();
    }

    //공유 시작
    @Override
    public void startShare() {
        loadingLiveData.setValue(true);
        mSocketRepository.startSocket(selectedPathLiveData.getValue(),shareNameLiveData.getValue());
    }
    
    //소켓 연결 결과에 따른 화면 처리
    private void setSocketObserver() {
        mSocketRepository.getIsConnecting().observe((BackdropActivity) mActivityRef.get(), s -> {
            switch (s) {
                case "successConnect":
                    shareNameFragment.dismiss();
                    loadingLiveData.setValue(false);
                    shareTitleLiveData.setValue("공유중입니다.\n장치 별명: " + shareNameLiveData.getValue());
                    shareFragment = ShareFragment.newInstance();
                    shareFragment.setCancelable(false);
                    shareFragment.show(mActivityRef.get().getFragmentManager(), "START_SHARE");
                    break;
                case "failConnect":
                    loadingLiveData.setValue(false);
                    new AlertDialog.Builder(mActivityRef.get())
                            .setTitle("통신 중지")
                            .setMessage("소켓 연결이 끊어졌습니다.")
                            .setPositiveButton("확인", (dialog, whichButton) -> {
                                if (shareFragment != null && shareFragment.isAdded()) {  //dialog fragment
                                    shareFragment.dismiss();
                                }
                            })
                            .show();
                    break;
                case "successClose":
                    shareFragment.dismiss();
                    Toast.makeText(mActivityRef.get(), R.string.toast_socket_stop_message, Toast.LENGTH_SHORT).show();
                    loadingLiveData.setValue(false);
                    break;
                case "failClose":
                    Toast.makeText(mActivityRef.get(), R.string.toast_socket_close_fail_message, Toast.LENGTH_SHORT).show();
                    loadingLiveData.setValue(false);
                    break;
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
                Toast.makeText(mActivityRef.get(), "이 폴더는 공유할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mActivityRef.get(), "파일은 공유할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //폴더 목록 생성
    private void getDir(String name, String dirPath) {

        if (name.equals(new File(mRoot).getName()))
            name = "Root";
        folderTitleLiveData.setValue(name);
        folderPathLiveData.setValue(dirPath.replace(mRoot, "Root"));
        List<Folder> newList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(mActivityRef.get(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //권한 허용되어 있을 때
            File f = new File(dirPath);
            File[] files = f.listFiles();

            if (!dirPath.equals(mRoot)) {
                Folder folder = new Folder(0, mActivityRef.get().getString(R.string.fragment_folder_nothing_text), f.getParent());
                newList.add(folder);
            }

            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];

                    Folder folder = new Folder();
                    folder.setType(file.isDirectory() ? 1 : 2);
                    folder.setName(file.getName());
                    folder.setPath(file.getAbsolutePath());

                    newList.add(folder);
                }
            }
            Collections.sort(newList);
            folderItems = new ArrayList<>(newList);
            folderRecyclerAdapter.notifyDataSetChanged();
        }
    }

    //폴더 경로 선택
    @Override
    public void choiceFolderPath() {
        Log.i("TAG", "choiceFolderPath: 들어옴");
        String path = folderPathLiveData.getValue().replace("Root", mRoot);
        selectedPathLiveData.setValue(path);
        mActivityRef.get().onBackPressed();
    }

    @Override
    public void createFolderFragmentOpen() {
        newFolderNameLiveData = new MutableLiveData<>("");
        createFolderFragment = CreateFolderFragment.newInstance();
        createFolderFragment.show(mActivityRef.get().getFragmentManager(), "CREATE_FOLDER");
    }

    @Override
    public void createFolder() {
        if (mSocketRepository.createFolder(folderPathLiveData.getValue().replace("Root", mRoot), newFolderNameLiveData.getValue())) {
            Toast.makeText(mActivityRef.get(), R.string.toast_create_folder_success_message, Toast.LENGTH_SHORT).show();
            createFolderFragment.dismiss();
            getDir(folderTitleLiveData.getValue(), folderPathLiveData.getValue().replace("Root", mRoot));
        } else {
            Toast.makeText(mActivityRef.get(), R.string.toast_create_folder_fail_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void createFolderFragmentClose() {
        createFolderFragment.dismiss();
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

    @Override
    public void setNewFolderNameLiveData(MutableLiveData<String> newFolderNameLiveData) {
        this.newFolderNameLiveData = newFolderNameLiveData;
    }

    @Override
    public MutableLiveData<String> getNewFolderNameLiveData() {
        return newFolderNameLiveData;
    }

    @Override
    public MutableLiveData<String> getShareNameLiveData() {
        return shareNameLiveData;
    }

    @Override
    public void setShareNameLiveData(MutableLiveData<String> shareNameLiveData) {
        this.shareNameLiveData = shareNameLiveData;
    }

    @Override
    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    @Override
    public MutableLiveData<String> getShareTitleLiveData() {
        return shareTitleLiveData;
    }
}
