'use strict';

const express = require('express');
const router = express.Router();

const user = require('./user');
const login = require('./login');

const auth = require('../util/jwt/auth');

//router.use('/main', main);



router.use('/user', auth, user);
router.use('/login', login);

module.exports = router;
