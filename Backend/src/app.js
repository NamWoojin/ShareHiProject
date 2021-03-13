'use strict';

const express = require('express');
const app = express();

const home = require('./controller/home');
app.use('/', home);

module.exports = app;
