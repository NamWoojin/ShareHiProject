'use strict';

const hello = (req, res) => {
    res.send('홈화면입니다.');
}
const login = (req, res) => {
    res.send('로그인.');
}
module.exports = {hello, login};