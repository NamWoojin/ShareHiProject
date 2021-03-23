'use strict';

const express = require('express');
const router = express.Router();

const user = require('./user');
const login = require('./login');
const follow = require('./follow');


// router.use('/user', auth, user);
router.use('/user', user);
router.use('/login', login);
router.use('/follow', follow);

module.exports = router;