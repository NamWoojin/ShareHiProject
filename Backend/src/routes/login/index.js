'use strict';

const express = require('express');
const router = express.Router();
const LoginService = require('../../services/login/login.srv');

router.post('/login', LoginService.login);

module.exports = router;
