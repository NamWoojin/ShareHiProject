'use strict';

const pool = require('../../config/db/db_connect');
const DeviceQuery = require('../../models/device/device');
const async = require('async');

const getOnlineDevice = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        let memId = req.query.mem_id;
        pool.query(DeviceQuery.getOnlineDevice, memId, function (err, result) {
          if (err) {
            callback(err);
          } else {
            callback(true, {
              message: 'SUCCESS',
              detail: '',
              content: { device: result },
            });
          }
        });
      },
    ],
    function (err, data) {
      if (!data) {
        console.log(err);
        return res.status(500).json({
          message: 'FAIL',
          detail: err,
          content: {},
        });
      } else {
        res.status(200).json(data);
      }
    }
  );
};
const getOfflineDevice = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        let memId = req.query.mem_id;
        pool.query(DeviceQuery.getOfflineDevice, memId, function (err, result) {
          if (err) {
            callback(err);
          } else {
            callback(true, {
              message: 'SUCCESS',
              detail: '',
              content: { device: result },
            });
          }
        });
      },
    ],
    function (err, data) {
      if (!data) {
        console.log(err);
        return res.status(500).json({
          message: 'FAIL',
          detail: err,
          content: {},
        });
      } else {
        res.status(200).json(data);
      }
    }
  );  
};
const insertDevice = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        let device = req.body;
        pool.query(checkDevice.checkDevice,[device.mem_id, device.dev_name, device.dev_type], function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(null, device);
          }else {
            callback(true,{
              message: 'FAIL',
              detail: 'DUPLICATE DEV_NAME',
              content: {},
            });
          }
        });
      },
      function(device, callback) {
        pool.query(checkDevice.insertDevice, device, function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(true, {
              message: 'SUCCESS',
              detail: '',
              content: {},
            });
          }
        });
      }
    ],
    function (err, data) {
      if (!data) {
        console.log(err);
        return res.status(500).json({
          message: 'FAIL',
          detail: err,
          content: {},
        });
      } else {
        res.status(200).json(data);
      }
    }
  );  
};
const deletetDevice = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        let devId = req.query.dev_id;
        pool.query(DeviceQuery.deletetDevice, devId, function (err, result) {
          if (err) {
            callback(err);
          } else {
            callback(true, {
              message: 'SUCCESS',
              detail: '',
              content: {},
            });
          }
        });
      },
    ],
    function (err, data) {
      if (!data) {
        console.log(err);
        return res.status(500).json({
          message: 'FAIL',
          detail: err,
          content: {},
        });
      } else {
        res.status(200).json(data);
      }
    }
  );  
};
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
