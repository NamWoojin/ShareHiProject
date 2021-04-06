package com.example.android.data.model.dto;

/*
Folder : 공유할 파일을 지정할 때 recyclerView에 담기는 내용을 담는 DTO
 */
public class Folder implements Comparable<Folder>{
    //0 : parent, 1:folder, 2:file
    int type;
    String name;
    String path;

    public Folder(int type, String name, String path) {
        this.type = type;
        this.name = name;
        this.path = path;
    }

    public Folder() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public int compareTo(Folder o) {
        if(this.type == o.type){
            return this.name.compareTo(o.name);
        }
        return this.type - o.type;
    }
}
