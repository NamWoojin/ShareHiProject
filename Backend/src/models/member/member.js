'use strict';

const signup = 'insert into member (mem_name, mem_email, mem_password) \
                values (?, ?, ?)';
const signout = 'delete from member \
                  where mem_id=?';
const getUser = 'select mem_id, mem_name, mem_email, mem_registDevice, mem_image, mem_joinDate \
                   from member \
                  where mem_id=?';
const checkEmail = 'select mem_id, mem_name, mem_email, mem_registDevice, mem_image, mem_joinDate \
                      from member \
                    where mem_email=?';
const update = '';
const checkPassword = 'select * \
                         from member \
                        where mem_id=?';
const updatePassword = 'update member \
                           set mem_password=? \
                         where mem_id=?';
const updateProfileImg = 'update member \
                            set mem_image=? \
                          where mem_id=?';
module.exports = {
  signup,
  signout,
  getUser,
  checkEmail,
  update,
  updatePassword,
  checkPassword,
  updateProfileImg,
};
