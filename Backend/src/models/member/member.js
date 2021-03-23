'use strict';

const signup = 'insert into member set ?';
const signout = 'delete from member where mem_id=?';
const getUser = 'select * from member where mem_id=?';
const checkEmail = 'select * from member where mem_email=?';
const update = '';
const checkPassword = 'select * from member where mem_id=?';
const updatePassword = 'update member set mem_password=? where mem_id=?';
module.exports = {
  signup,
  signout,
  getUser,
  checkEmail,
  update,
  updatePassword,
  checkPassword,
};
