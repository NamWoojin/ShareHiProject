'use strict';

const express = require('express');
const router = express.Router();

const member = require('./member');
const login = require('./login');
const follow = require('./follow');
const device = require('./device');
const share = require('./share');
const alert = require('./alert')

router.use('/member', member);
router.use('/login', login);
router.use('/follow', follow);
router.use('/device', device);
router.use('/share', share);
router.use('/alert', alert);

module.exports = router;
