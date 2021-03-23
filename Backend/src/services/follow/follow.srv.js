'use strict';

const pool = require('../../config/db/db_connect');
const FollowQuery = require('../../models/follow/follow');

const getFollower = async (req, res) => {
    let memId = req.query.mem_id;
    await pool.query(FollowQuery.getFollower, memId, function (err, result) {
        if (err) {
          res.status(500).json({
            message: 'FAIL',
            detail: err,
            content: {},
          });
        } else if (result.length == 0) {
          return res.status(200).json({
            message: 'SUCCESS',
            detail: 'NO FOLLOWER',
            content: {},
          });
        } else
          return res.status(200).json({
            message: 'SUCCESS',
            detail: '',
            content: { member: result },
          });
      });
    

};
const getFollowing = async (req, res) => {
    let targetMemId = req.query.target_mem_id;
    await pool.query(FollowQuery.getFollowing, targetMemId, function (err, result) {
        if (err) {
          res.status(500).json({
            message: 'FAIL',
            detail: err,
            content: {},
          });
        } else if (result.length == 0) {
          return res.status(200).json({
            message: 'SUCCESS',
            detail: 'NO FOLLOWING',
            content: {},
          });
        } else
          return res.status(200).json({
            message: 'SUCCESS',
            detail: '',
            content: { member: result },
          });
      });
};
const insertFollowing = async (req, res) => {
    let follow = req.body;

    await pool.query(FollowQuery.checkFollowing, [follow.mem_id, follow.target_mem_id], function (err, result) {
        if (err) {
            console.log(err);
            res.status(500).json({
              message: 'FAIL',
              detail: err,
              content: {},
            });
          } else if (result.length != 0) {
              res.status(200).json({
                message: 'FAIL',
                detail: "EXIST FOLLOW",
                content: {},
              });
          } else {
            pool.query(FollowQuery.insertFollowing, follow, function (err, result) {
                if (err) {
                  console.log(err);
                  res.status(200).json({
                    message: 'FAIL',
                    detail: err,
                    content: {},
                  });
                }
                res.status(200).json({
                  message: 'SUCCESS',
                  detail: "",
                  content: {},
                });
              });
          }
      });
};
const deleteFollowing = async (req, res) => {
    let memId = req.query.mem_id;
    let targetMemId = req.query.target_mem_id;

    await pool.query(FollowQuery.checkFollowing, [memId, targetMemId], function (err, result) {
        if (err) {
            console.log(err);
            res.status(500).json({
              message: 'FAIL',
              detail: err,
              content: {},
            });
          } else if (result.length == 0) {
              res.status(200).json({
                message: 'FAIL',
                detail: "NOT FOUND FOLLOW",
                content: {},
              });
          } else {
            pool.query(FollowQuery.deleteFollowing, [memId, targetMemId], function (err, result) {
                if (err) {
                  console.log(err);
                  res.status(200).json({
                    message: 'FAIL',
                    detail: err,
                    content: {},
                  });
                }
                res.status(200).json({
                  message: 'SUCCESS',
                  detail: "",
                  content: {},
                });
              });
          }
      });
   
};
module.exports = {
    getFollower,
    getFollowing,
    insertFollowing,
    deleteFollowing,
};