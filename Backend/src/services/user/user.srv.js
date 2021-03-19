'use strict';

const pool = require('../../config/db_connect');
const UserQuery = require('../../models/member');

module.exports = {
  signup: async (user) => {
    try {
      let data = pool.query(UserQuery.signup, user);
      return data[0];
    } catch (err) {
      console.log(err);
      throw Error(err);
    }
  },
  getUser: async (userId) => {
    try {
      let data = pool.query(UserQuery.getUser, userId);
      console.log(">>>")
      console.log(data);
    //   pool.query('select * from member where mem_id=?', 1, function (err, results) {
    //     if (err) {
    //       console.log(err);
    //     }

    //     console.log(results);
    //   });
      return data[0];
    } catch (err) {
      console.log(err);
      throw Error(err);
    }
  },
};
