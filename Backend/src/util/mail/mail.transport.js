'use strict';

const nodemailer = require('nodemailer');

const transport = nodemailer.createTransport({
  service: 'Gmail',
  auth: {
    user: 'filedsolar@gmail.com',
    pass: 'solar123@',
  },
  tls: {
    rejectUnauthorized: false,
  },
});
module.exports = transport;
