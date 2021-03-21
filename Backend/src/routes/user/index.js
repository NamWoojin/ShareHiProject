'use strict';

const express = require('express');
const router = express.Router();
const UserService = require('../../services/user/user.srv');

/**
 * @swagger
 *  /signup:
 *    get:
 *      tags:
 *      - product
 *      description: 모든 제품 조회
 *      produces:
 *      - application/json
 *      parameters:
 *        - in: query
 *          name: category
 *          required: false
 *          schema:
 *            type: integer
 *            description: 카테고리
 *      responses:
 *       200:
 *        description: 제품 조회 성공
 */
router.post('/signup', UserService.signup);
router.delete('/signout/:memId', UserService.signout);
router.get('/getUser/:memId', UserService.getUser);
router.get('/checkEmail/:memID', UserService.checkEmail);
router.put('/update', UserService.update);
router.put('/updatePassword', UserService.updatePassword);
router.get('/checkEmail/:memID', UserService.checkEmail);
router.post('/requireEmailAuth', UserService.requireEmailAuth);
router.post('/checkEmailAuth', UserService.checkEmailAuth);

module.exports = router;
