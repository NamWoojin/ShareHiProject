'use strict';

var redis = require('redis');
const config = require('./redis.config')
const client = redis.createClient({
  host: config.host,
  port: config.emailAuthPort,
  //no_ready_check: true,
  auth_pass: config.emailAuthPass,
 
});

module.exports = //client;
client.on('error', function (err) {
    console.log('Error ' + err);
});
