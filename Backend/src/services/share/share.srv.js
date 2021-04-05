'use strict';

const pool = require('../../config/db/db_connect');
const ShareQuery = require('../../models/device/device');
const async = require('async');

const insertShareInfo = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
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
    insertShareInfo,

};
