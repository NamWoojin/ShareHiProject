'use strict'

const UserService = require('../../services/user/user.srv');


const signup = async(req, res) => {
    let user = req.body;
    try {
        let result = UserService.signup(user);
        return res.json(result);
    } catch(err) {
        return res.status(500).json(err);
    }
}

const signout = (req, res) => {
    res.send('회원탈퇴.');
}

const getUser = (req, res) => {
    let userId = req.params;
    console.log(userId);
    try {
        let result =  UserService.getUser(userId);
        res.send('회원조회.');
        console.log(result);

        return res.json(result);
    } catch(err) {
        return res.status(500).json(err);
    }
}

const checkEmail = (req, res) => {
    res.send('회원이메일조회.');
}

const update = (req, res) => {
    res.send('회원정보수정.');
}

const updatePassword= (req, res) => {
    res.send('비밀번호 수정.');
}
module.exports = {
                    signup, 
                    signout,
                    getUser, 
                    checkEmail,
                    update,
                    updatePassword
                 };