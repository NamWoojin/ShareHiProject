'use strict';

const pool = require('../../config/db/db_connect');
const { SECRET, jwt } = require('../../util/jwt/jwt');
const LoginQuery = require('../../models/login/login');
const bcrypt = require('bcrypt');

const basic = async (req, res) => {
  let member = req.body;

  await pool.query(LoginQuery.checkPassword, member.mem_email, function (err, result) {
    if (err) {
      console.log(err);
      return res.status(500).json({
        message: 'FAIL',
        detail: err,
        content: {},
      });
    } else if (result.length == 1 && bcrypt.compareSync(member.mem_password, result[0].mem_password)) {
      pool.query(LoginQuery.basic, member.mem_email, function (err, result) {
        if (err) {
          console.log(err);
          return res.status(500).json({
            message: 'FAIL',
            detail: err,
            content: {},
          });
        } else {
          const token = jwt.sign(
            {
              username: member.mem_email,
            },
            SECRET,
            {
              algorithm: 'HS256',
              expiresIn: '10m',
            }
          );
          return res.status(200).json({
            message: 'SUCCESS',
            detail: '',
            content: {
              token: token,
              member: result[0],
            },
          });
        }
      });
    } else {
      return res.status(200).json({
        message: 'FAIL',
        detail: 'CHECK MEM_EMAIL OR MEM_PASSWORD',
        content: {},
      });
    }
  });
};
const social = async (req, res) => {
  let member = req.body;
  if (!member.mem_image) member.mem_image = 'default.img';

  bcrypt.hash(member.mem_email, 10, async function (err, hash) {
    if (err) {
      console.log(err);
      res.status(500).json({
        message: 'FAIL',
        detail: err,
        content: {},
      });
    }
    console.log(1);

    member.mem_password = hash;

    await pool.query(LoginQuery.socialCheckEmail, member.mem_email, function (err, result) {
      console.log(2);
      if (err) {
        console.log(err);
        return res.status(500).json({
          message: 'FAIL',
          detail: err,
          content: {},
        });
      } else if (result.length == 0) {
        console.log('socail login');
        pool.query(LoginQuery.insertSocial, member, function (err, result) {
          if (err) {
            console.log(err);
            return res.status(500).json({
              message: 'FAIL',
              detail: err,
              content: {},
            });
          }
        });
      }
      pool.query(LoginQuery.basic, member.mem_email, function (err, result) {
        if (err) {
          console.log(err);
          return res.status(500).json({
            message: 'FAIL',
            detail: err,
            content: {},
          });
        } else {
          const token = jwt.sign(
            {
              username: member.mem_email,
            },
            SECRET,
            {
              algorithm: 'HS256',
              expiresIn: '10m',
            }
          );
          return res.status(200).json({
            message: 'SUCCESS',
            detail: '',
            content: {
              token: token,
              member: result[0],
            },
          });
        }
      });
    });
  });
  
};

module.exports = {
  basic,
  social,
};
