'use strict';

const pool = require('../../config/db/db_connect');
const ShareQuery = require('../../models/device/device');
const async = require('async');

const getOnlineDevice = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        console.log('>>>> 온라인디바이스가져오기');
        let memId = req.query.mem_id;
        pool.query(ShareQuery.getOnlineDevice, memId, function (err, result) {
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


module.exports = {
  getOnlineDevice,

};
