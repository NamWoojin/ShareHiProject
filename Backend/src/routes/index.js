'use strict';

const express = require('express');
const router = express.Router();

const member = require('./member');
const login = require('./login');
const follow = require('./follow');
const device = require('./device');

// router.use('/user', auth, user);
router.use('/member', member);
router.use('/login', login);
router.use('/follow', follow);
router.use('/device', device);

module.exports = router;