'use strict';

//import swaggerJsdoc from "swagger-jsdoc"
const swaggerUi = require("swagger-ui-express");
const swaggerJsdoc = require("swagger-jsdoc");

const options = {
//swagger문서 설정
    swaggerDefinition: {
        info: {
            title: 'Test API',
            version: '1.0.0',
            description: 'Test API with express',
        },
        host: 'localhost:8080',
        basePath: '/'
    },
//swagger api가 존재하는 곳 입니다.
    apis: ['../routes/user/*.js']
};

const specs = swaggerJsdoc(options);

//var test = function() {
//    console.log('Hello');
//}

//module.exports = test

module.exports = {
    swaggerUi,
    specs
}