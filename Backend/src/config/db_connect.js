'use strict';

var mysql = require('mysql');
var config = require('./db_config'); // ./는 현재 디렉토리를 나타냅니다
var pool = mysql.createPool({
  host: config.host,
  user: config.user,
  password: config.password,
  database: config.database,
});

const getConn = function (callback) {
  pool.getConnection(function (err, connection) {
    callback(err, connection);
  });
};

module.exports = getConn;
