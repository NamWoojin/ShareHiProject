'use strict';

const pool = require('../../config/db/db_connect');
const {SECRET, jwt} = require('../../util/jwt/jwt');
const LoginQuery = require('../../models/login/login');

const login = async (req, res) => {
    let member = req.body;
    await pool.query(LoginQuery.login, [member.mem_email, member.mem_password], function (err, result) {
        if (err) {
          console.log(err);
          res.status(500).json(err);
        } else if (result.length == 1) {
            const token = jwt.sign({
                username: result[0].mem_id
            }, SECRET, {
                algorithm: 'HS256',
                expiresIn: '10m'
            })
            res.status(200).json({'token' : token});
        } else if (result.length == 0) {
          return res.status(500).json('FAIL');
        }
      });
};

module.exports = {
    login,
}