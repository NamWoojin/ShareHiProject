'use strict';

var mysql = require('mysql');
var dbConfig = mysql.createConnection({
    host     : 'j4f001.p.ssafy.io',
    port     : '3306',
    user     : 'ssafy_solar',
    password : 'solar123@',
    database : 'solar' 
})

module.exports = mysql.createPool(dbConfig);