'use strict';

class Member {

}

module.exports = Member;

const pool = require('../config/db_connect');

pool((err, connection) => {
    connection.query('select * from member', function(err, results) {
        if(err) {
            console.log(err);
        }
        console.log(results);
    });
})
