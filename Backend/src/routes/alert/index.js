'use strict'

const express = require('express');
const router = express.Router();
const AlertService = require('../../services/alert/alert.srv');

router.get('/sendMSG', AlertService.sendMSG)

module.exports = router;