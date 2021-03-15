'use strict';
// import {swaggerUi, specs} from './swagger.js';

 const express = require('express');
//import express from 'express'
const app = express();
const PORT = 8080;

const { swaggerUi, specs } = require('./modules/swagger');
// app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(specs));
// swagger();

//const swaggerUi = require("swagger-ui-express");
//const swaggerJsdoc = require("swagger-jsdoc");
//import {swaggerUi, specs} from './modules/swagger';
// import swaggerUi from 'swagger-ui-express'
// import swaggerJsdoc from 'swagger-jsdoc'

// const options = {
// //swagger문서 설정
//     swaggerDefinition: {
//         info: {
//             title: 'Test API',
//             version: '1.0.0',
//             description: 'Test API with express',
//         },
//         host: 'localhost:8080',
//         basePath: '/'
//     },
// //swagger api가 존재하는 곳 입니다.
//     apis: ['./controllers/*.js']
// };

// const specs = swaggerJsdoc(options);
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(specs));
//const home = require('./controllers/home');
//app.use('/api', home);

app.listen(PORT, function () {
  console.log('서버가동');
});
