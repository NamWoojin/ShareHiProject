'use strict';

//import swaggerJsdoc from "swagger-jsdoc"
const swaggerUi = require('swagger-ui-express');
const swaggerJsdoc = require('swagger-jsdoc');

const options = {
  //swagger문서 설정
  swaggerDefinition: {
    info: {
      title: 'ShareHi',
      version: '1.0.0',
      description: 'Test API with express',
    },
    host: 'localhost:8080',
    basePath: '/',
  },
  //swagger api가 존재하는 곳 입니다.
  apis: ['../routes/user/index.js', './*'],
};

const specs = swaggerJsdoc(options);

module.exports = {
  swaggerUi,
  specs,
};
