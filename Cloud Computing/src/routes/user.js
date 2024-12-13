const express = require('express');
const router = express.Router();

const {
    getAllUsers,
    signupUser,
    loginUser,
    logoutUser,
    getDetailUser,
} = require('../controllers/users');

router.get('/', getAllUsers);
router.get('/:userId', getDetailUser);
router.post('/signup', signupUser);
router.post('/login', loginUser);
router.post('/logout', logoutUser);

module.exports = router;