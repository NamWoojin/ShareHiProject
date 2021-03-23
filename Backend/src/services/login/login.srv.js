'use strict';

const pool = require('../../config/db/db_connect');
const {SECRET, jwt} = require('../../util/jwt/jwt');
const LoginQuery = require('../../models/login/login');

const basic = async (req, res) => {
    let member = req.body;
    await pool.query(LoginQuery.basic, [member.mem_email, member.mem_password], function (err, result) {
        if (err) {
          console.log(err);
          res.status(500).json({
            message: 'FAIL',
            detail: err,
            content: {},
          });
        } else if (result.length == 1) {
            const token = jwt.sign({
                username: result[0].mem_id
            }, SECRET, {
                algorithm: 'HS256',
                expiresIn: '10m'
            })
            res.status(200).json({
              message: 'SUCCESS',
              detail: err,
              content: {'token' : token},
            });
        } else if (result.length == 0) {
          return res.status(200).json({
            message: 'FAIL',
            detail: "CHECK MEM_EMAIL OR MEM_PASSWORD",
            content: {},
          });
        }
      });
};
const social = async (req, res) => {

};

module.exports = {
  basic,
  social
}