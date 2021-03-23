'use strict'

const login = 'select * from member where mem_email=? and mem_password=?';

module.exports = {
    login,
}