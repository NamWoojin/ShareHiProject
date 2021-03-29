'use strict';

const pool = require('../../config/db/db_connect');
const { SECRET, jwt } = require('../../util/jwt/jwt');
const LoginQuery = require('../../models/login/login');
const bcrypt = require('bcrypt');
const async = require('async');

const basic = (req, res) => {
  async.waterfall(
    [
      function (callback) {
        console.log('>>>>회원정보가져오기');

        let member = req.body;
        pool.query(LoginQuery.checkPassword, member.mem_email, (err, result) => {
          if (err) {
            callback(err);
          } else {
            callback(null, result, member);
          }
        });
      },
      function (result_mem, member, callback) {
        if (result_mem.length == 1 && bcrypt.compareSync(member.mem_password, result_mem[0].mem_password)) {
          if (member.ad_id) {
            console.log('>>>>비밀번호확인성공');

            // 기기가 등록되어있는지 확인
            pool.query(LoginQuery.checkDevice, [result_mem[0].mem_id, member.ad_id], (err, result) => {
              if (err) {
                callback(err);
              }
              // 등록되어있지않다면 등록
              else if (result.length == 0) {
                console.log('>>>>기기등록');

                let dev_name = member.ad_id;
                let mem_id = result_mem[0].mem_id;
                let status = 'ON';
                let dev_ad_id = member.ad_id;
                pool.query(LoginQuery.insertDevice, [dev_name, mem_id, status, dev_ad_id], (err, result) => {
                  if (err) {
                    callback(err);
                  } else {
                    callback(null, result_mem[0], member);
                  }
                });
              } else {
                console.log('>>>>기기존재');
                callback(null, result_mem[0], member);
              }
            });
          } else {
            callback(null, result_mem[0], member);
          }
        } else {
          callback(true, {
            message: 'FAIL',
            detail: 'CHECK MEM_EMAIL OR MEM_PASSWORD',
            content: {},
          });
        }
      },
      function (result_mem, member, callback) {
        console.log('>>>>디바이스정보가져오기');
        pool.query(LoginQuery.checkDevice, [result_mem.mem_id, member.ad_id], (err, result) => {
          if (err) {
            callback(err);
          } else {
            callback(null, result_mem, result[0]);
          }
        });
      },
      function (result_mem, result_dev, callback) {
        console.log('>>>>토큰발급');
        const token = jwt.sign(
          {
            username: result_mem.mem_email,
          },
          SECRET,
          {
            algorithm: 'HS256',
            expiresIn: '10m',
          }
        );
        if (!result_dev) {
          callback(true, {
            message: 'SUCCESS',
            detail: '',
            content: {
              token: token,
              member: {
                mem_id: result_mem.mem_id,
                mem_name: result_mem.mem_name,
                mem_email: result_mem.mem_email,
                mem_registDevice: result_mem.mem_registDevice,
                mem_image: result_mem.mem_image,
                mem_joinDate: result_mem.mem_joinDate,
              },
            },
          });
        } else {
          callback(true, {
            message: 'SUCCESS',
            detail: '',
            content: {
              token: token,
              member: {
                mem_id: result_mem.mem_id,
                mem_name: result_mem.mem_name,
                mem_email: result_mem.mem_email,
                mem_registDevice: result_mem.mem_registDevice,
                mem_image: result_mem.mem_image,
                mem_joinDate: result_mem.mem_joinDate,
                dev_id: result_dev.dev_id,
              },
            },
          });
        }
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

const social = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        console.log('>>>>회원정보가져오기');

        let member = req.body;
        if (!member.mem_image) member.mem_image = 'default.img';

        pool.query(LoginQuery.socialCheckEmail, member.mem_email, function (err, result) {
          if (err) {
            callback(err);
          } else if (result.length == 0) {
            console.log('>>>> 소셜회원등록');
            pool.query(LoginQuery.insertSocial, member, function (err, result) {
              if (err) {
                callback(err);
              } else {
                callback(null, member);
              }
            });
          } else {
            console.log('>>>> 소셜회원존재');
            callback(null, member);
          }
        });
      },
      function (member, callback) {
        console.log('>>>> 소셜회원정보가져오기');
        pool.query(LoginQuery.socialCheckEmail, member.mem_email, function (err, result) {
          if (err) {
            callback(err);
          } else {
            callback(null, result[0], member);
          }
        });
      },
      function (result_mem, member, callback) {
        console.log('>>>> 기기등록확인');

        if (member.ad_id) {
          // 기기가 등록되어있는지 확인
          pool.query(LoginQuery.checkDevice, [result_mem.mem_id, member.ad_id], (err, result) => {
            if (err) {
              callback(err);
            }
            // 등록되어있지않다면 등록
            else if (result.length == 0) {
              console.log('>>>>기기등록');

              let dev_name = member.ad_id;
              let mem_id = result_mem.mem_id;
              let status = 'ON';
              let dev_ad_id = member.ad_id;
              pool.query(LoginQuery.insertDevice, [dev_name, mem_id, status, dev_ad_id], (err, result) => {
                if (err) {
                  callback(err);
                } else {
                  callback(null, result_mem, member);
                }
              });
            } else {
              console.log('>>>>기기존재');
              callback(null, result_mem, member);
            }
          });
        }
      },
      function (result_mem, member, callback) {
        console.log('>>>>디바이스정보가져오기');
        pool.query(LoginQuery.checkDevice, [result_mem.mem_id, member.ad_id], (err, result) => {
          if (err) {
            callback(err);
          } else {
            callback(null, result_mem, result[0]);
          }
        });
      },
      function (result_mem, result_dev, callback) {
        console.log('>>>>토큰발급');
        const token = jwt.sign(
          {
            username: result_mem.mem_email,
          },
          SECRET,
          {
            algorithm: 'HS256',
            expiresIn: '10m',
          }
        );

        callback(true, {
          message: 'SUCCESS',
          detail: '',
          content: {
            token: token,
            member: {
              mem_id: result_mem.mem_id,
              mem_name: result_mem.mem_name,
              mem_email: result_mem.mem_email,
              mem_registDevice: result_mem.mem_registDevice,
              mem_image: result_mem.mem_image,
              mem_joinDate: result_mem.mem_joinDate,
              dev_id: result_dev.dev_id,
            },
          },
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

const logout = async (req, res) => {
  async.waterfall(
    [
      function (callback) {
        console.log('>>> logout');
        let mem_id = req.query.mem_id;
        let dev_id = req.query.dev_id;
        pool.query(LoginQuery.logout, [mem_id, dev_id], (err, result) => {
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
module.exports = {
  basic,
  social,
  logout,
};
