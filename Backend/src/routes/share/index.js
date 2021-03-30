'use strict';

const express = require('express');
const router = express.Router();
const ShareService = require('../../services/share/share.srv')

router.get('/getOnlineDevice', ShareService.getOnlineDevice);




module.exports = router;