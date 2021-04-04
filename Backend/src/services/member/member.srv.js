'use strict';

const pool = require('../../config/db/db_connect');
const UserQuery = require('../../models/member/member');
const transport = require('../../util/mail/mail.transport');
const redis = require('../../config/redis/redis.emailAuth');
const bcrypt = require('bcrypt');
const async = require('async');
// const multer = require('../../util/multer/multer');

const signup = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        console.log('>>>> 비밀번호암호화');
        let member = req.body;
        bcrypt.hash(member.mem_password, 10, async function (err, hash) {
          if (err) {
            callback(err);
          } else {
            member.mem_password = hash;
            callback(null, member);
          }
        });
      },
      function (member, callback) {
        console.log('>>>> 이메일확인');
        pool.query(UserQuery.checkEmail, member.mem_email, function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(null, member);
          } else {
            callback(true, {
              message: 'FAIL',
              detail: 'DUPLICATE EMAIL',
              content: {},
            });
          }
        });
      },
      function (member, callback) {
        console.log('>>>> 회원가입');
        pool.query(UserQuery.signup, [member.mem_name, member.mem_email, member.mem_password], function (err, result) {
          if (err) {
            callback(err);
          } else {
            callback(null, {
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

const signout = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        console.log('>>>> 회원정보가져오기');
        let memId = req.query.mem_id;
        pool.query(UserQuery.getUser, memId, async function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(true, {
              message: 'FAIL',
              detail: '',
              content: {},
            });
          } else {
            callback(null, memId);
          }
        });
      },
      function (mem_id, callback) {
        console.log('>>>> 회원탈퇴');
        pool.query(UserQuery.signout, mem_id, function (err, result) {
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

const getUser = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        console.log('>>>> 회원정보가져오기');
        let memId = req.query.mem_id;
        pool.query(UserQuery.getUser, memId, async function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(true, {
              message: 'FAIL',
              detail: '',
              content: {},
            });
          } else {
            callback(true, {
              message: 'SUCCESS',
              detail: '',
              content: { member: result[0] },
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

const checkEmail = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        console.log('>>>> 이메일중복확인');
        let memEmail = req.query.mem_email;
        pool.query(UserQuery.checkEmail, memEmail, function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(true, {
              message: 'SUCCESS',
              detail: '',
              content: {},
            });
          } else {
            callback(true, {
              message: 'FAIL',
              detail: 'DUPLICATE EMAIL',
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

const update = async (req, res) => {
  res.json('회원정보수정.');
};

const checkPassword = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        console.log('>>>> 비밀번호확인');
        console.log('>>>> 아이디존재유무확인');
        let member = req.body;

        pool.query(UserQuery.getUser, member.mem_id, async function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(true, {
              message: 'FAIL',
              detail: 'CHECK MEM_ID',
              content: {},
            });
          } else {
            callback(null, member);
          }
        });
      },
      function (member, callback) {
        console.log('>>>> 비밀번호확인');
        pool.query(UserQuery.checkPassword, member.mem_id, function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            callback(true, {
              message: 'FAIL',
              detail: 'CHECK PASSWORD',
              content: {},
            });
          } else if (bcrypt.compareSync(member.mem_password, result[0].mem_password)) {
            callback(true, {
              message: 'SUCCESS',
              detail: '',
              content: {},
            });
          } else {
            callback(true, {
              message: 'FAIL',
              detail: 'CHECK PASSWORD',
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

const updatePassword = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        console.log('>>>> 비밀번호변경');
        console.log('>>>> 비밀번호암호화');
        let member = req.body;
        bcrypt.hash(member.mem_password, 10, async function (err, hash) {
          if (err) {
            callback(err);
          } else {
            member.mem_password = hash;
            callback(null, member);
          }
        });
      },
      function (member, callback) {
        console.log('>>>> 비밀번호변경');

        pool.query(UserQuery.updatePassword, [member.mem_password, member.mem_id], function (err, result) {
          if (err) {
            callback(err);
          } else if (result.affectedRows == 0) {
            callback(err);
          } else if (result.affectedRows == 1) {
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

const requireEmailAuth = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        console.log('>>>> 이메일인증번호요청');
        let member = req.body;

        let authNum = Math.random().toString().substr(2, 6);
        console.log('>>>authNum');
        console.log(authNum);

        const mailOptions = {
          from: 'filedsolar@gmail.com',
          to: member.mem_email,
          subject: '[ShareHi] 인증번호가 도착했습니다.',
          text: '오른쪽 숫자 6자리를 입력해주세요 : ' + authNum,
        };

        callback(null, member, authNum, mailOptions);
      },
      function (member, authNum, mailOptions, callback) {
        transport.sendMail(mailOptions, (err) => {
          if (err) {
            callback(err);
          } else {
            redis.set(member.mem_email, authNum);
            redis.expire(member.mem_email, 180);
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

const checkEmailAuth = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        console.log('>>>> 이메일인증번호확인');
        let member = req.body;
        redis.get(member.mem_email, function (err, value) {
          if (err) {
            callback(err);
          } else if (!value) {
            callback(true, {
              message: 'FAIL',
              detail: 'TIMEOUT',
              content: {},
            });
          } else if (value != member.authNum) {
            callback(true, {
              message: 'FAIL',
              detail: 'DIFFERNT AUTHNUM',
              content: {},
            });
          } else if (value === member.authNum) {
            redis.del(member.mem_email);

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

const upload = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        let mem_id = req.body.mem_id;
        let image = req.file;
        if (!image) {
          callback(true, {
            message: 'FAIL',
            detail: 'NOT EXIST IMAGE',
            content: {},
          });
        } else {
          let path = "https://j4f001.p.ssafy.io/profileImg/" + image.filename;
          console.log(image.filename);
          
          callback(null, path, mem_id);
          
        }
      },
      function(path, mem_id, callback) {
        pool.query(UserQuery.updateProfileImg, [path, mem_id], function (err, result) {
          if (err) {
            callback(err);
          } else if (result.affectedRows == 0) {
            callback(err);
          } else if (result.affectedRows == 1) {
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
  upload,
};
