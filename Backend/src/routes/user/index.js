'use strict';

const express = require('express');
const router = express.Router();
const controller = require('./user.ctrl');

/* GET users listing. */
router.get('/signup', controller.signup);
router.get('/login', controller.login);

module.exports = router;