'use strict';

const express = require('express');
const router = express.Router();
const DeviceService = require('../../services/device/device.srv')

router.get('/getOnlineDevice', DeviceService.getOnlineDevice);
router.get('/getOfflineDevice', DeviceService.getOfflineDevice);
router.post('/insertDevice', DeviceService.insertDevice);
router.delete('/deletetDevice', DeviceService.deletetDevice);
router.get('/checkDevice', DeviceService.checkDevice);
router.put('/updateStatus', DeviceService.updateStatus);



module.exports = router;