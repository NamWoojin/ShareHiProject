const signup = (req, res) => {
    res.send('회원가입.');
}
const login = (req, res) => {
    res.send('로그인.');
}
module.exports = {signup, login};