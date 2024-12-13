const jwt = require('jsonwebtoken');

const generateTokenAndCookie = (userId, res) => {
    const token = jwt.sign({ userId }, process.env.JWT_SECRET, {
        expiresIn: '15d',
    });

    res.cookie('token', token, {
        httpOnly: true,
        maxAge: 15 * 24 * 60 * 60 * 1000, // 15 days
    });

    return token;
};

module.exports = generateTokenAndCookie;