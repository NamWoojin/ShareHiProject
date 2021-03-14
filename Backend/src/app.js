'use strict';

const express = require('express');
const app = express();
const PORT = 8080;

const home = require('./controller/home');
app.use('/api', home);

app.listen(PORT, function () {
  console.log('서버가동');
});
