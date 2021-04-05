'use strict';

const pool = require('../../config/db/db_connect');
const FollowQuery = require('../../models/follow/follow');
const async = require('async');

const getFollower = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        let memId = req.query.mem_id;
        pool.query(FollowQuery.getFollower, memId, function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(true, {
              message: 'SUCCESS',
              detail: 'NO FOLLOWER',
              content: {},
            });
          } else {
            callback(true, {
              message: 'SUCCESS',
              detail: '',
              content: { member: result },
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
const getFollowing = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        let targetMemId = req.query.target_mem_id;
        pool.query(FollowQuery.getFollowing, targetMemId, function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(true, {
              message: 'SUCCESS',
              detail: 'NO FOLLOWING',
              content: {},
            });
          } else {
            callback(true, {
              message: 'SUCCESS',
              detail: '',
              content: { member: result },
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
const insertFollowing = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        let follow = req.body;
        pool.query(FollowQuery.checkFollowing, [follow.mem_id, follow.target_mem_id], function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length != 0) {
            callback(true, {
              message: 'FAIL',
              detail: 'EXIST FOLLOW',
              content: {},
            });
          } else {
            callback(null, follow);
          }
        });
      },
      function (follow, callback) {
        pool.query(FollowQuery.insertFollowing, follow, function (err, result) {
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
const deleteFollowing = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        let memId = req.query.mem_id;
        let targetMemId = req.query.target_mem_id;
        pool.query(FollowQuery.checkFollowing, [memId, targetMemId], function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(true, {
              message: 'FAIL',
              detail: 'NOT FOUND FOLLOW',
              content: {},
            });
          } else {
            callback(null, memId, targetMemId);
          }
        });
      },
      function (memId, targetMemId, callback) {
        pool.query(FollowQuery.deleteFollowing, [memId, targetMemId], function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(true, {
              message: 'FAIL',
              detail: 'NOT FOUND FOLLOW',
              content: {},
            });
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

const searchMember = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        let searchWord = "%" + req.query.searchWord + "%";
        pool.query(FollowQuery.searchMember, [searchWord, searchWord], function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(true, {
              message: 'FAIL',
              detail: 'NOT FOUND MEMBER',
              content: {},
            });
          } else {
            callback(true, {
              message: 'SUCCESS',
              detail: '',
              content: { member: result },
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
  getFollower,
  getFollowing,
  insertFollowing,
  deleteFollowing,
  searchMember,
};
