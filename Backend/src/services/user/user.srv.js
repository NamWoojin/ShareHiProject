'use strict';

const pool = require('../../config/db_connect');
const UserQuery = require('../../models/member');
const transport = require('../../config/mail.transport');
const redis = require('../../config/redis.emailAuth');

const signup = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>singup');
  let member = req.body;
  // console.log('>>>mem_email');
  // console.log(member.mem_email);
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
        res.status(200).send('SUCCESS');
      });
    } else {
      res.status(200).send('DUPLICATION');
    }
  });
};

const signout = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>signout');
  let member = req.params;
  await pool.query(UserQuery.getUser, member['memId'], async function (err, result) {
    if (err) {
      console.log(err);
      res.status(500).send(err);
    } else if (result.length == 0) {
      res.status(200).send('FAIL');
    } else {
      await pool.query(UserQuery.signout, member['memId'], function (err, result) {
        if (err) {
          console.log(err);
          res.status(500).send(err);
        }
        // console.log('>>>success signout');
        res.status(200).send('SUCCESS');
      });
    }
    // return res.json(result);
  });
};

const getUser = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>getUser');
  let member = req.params;
  await pool.query(UserQuery.getUser, member['memId'], function (err, result) {
    if (err) {
      console.log(err);
      res.status(500).send(err);
    } else if (result.length == 0) {
      return res.status(500).send('FAIL');
    } else return res.status(200).json(result);
  });
};

const checkEmail = async (req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>checkEmail');
  let member = req.params;
  console.log('>>>memEmail');
  console.log(member.memEmail);
  await pool.query(UserQuery.checkEmail, member.memEmail, function (err, result) {
    if (err) {
      console.log(err);
      res.status(500).send(err);
    } else if (result.length == 0) {
      res.status(200).send('SUCCESS');
    } else {
      res.status(500).send('FAIL');
    }
  });
};

const update = async (req, res) => {
  res.send('회원정보수정.');
};



const checkPassword = async(req, res) => {
  console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>checkPassword');
  let member = req.body;

  await pool.query(UserQuery.checkPassword, member.mem_id, function (err, result) {
    if (err) {
      console.log(err);
      return res.status(500).send(err);
    } else if (result.length == 0) {
      return res.status(500).send('FAIL');
    } else if(result[0].mem_password == member.mem_password) {
      return res.status(200).send('SUCCESS');
    } else {
      return res.status(200).send('DIF_PW');

    }
  });

}
const updatePassword = async (req, res) => {
 console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>updatePassword');
  let member = req.body;

  await pool.query(UserQuery.updatePassword, [member.mem_password, member.mem_id], function (err, result) {
    if (err) {
      console.log(err);
      return res.status(500).send(err);
    } else if (result.affectedRows == 0) {
      return res.status(500).send('FAIL');
    } else if(result.affectedRows ==1) {
      return res.status(200).send('SUCCESS');
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
      res.status(500).send(err);
    } else {
      redis.on('error', function (err) {
        console.log('Error ' + err);
      });
      redis.set(member.mem_email, authNum);
      redis.expire(member.mem_email, 180);
      redis.quit();
      res.status(200).send('SUCCESS');
    }
    transport.close();
  });
};

const checkEmailAuth = async (req, res) => {
  let member = req.body;
  // console.log('>>>check email');
  // console.log(member.mem_email);
  // console.log(member.authNum);

  redis.on('error', async function (err) {
    console.log('Error ' + err);
  });
  await redis.get(member.mem_email, function (err, value) {
    if (err) {
      throw err;
    } else if (!value) {
      res.status(500).send('TIMEOUT');
     } else if (value != member.authNum) {
      res.status(200).send('DIF_AUTHNUM');
    } else if (value === member.authNum) {
      res.status(200).send('SUCCESS');
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
