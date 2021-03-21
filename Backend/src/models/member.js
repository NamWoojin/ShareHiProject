'use strict';

const signup = 'insert into member set ?';
const signout = 'delete from member where mem_id=?';
const getUser = 'select * from member where mem_id=?';
const checkEmail = 'select * from member where mem_email=?';
const update = '';
const updatePassword = '';
const checkPassword = '';
module.exports = {
  signup,
  signout,
  getUser,
  checkEmail,
  update,
  updatePassword,
  checkPassword,
};
