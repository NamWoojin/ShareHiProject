'use strict';
// import {swaggerUi, specs} from './swagger.js';

const express = require('express');
const app = express();
// swagger
// const { swaggerUi, specs } = require('./src/modules/swagger');
// 라우팅
const home = require('./src/routes')
app.use(express.json());
app.use(express.urlencoded( {extended : false } ));
//app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(specs));
app.use("/api", home);


module.exports = app;