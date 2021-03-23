'use strict';

const SECRET = "SOLAR";
const jwt = require('jsonwebtoken');

const auth = async function (req, res, next) {
  const token = req.header('x-access-token');
//   console.log('>>>token');
//   console.log(token);
  // 토큰 없음
  if (!token) return res.status(500).json('Not Token');
  // decode
  await jwt.verify(token, SECRET, (err, decoded) => {
    if (err) {
      return res.status(500).json('Not Token');
    }
    console.log('>>>decoded');
    console.log(decoded);
    next();
  });
};

module.exports = auth;