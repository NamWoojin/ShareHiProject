'use strict'

const basic = 'select mem_id, mem_name, mem_email, mem_registDevice, mem_image, mem_joinDate from member where mem_email=? and mem_password=?';
const social = 'select * from member where mem_email=? and mem_password=?';
const socialCheckEmail = 'select mem_id, mem_name, mem_email, mem_registDevice, mem_image, mem_joinDate from member where mem_email=?';
const insertSocial = 'insert into member set ?';
module.exports = {
    basic,
    social,
    socialCheckEmail,
    insertSocial
}