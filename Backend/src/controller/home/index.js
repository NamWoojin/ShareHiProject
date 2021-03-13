'use strict';

const express = require('express');
const router = express.Router();

router.get('/', (req, res) => {
  res.send('홈화면입니다.');
});

module.exports = router;
