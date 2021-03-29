'use strict'

const checkPassword = 'select * \
                         from member \
                        where mem_email=?'
const checkDevice = 'select dev_id \
                       from device \
                      where mem_id=? and dev_ad_id=?'
const insertDevice = 'insert into device (dev_name, mem_id, status, dev_ad_id)\
                         values (?,?,?,?)';
const socialCheckEmail = 'select mem_id, mem_name, mem_email, mem_registDevice, mem_image, mem_joinDate \
                            from member \
                           where mem_email=?';
const logout = `update device \
                   set status='OFF' \
                 where mem_id=? and dev_id=?`;        
                           



module.exports = {
    socialCheckEmail,
    checkPassword,
    checkDevice,
    insertDevice,
    logout
}