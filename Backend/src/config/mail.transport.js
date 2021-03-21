'use strict';

const nodemailer = require('nodemailer');

// const transport = nodemailer.createTransport({
//   service: 'gmail',
//   host: 'smtp.gmail.com',
//   port: 465,
//   secure: true,
//   auth: {
//     user: 'fieldSolar@gmail.com',
//     pass: 'solar123@',
//   },
// });
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
