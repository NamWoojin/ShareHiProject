'use strict';

const express = require('express');
const router = express.Router();
const ShareService = require('../../services/share/share.srv')

router.post('/insertShareInfo', ShareService.insertShareInfo);

module.exports = router;