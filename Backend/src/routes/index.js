'use strict';

const express = require('express');
const router = express.Router();

const user = require('./user')

//router.use('/main', main);
router.use('/user', user);

module.exports = router;