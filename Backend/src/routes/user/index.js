'use strict';

const express = require('express');
const router = express.Router();
const UserController = require('./user.ctrl');

router.post('/signup', UserController.signup);
router.delete('/signout', UserController.signout);
router.get('/getUser/:userId', UserController.getUser);
router.get('/checkEmail', UserController.checkEmail);
router.put('/update', UserController.update);
router.put('/updatePassword', UserController.updatePassword);

module.exports = router;