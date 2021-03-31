'use strict';

const express = require('express');
const router = express.Router();
const FollowService = require('../../services/follow/follow.srv')

router.get('/getFollower', FollowService.getFollower);
router.get('/getFollowing', FollowService.getFollowing);
router.post('/insertFollowing', FollowService.insertFollowing);
router.delete('/deleteFollowing', FollowService.deleteFollowing);
router.get('/searchMember', FollowService.searchMember);

module.exports = router;