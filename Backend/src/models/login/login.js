'use strict'

const basic = 'select * from member where mem_email=? and mem_password=?';
const social = 'select * from member where mem_email=? and mem_password=?';
const socialCheckEmail = 'select * from member where mem_email=?';
const insertSocial = 'insert into member set ?';
module.exports = {
    basic,
    social,
    socialCheckEmail,
    insertSocial
}