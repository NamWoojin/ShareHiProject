'use strict';

const express = require('express');
const router = express.Router();
const UserService = require('../../services/member/member.srv');
const multer = require('multer');

var storage = multer.diskStorage({
  //경로 설정
  destination: function (req, file, cb) {
    cb(null, '/volumes/profile/');
    // cb(null, 'uploads/');
  },
//
  //실제 저장되는 파일명 설정
  filename: function (req, file, cb) {
    //파일명 설정을 돕기 위해 요청정보(req)와 파일(file)에 대한 정보를 전달함
    file.uploadedFile = {
      name: req.body.mem_id + '-' + file.originalname.split('.')[0],
      // name: req.body.mem_id,
      ext: file.mimetype.split('/')[1],
    };

    cb(null, file.uploadedFile.name + '.' + file.uploadedFile.ext);
  },
});

const upload = multer({ storage: storage });

router.post('/signup', UserService.signup);
router.delete('/signout', UserService.signout);
router.get('/getUser', UserService.getUser);
router.get('/checkEmail', UserService.checkEmail);
router.put('/update', UserService.update);
router.post('/checkPassword', UserService.checkPassword);
router.put('/updatePassword', UserService.updatePassword);
router.get('/checkEmail', UserService.checkEmail);
router.post('/requireEmailAuth', UserService.requireEmailAuth);
router.post('/checkEmailAuth', UserService.checkEmailAuth);
router.post('/upload', upload.single('image'), UserService.upload);
router.post('/findPW', UserService.findPW);

module.exports = router;
