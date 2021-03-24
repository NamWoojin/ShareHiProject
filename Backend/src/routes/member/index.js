'use strict';

const express = require('express');
const router = express.Router();
const UserService = require('../../services/member/member.srv');


router.post('/signup', UserService.signup);
router.delete('/signout', UserService.signout);
router.get('/getUser', UserService.getUser);
router.get('/checkEmail', UserService.checkEmail);
router.put('/update', UserService.update);
router.post('/checkPassword', UserService.checkPassword);
router.put('/updatePassword', UserService.updatePassword);
router.get('/checkEmail/:memID', UserService.checkEmail);
router.post('/requireEmailAuth', UserService.requireEmailAuth);
router.post('/checkEmailAuth', UserService.checkEmailAuth);

module.exports = router;
