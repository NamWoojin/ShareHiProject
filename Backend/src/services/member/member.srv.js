'use strict';

const pool = require('../../config/db/db_connect');
const UserQuery = require('../../models/member/member');
const transport = require('../../util/mail/mail.transport');
const redis = require('../../config/redis/redis.emailAuth');
const bcrypt = require('bcrypt');

const signup = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>singup');
  let member = req.body;

  bcrypt.hash(member.mem_password, 10, function (err, hash) {
    if (err) {
      console.log(err);
      res.status(500).json({
        message: 'FAIL',
        detail: err,
        content: {},
      });
    }
    member.mem_password = hash;
  });
  // console.log('>>>member');
  // console.log(member);
  await pool.query(UserQuery.checkEmail, member.mem_email, function (err, result) {
    if (err) {
      console.log(err);
      res.status(500).json({
        message: 'FAIL',
        detail: err,
        content: {},
      });
    } else if (result.length == 0) {
      pool.query(UserQuery.signup, member, function (err, result) {
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
          detail: '',
          content: {},
        });
      });
    } else {
      res.status(200).json({
        message: 'FAIL',
        detail: 'DUPLICATE EMAIL',
        content: {},
      });
    }
  });
};

const signout = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>signout');
  let memId = req.query.mem_id;
  console.log(memId);
  await pool.query(UserQuery.getUser, memId, async function (err, result) {
    if (err) {
      // console.log(err);
      res.status(500).json({
        message: 'FAIL',
        detail: err,
        content: {},
      });
    } else if (result.length == 0) {
      res.status(200).json({
        message: 'FAIL',
        detail: '',
        content: {},
      });
    } else {
      await pool.query(UserQuery.signout, memId, function (err, result) {
        if (err) {
          res.status(500).json({
            message: 'FAIL',
            detail: err,
            content: {},
          });
        }
        res.status(200).json({
          message: 'SUCCESS',
          detail: '',
          content: {},
        });
      });
    }
  });
};

const getUser = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>getUser');
  let memId = req.query.mem_id;
  await pool.query(UserQuery.getUser, memId, function (err, result) {
    if (err) {
      res.status(500).json({
        message: 'FAIL',
        detail: err,
        content: {},
      });
    } else if (result.length == 0) {
      return res.status(200).json({
        message: 'FAIL',
        detail: '',
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

const checkEmail = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>checkEmail');
  let memEmail = req.query.mem_email;
  await pool.query(UserQuery.checkEmail, memEmail, function (err, result) {
    if (err) {
      res.status(500).json({
        message: 'FAIL',
        detail: err,
        content: {},
      });
    } else if (result.length == 0) {
      res.status(200).json({
        message: 'SUCCESS',
        detail: '',
        content: {},
      });
    } else {
      res.status(200).json({
        message: 'FAIL',
        detail: 'DUPLICATE EMAIL',
        content: {},
      });
    }
  });
};

const update = async (req, res) => {
  res.json('회원정보수정.');
};

const checkPassword = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>checkPassword');
  let member = req.body;
  await pool.query(UserQuery.getUser, member.mem_id, async function (err, result) {
    if (err) {
      res.status(500).json({
        message: 'FAIL',
        detail: err,
        content: {},
      });
    } else if (result.length == 0) {
      return res.status(200).json({
        message: 'FAIL',
        detail: 'CHECK MEM_ID',
        content: {},
      });
    } else {
      await pool.query(UserQuery.checkPassword, member.mem_id, function (err, result) {
        if (err) {
          console.log(err);
          return res.status(500).json({
            message: 'FAIL',
            detail: err,
            content: {},
          });
        } else if (result.length == 0) {
          return res.status(200).json({
            message: 'FAIL',
            detail: 'CHECK PASSWORD',
            content: {},
          });
        } else if (bcrypt.compareSync(member.mem_password, result[0].mem_password)) {
          return res.status(200).json({
            message: 'SUCCESS',
            detail: '',
            content: {},
          });
        } else {
          return res.status(200).json({
            message: 'FAIL',
            detail: 'CHECK PASSWORD',
            content: {},
          });
        }
      });
    }
  });
};

const updatePassword = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>updatePassword');
  let member = req.body;
  bcrypt.hash(member.mem_password, 10, async function (err, hash) {
    if (err) {
      console.log(err);
      res.status(500).json({
        message: 'FAIL',
        detail: err,
        content: {},
      });
    } else {
    console.log(">>>1")
    member.mem_password = hash;
    await pool.query(UserQuery.updatePassword, [member.mem_password, member.mem_id], function (err, result) {
      console.log(">>>2")
  
      if (err) {
        console.log(err);
        return res.status(500).json({
          message: 'FAIL',
          detail: err,
          content: {},
        });
      } else if (result.affectedRows == 0) {
        return res.status(500).json({
          message: 'FAIL',
          detail: err,
          content: {},
        });
      } else if (result.affectedRows == 1) {
        return res.status(200).json({
          message: 'SUCCESS',
          detail: '',
          content: {},
        });
      }
    });
    }
  });
  
};

const requireEmailAuth = async (req, res) => {
  let member = req.body;
  // console.log('>>>check email');
  // console.log(member.mem_email);

  let authNum = Math.random().toString().substr(2, 6);
  console.log('>>>authNum');
  console.log(authNum);

  const mailOptions = {
    from: 'filedsolar@gmail.com',
    to: member.mem_email,
    subject: '[ShareHi] 인증번호가 도착했습니다.',
    text: '오른쪽 숫자 6자리를 입력해주세요 : ' + authNum,
  };

  await transport.sendMail(mailOptions, (err) => {
    if (err) {
      console.log(err);
      res.status(500).json({
        message: 'FAIL',
        detail: err,
        content: {},
      });
    } else {
      redis.set(member.mem_email, authNum);
      redis.expire(member.mem_email, 180);
      res.status(200).json({
        message: 'SUCCESS',
        detail: '',
        content: {},
      });
    }
    transport.close();
  });
};

const checkEmailAuth = async (req, res) => {
  let member = req.body;
  redis.get(member.mem_email, function (err, value) {
    if (err) {
      throw err;
    } else if (!value) {
      res.status(500).json({
        message: 'FAIL',
        detail: 'TIMEOUT',
        content: {},
      });
    } else if (value != member.authNum) {
      res.status(200).json({
        message: 'FAIL',
        detail: 'DIFFERNT AUTHNUM',
        content: {},
      });
    } else if (value === member.authNum) {
      res.status(200).json({
        message: 'SUCCESS',
        detail: '',
        content: {},
      });
      redis.del(member.mem_email);
    }
  });
};

module.exports = {
  signup,
  signout,
  getUser,
  checkEmail,
  update,
  updatePassword,
  requireEmailAuth,
  checkEmailAuth,
  checkPassword,
};
