'use strict';
// import {swaggerUi, specs} from './swagger.js';

 const express = require('express');
//import express from 'express'
const app = express();
const PORT = 8080;

const { swaggerUi, specs } = require('./modules/swagger');

app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(specs));

app.listen(PORT, function () {
  console.log('서버가동');
});
