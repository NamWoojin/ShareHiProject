'use strict';

const express = require('express');
const router = express.Router();
const LoginService = require('../../services/login/login.srv');

router.post('/basic', LoginService.basic);
router.post('/social', LoginService.social);
router.get('/logout', LoginService.logout);

module.exports = router;
