'use strict';
// import {swaggerUi, specs} from './swagger.js';

const express = require('express');
const app = express();
const PORT = 8080;

// const swagger = require('./modules');
// app.use('/api-docs', swagger.waggerUi.serve, swagger.swaggerUi.setup(specs));

const home = require('./controllers/home');
app.use('/api', home);

app.listen(PORT, function () {
  console.log('서버가동');
});
