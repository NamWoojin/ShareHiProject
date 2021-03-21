'use strict';

const pool = require('../../config/db_connect');
const UserQuery = require('../../models/member');
const transport = require('../../config/mail.transport');
const redis = require('../../config/redis.emailAuth');

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

const update = async (req, res) => {
  res.send('회원정보수정.');
};

const updatePassword = async (req, res) => {
  res.send('비밀번호 수정.');
};

const requireEmailAuth = async (req, res) => {
  let member = req.body;
  console.log('>>>check email');
  console.log(member.mem_email);

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
      res.status(500).send(err);
    } else {
      redis.on('error', function (err) {
        console.log('Error ' + err);
      });
      redis.set(member.mem_email, authNum);
      redis.expire(member.mem_email, 180);
      redis.quit();
      res.status(200);
    }
    transport.close();
  });

  res.status(200);
};

const checkEmailAuth = async (req, res) => {
  let member = req.body;
  // console.log('>>>check email');
  // console.log(member.mem_email);
  // console.log(member.authNum);

  redis.on('error', function (err) {
    console.log('Error ' + err);
  });

  await redis.get(member.mem_email, function (err, value) {
    if (err) {
      redis.quit();
      throw err;
    } else if(!value) {
      res.status(500).send('Timeout');
      redis.quit();
    }else if (value != member.authNum) {
      redis.quit();
      res.status(200).send('Different AuthNum');
    } else if (value === member.authNum){
      redis.quit();
      res.status(200).send('200');
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
};
