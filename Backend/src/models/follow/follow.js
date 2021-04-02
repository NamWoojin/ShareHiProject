'use strict';


const getFollower = 'select mem_id, mem_name, mem_email, mem_image \
                       from member \
                      where mem_id in (select mem_id from follow where target_mem_id=?)';
const getFollowing = 'select mem_id, mem_name, mem_email, mem_image \
                        from member \
                       where mem_id in (select target_mem_id from follow where mem_id=?)';
const insertFollowing = 'insert into follow \
                            set ?';
const deleteFollowing = 'delete from follow \
                          where mem_id=? and target_mem_id=?';
const checkFollowing = 'select * \
                          from follow \
                         where mem_id=? and target_mem_id=?'
const searchMember = 'select mem_id, mem_name, mem_email, mem_image \
                        from member \
                       where mem_email like ? or mem_name like ?';
module.exports = {
  getFollower,
  getFollowing,
  insertFollowing,
  deleteFollowing,
  checkFollowing,
  searchMember
};
