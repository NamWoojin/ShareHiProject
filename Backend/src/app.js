'use strict';

const express = require('express');
const app = express();
const PORT = 3000;

const home = require('./controller/home');
app.use('/', home);

app.listen(PORT, function () {
  console.log('서버가동');
});
