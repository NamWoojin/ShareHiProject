'use strict';

const express = require('express');
const router = express.Router();
const LoginService = require('../../services/login/login.srv');

router.post('/basic', LoginService.basic);
router.post('/social', LoginService.social);

module.exports = router;
