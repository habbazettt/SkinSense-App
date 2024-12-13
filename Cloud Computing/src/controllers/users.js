const bcrypt = require('bcryptjs')
const response = require('../response')

const {
    findUsers,
    findUserByEmail,
    findUserById,
    addUser
} = require('../models/Users')

const generateTokenAndCookie = require('../utils/generateTokenAndCookie')

const getAllUsers = async (req, res) => {
    try {
        const users = await findUsers()

        response(200, users, 'Success to get all users', res)
    } catch (error) {
        response(500, 'invalid', error.message, res)
        console.log(error.message);
    }
}

const signupUser = async (req, res) => {
    try {
        // get data from request
        const {
            name,
            username,
            email,
            phone,
            password,
            repeatPassword
        } = req.body

        // check if user already exists
        const user = await findUserByEmail(email)

        if (user) {
            return response(400, null, 'User already exists', res)
        }

        // check if password and repeat password match
        if (password !== repeatPassword) {
            return response(400, null, 'Password and repeat password do not match', res)
        }

        // encrypt password
        const salt = await bcrypt.genSalt(10)
        const hashedPassword = await bcrypt.hash(password, salt)

        // send user data to database
        const newUser = {
            name,
            username,
            email,
            phone,
            password: hashedPassword
        }

        await addUser(newUser)

        response(201, newUser, 'User created successfully', res)
    } catch (error) {
        response(500, 'invalid', 'error when signup', res);
        console.log(error.message);
    }
}

const loginUser = async (req, res) => {
    try {
        const { email, password } = req.body

        // check if email and password are valid
        if (!email || !password) {
            return response(400, 'error', 'Email and password are required', res)
        }

        // find user by email
        const user = await findUserByEmail(email)

        // check if user exists
        if (!user) {
            return response(404, 'user not found', 'User does not exist, please sign up', res)
        }

        // Create token for user
        let token = generateTokenAndCookie(user.id, res);

        const userData = {
            token,
            user_id: user.id,
            username: user.username,
            email: user.email,
        };

        return response(200, userData, 'User successfully logged in', res);
    } catch (error) {
        console.log(error.message);
        return response(500, 'invalid', 'Error when logging in user', res);
    }
}

const logoutUser = (req, res) => {
    try {
        res.clearCookie('token');
        response(200, 'success', 'Logout successful', res);
    } catch (error) {
        response(500, 'error', 'Internal Server Error when logout', res);
        console.log(error.message);
    }
};

const getDetailUser = async (req, res) => {
    try {
        const userId = parseInt(req.params.userId);

        if (!userId) {
            return response(400, 'error', 'User ID is required', res);
        }

        const user = await findUserById(userId);

        if (!user) {
            return response(404, 'error', 'User not found', res);
        }

        return response(200, user, 'Success get user detail', res);
    } catch (error) {
        response(500, 'invalid', 'Error getting user detail', res);
        console.log(error.message);
    }
};

module.exports = {
    getAllUsers,
    signupUser,
    loginUser,
    logoutUser,
    getDetailUser,
};