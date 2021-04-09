# Share Hi

## 📃프로젝트 개요


- **개발 기간** : 2021.02.22 ~ 2021.04.09 (총 7주)
- **프로젝트 이름** : Share Hi
- **프로젝트 설명** :  휴대 단말과 PC간의 파일 공유 구글 크롬 확장 애플리케이션 개발

## 🛠개발 환경설정


### API Server

- 환경 : Node.js, Express.js

```bash
cd .\Backend\
npm install
npm startcd .\FileServer\
```

### File Server

- 환경 : Node.js, Express.js

```bash
cd .\FileServer\
npm install
npm start
```

### Web

```bash
cd .\Web\
npm install
npm run serve
```

### Chrome Extension

```bash
cd .\ChromeExtension\
npm install
npm run build
```

1. chrome://ChromeExtension/Extensions/ 에서 우측 상단 개발자 모드 활성화
2. 좌측 상단 '압축해제된 확장 프로그램을 로드합니다.' 클릭
3. \ChromExtension\Dist 폴더 업로드 후 확장 프로그램 실행

### Android

❗Android Lollipop(21)버전부터 Android 11(30)버전까지 지원합니다.

1. Android Studio 설치
[https://developer.android.com/studio?hl=ko#/](https://developer.android.com/studio?hl=ko#/)
2. File → Open 에서 'Android' 폴더 선택
3. Emulator 또는 단말기로 프로그램 실행

## 📚기술스택

### OS

- Ubuntu : 20.04

### Frontend

- Vue.js : 2.6.11
- Chrome Extension manifest_version : 2

### Mobile

- Android Studio : 4.1.2

### Backend

- nodejs : 10.24.0
- express : 4.17.1
- Redis : 6.2.1

### DB

- MySQL : 8.0.23

### CI/CD

- Jenkins : 2.283
- docker : 20.10.5

## 📌주요기능


 **SHARE HI**는 휴대 단말의 저장공간을 웹과 Chrome 확장 프로그램에 공유하는 서비스입니다. 

### 웹 & Chrome 확장 프로그램 → 단말기

- 단말기 저장공간 조회
- 파일 업로드
- 새 폴더 생성
- 파일 및 폴더명 수정
- 파일 및 폴더 삭제

### 웹 & Chrome 확장 프로그램 ← 단말기

- 파일 다운로드

## 😎 페이지소개


### Web


<details>
    <summary><strong>메인화면</strong></summary>

![Images/ReadMe/Web/Web_Main_1.png](Images/ReadMe/Web/Web_Main_1.png)

![Images/ReadMe/Web/Web_Main_2.png](Images/ReadMe/Web/Web_Main_2.png)

![Images/ReadMe/Web/Web_Main_3.png](Images/ReadMe/Web/Web_Main_3.png)

![Images/ReadMe/Web/Web_Main_4.png](Images/ReadMe/Web/Web_Main_4.png)
</details>


<details>
    <summary><strong>로그인</strong></summary>
    
![Images/ReadMe/Web/Web_Login.png](Images/ReadMe/Web/Web_Login.png)

**비밀번호 찾기**

![Images/ReadMe/Web/Web_FindPassword.png](Images/ReadMe/Web/Web_FindPassword.png)
</details>


<details>
    <summary><strong>회원가입</strong></summary>
    
![Images/ReadMe/Web/Web_SignUp.png](Images/ReadMe/Web/Web_SignUp.png)

**이메일 인증**

![Images/ReadMe/Web/Web_SignUp_CheckEmail.png](Images/ReadMe/Web/Web_SignUp_CheckEmail.png)
</details>


<details>
    <summary><strong>내 저장소</strong></summary>
    
![Images/ReadMe/Web/Web_MyDevice.png](Images/ReadMe/Web/Web_MyDevice.png)

**저장소 조회**

![Images/ReadMe/Web/Web_MyDevice_2.png](Images/ReadMe/Web/Web_MyDevice_2.png)

![Images/ReadMe/Web/Web_MyDevice_3.png](Images/ReadMe/Web/Web_MyDevice_3.png)

**파일 및 폴더 이름 변경**

![Images/ReadMe/Web/Web_MyDevice_ChangeName.png](Images/ReadMe/Web/Web_MyDevice_ChangeName.png)

**파일 및 폴더 삭제**

![Images/ReadMe/Web/Web_MyDevice_Delete.png](Images/ReadMe/Web/Web_MyDevice_Delete.png)

**파일 업로드**

![Images/ReadMe/Web/Web_MyDevice_Upload.png](Images/ReadMe/Web/Web_MyDevice_Upload.png)
</details>


<details>
    <summary><strong>계정 설정</strong></summary>
    
![Images/ReadMe/Web/Web_Setting_Profile.png](Images/ReadMe/Web/Web_Setting_Profile.png)

**비밀번호 변경**

![Images/ReadMe/Web/Web_Setting_ChangePassword.png](Images/ReadMe/Web/Web_Setting_ChangePassword.png)

**회원 탈퇴**

![Images/ReadMe/Web/Web_Setting_Out.png](Images/ReadMe/Web/Web_Setting_Out.png)

**팔로워 관리**

![Images/ReadMe/Web/Web_Setting_Follower.png](Images/ReadMe/Web/Web_Setting_Follower.png)
</details>
   

### Chrome Extension


<details>
    <summary><strong>로그인</strong></summary>
    
![Images/ReadMe/ChromeExtension/Extension_Login.png](Images/ReadMe/ChromeExtension/Extension_Login.png)
</details>


<details>
    <summary><strong>내 저장소</strong></summary>
    
![Images/ReadMe/ChromeExtension/Extension_MyDevice.png](Images/ReadMe/ChromeExtension/Extension_MyDevice.png)

**저장소 조회**

![Images/ReadMe/ChromeExtension/Extension_MyDevice_FileList.png](Images/ReadMe/ChromeExtension/Extension_MyDevice_FileList.png)

**동작 옵션(우측 마우스 클릭)**

![Images/ReadMe/ChromeExtension/Extension_MyDevice_FileOption.png](Images/ReadMe/ChromeExtension/Extension_MyDevice_FileOption.png)

**계정 옵션**

![Images/ReadMe/ChromeExtension/Extension_Option.png](Images/ReadMe/ChromeExtension/Extension_Option.png)

**파일 및 폴더 이름 변경**

![Images/ReadMe/ChromeExtension/Extension_MyDevice_ChangeName.png](Images/ReadMe/ChromeExtension/Extension_MyDevice_ChangeName.png)

**파일 및 폴더 삭제**

![Images/ReadMe/ChromeExtension/Extension_MyDevice_Delete.png](Images/ReadMe/ChromeExtension/Extension_MyDevice_Delete.png)

**파일 업로드**

![Images/ReadMe/ChromeExtension/Extension_MyDevice_Upload.png](Images/ReadMe/ChromeExtension/Extension_MyDevice_Upload.png)
</details>

    

### Android

<details>
    <summary><strong>인트로</strong></summary>
    
![Images/ReadMe/Android/Android_Intro.jpg](Images/ReadMe/Android/Android_Intro.jpg)
</details>


<details>
    <summary><strong>로그인</strong></summary>
    
![Images/ReadMe/Android/Android_Login.jpg](Images/ReadMe/Android/Android_Login.jpg)
</details>


<details>
    <summary><strong>회원가입</strong></summary>
    
![Images/ReadMe/Android/Android_SignUp.jpg](Images/ReadMe/Android/Android_SignUp.jpg)

**이메일 인증**

![Images/ReadMe/Android/Android_SignUp_CheckEmail.jpg](Images/ReadMe/Android/Android_SignUp_CheckEmail.jpg)
</details>


<details>
    <summary><strong>메인화면</strong></summary>
    
![Images/ReadMe/Android/Android_Main.jpg](Images/ReadMe/Android/Android_Main.jpg)
</details>


<details>
    <summary><strong>공유하기</strong></summary>
    
![Images/ReadMe/Android/Android_Send.jpg](Images/ReadMe/Android/Android_Send.jpg)

**폴더 리스트 조회**

![Images/ReadMe/Android/Android_Send_FileList.jpg](Images/ReadMe/Android/Android_Send_FileList.jpg)

**새 폴더 생성**

![Images/ReadMe/Android/Android_Send_NewFolder.jpg](Images/ReadMe/Android/Android_Send_NewFolder.jpg)

**공유할 폴더 선택 결과**

![Images/ReadMe/Android/Android_Send_ChoicedFolder.jpg](Images/ReadMe/Android/Android_Send_ChoicedFolder.jpg)

**공유 전 기기 별명 지정**

![Images/ReadMe/Android/Android_Send_Name.jpg](Images/ReadMe/Android/Android_Send_Name.jpg)

**공유 중 화면**

![Images/ReadMe/Android/Android_Send_Share.jpg](Images/ReadMe/Android/Android_Send_Share.jpg)

**서버와 연결이 끊겼을 때 알림**

![Images/ReadMe/Android/Android_Send_Fail.jpg](Images/ReadMe/Android/Android_Send_Fail.jpg)

**파일 받을 때 Notification**

![Images/ReadMe/Android/Android_Send_Notification.jpg](Images/ReadMe/Android/Android_Send_Notification.jpg)
</details>

<details>
    <summary><strong>계정설정</strong></summary>
    
![Images/ReadMe/Android/Android_Setting.jpg](Images/ReadMe/Android/Android_Setting.jpg)

**이미지 변경**

![Images/ReadMe/Android/Android_Setting_Image.jpg](Images/ReadMe/Android/Android_Setting_Image.jpg)

**비밀번호 변경**

![Images/ReadMe/Android/Android_Setting_ChangePassword.jpg](Images/ReadMe/Android/Android_Setting_ChangePassword.jpg)

**로그아웃 알림**

![Images/ReadMe/Android/Android_Setting_SignOut.jpg](Images/ReadMe/Android/Android_Setting_SignOut.jpg)

**회원탈퇴 알림**

![Images/ReadMe/Android/Android_Setting_Out.jpg](Images/ReadMe/Android/Android_Setting_Out.jpg)
</details>
    

## 🏆 최종산출물


- [최종발표 PPT](https://drive.google.com/file/d/1quUGWp0eOrOnWuBvGIO5JuwenwlJiyC_/view?usp=sharing)
- [UCC](https://youtu.be/25BzwXkryw4)

## 💡Team 태양광전구


- **구민진(팀장)**
    - [whxorb44@gmail.com](mailto:whxorb44@gmail.com)
    - Backend, Infra
- **권세진**
    - [kwon_sejin@naver.com](mailto:kwon_sejin@naver.com)
    - Frontend Web
- **남우진**
    - [nam990304@gmail.com](mailto:nam990304@gmail.com)
    - Android
- **심재혁**
    - [jaehyukawot@gmail.com](mailto:jaehyukawot@gmail.com)
    - Backend Fileserver
- **이병훈**
    - [eric99kr@naver.com](mailto:eric99kr@naver.com)
    - Chrome Extension