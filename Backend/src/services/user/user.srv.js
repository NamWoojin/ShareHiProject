'use strict';

const pool = require('../../config/db_connect');
const UserQuery = require('../../models/member');

const signup = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>singup');
  let member = req.body;
  console.log('>>>mem_email');
  console.log(member.mem_email);
  await pool.query(UserQuery.checkEmail, member.mem_email, function (err, result) {
    if (err) {
      console.log(err);
      res.status(500).send(err);
    } else if (result.length == 0) {
      pool.query(UserQuery.signup, member, function (err, result) {
        if (err) {
          console.log(err);
          res.status(500).send(err);
        }
        console.log('>>>success signin');
        res.status(200);
      });
    } else {
      res.status(200).send('Email Duplicatoin');
    }
  });
};

const signout = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>signout');
  let member = req.params;
  await pool.query(UserQuery.getUser, member['memId'], function (err, result) {
    if (err) {
      console.log(err);
      res.status(500).send(err);
    } else if (result.length == 1) {
      pool.query(UserQuery.signout, member['memId'], function (err, result) {
        if (err) {
          console.log(err);
          res.status(500).send(err);
        }
        console.log('>>>success signout');
        res.status(200);
      });
    } else {
      res.status(200).send('Check mem_id');
    }
    return res.json(result);
  });
};

const getUser = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>signout');
  let member = req.params;
  await pool.query(UserQuery.getUser, member['memId'], function (err, results) {
    if (err) {
      console.log(err);
    }

    console.log(results[0]);
    console.log('>>>test end');

    return res.json(200, results);
  });
};

const checkEmail = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>checkEmail');
  let member = req.params;
  console.log('>>>mem_email');
  console.log(member.mem_email);
  await pool.query(UserQuery.checkEmail, member['mem_email'], function (err, result) {
    if (err) {
      console.log(err);
      res.status(500).send(err);
    } else if (result.length == 0) {
      res.status(200);
    } else {
      res.status(200).send('Email Duplicatoin');
    }
  });
};

const update = (req, res) => {
  res.send('회원정보수정.');
};

const updatePassword = (req, res) => {
  res.send('비밀번호 수정.');
};
module.exports = {
  signup,
  signout,
  getUser,
  checkEmail,
  update,
  updatePassword,
};
