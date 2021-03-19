'use strict';

const signup = 'insert into member set ?';
const getUser = 'select * from member where mem_id=?';

module.exports = {
  signup,
  getUser,
};
// const pool = require('../config/db_connect');
// pool.query('select * from member where mem_id=?', 1, function (err, results) {
//   if (err) {
//     console.log(err);
//   }

//   console.log(results);
// });
// pool((err, connection) => {
//     connection.query('select * from member', function(err, results) {
//         if(err) {
//             console.log(err);
//         }

//         console.log(results);
//     });
// })
