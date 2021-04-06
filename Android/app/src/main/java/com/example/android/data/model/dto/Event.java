package com.example.android.data.model.dto;

/*
Event : LiveData 상태를 나타내기 위한 DTO
 */
public class Event<T> {
    private boolean hasBeenHandled = false;
    private T content;

    public Event(T content){
        this.content = content;
    }

    //핸들 상태에 따른 동작
    public T getContentIfNotHandled(){
        if(hasBeenHandled){
            return null;
        }else{
            hasBeenHandled = true;
            return content;
        }
    }

    //content값 반환
    public T peekContent(){
        return content;
    }

    //핸들 되었는지 여부
    public boolean isHandled(){
        return hasBeenHandled;
    }

    //핸들 초기화
    public void resetHandled(){
        hasBeenHandled = false;
    }
}
