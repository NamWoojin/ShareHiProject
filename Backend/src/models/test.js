'use strict';

const pool = require('../config/db_connect');

pool.query('select * from member where mem_id=?', 1, function (err, results) {
  if (err) {
    console.log(err);
  }

  console.log(results);
});

// let data = pool.query('select * from member where mem_id=?', 1);
// console.log('>>>');
// console.log(data);
