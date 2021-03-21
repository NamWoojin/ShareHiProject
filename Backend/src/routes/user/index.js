'use strict';

const express = require('express');
const router = express.Router();
const UserService = require('../../services/user/user.srv');

const transport = require('../../config/mail.transport');

router.post('/signup', UserService.signup);
router.delete('/signout/:memId', UserService.signout);
router.get('/getUser/:memId', UserService.getUser);
router.get('/checkEmail/:memID', UserService.checkEmail);
router.put('/update', UserService.update);
router.put('/updatePassword', UserService.updatePassword);
router.get('/checkEmail/:memID', UserService.checkEmail);

router.post('/test/:memEmail', async (req, res, next) => {
  const member = req.params;
  console.log('>>>check email');
  console.log(member['memEmail']);

  const mailOptions = {
    from: 'filedsolar@gmail.com',
    to: member['memEmail'],
    subject: '[ABC] 인증번호가 도착했습니다.',
    text: '123456',
    html: `
      <div style="text-align: center;">
        <h3 style="color: #FA5882">ABC</h3>
        <br />
        <p>123456</p>
      </div>
      `,
  };
  await transport.sendMail(mailOptions, (error, responses) => {
    if (error) {
      console.log(error);
      res.json({ msg: 'err' });
    } else {
      res.json({ msg: 'sucess' });
    }
    transport.close();
  });
});
module.exports = router;
