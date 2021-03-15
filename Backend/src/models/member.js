'use strict';

var mysql = require('mysql');
var conn = mysql.createConnection({
    host     : 'j4f001.p.ssafy.io',
    port     : '3306',
    user     : 'ssafy_solar',
    password : 'solar123@',
    database : 'solar' 
})

conn.connect();

conn.query('select * from member', function(err, results, fields) {
    if(err) {
        console.log(err);
    }
    console.log(results);
});