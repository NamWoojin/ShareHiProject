'use strict';

const pool = require('../../config/db/db_connect');
const DeviceQuery = require('../../models/device/device');

const getOnlineDevice = async (req, res) => {
  let memId = req.query.mem_id;
  await pool.query(DeviceQuery.getOnlineDevice, memId, function (err, result) {
    if (err) {
      return res.status(500).json({
        message: 'FAIL',
        detail: err,
        content: {},
      });
    }
    return res.status(200).json({
      message: 'SUCCESS',
      detail: '',
      content: { device: result },
    });
  });
};
const getOfflineDevice = async (req, res) => {
    let memId = req.query.mem_id;
    await pool.query(DeviceQuery.getOfflineDevice, memId, function (err, result) {
        if (err) {
          return res.status(500).json({
            message: 'FAIL',
            detail: err,
            content: {},
          });
        }
        return res.status(200).json({
          message: 'SUCCESS',
          detail: '',
          content: { device: result },
        });
      });
};
const insertDevice = async (req, res) => {
    let device = req.body;

    await pool.query(checkDevice.checkDevice,[device.mem_id, device.dev_name, device.dev_type], function (err, result) {
        if (err) {
          console.log(err);
          return res.status(500).json({
            message: 'FAIL',
            detail: err,
            content: {},
          });
        } else if (result.length == 0) {
          pool.query(checkDevice.insertDevice, device, function (err, result) {
            if (err) {
              console.log(err);
              return res.status(200).json({
                message: 'FAIL',
                detail: err,
                content: {},
              });
            }
            return res.status(200).json({
              message: 'SUCCESS',
              detail: '',
              content: {},
            });
          });
        } else {
            return res.status(200).json({
            message: 'FAIL',
            detail: 'DUPLICATE DEV_NAME',
            content: {},
          });
        }
    });
};
const deletetDevice = async (req, res) => {};
const checkDevice = async (req, res) => {};
const updateStatus = async (req, res) => {};

module.exports = {
  getOnlineDevice,
  getOfflineDevice,
  insertDevice,
  deletetDevice,
  checkDevice,
  updateStatus,
};
