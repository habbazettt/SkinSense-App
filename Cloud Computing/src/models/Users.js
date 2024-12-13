const prisma = require("../db/index");

// Find all users
const findUsers = async () => {
    const users = await prisma.Users.findMany();

    return users;
}

// Find user by email
const findUserByEmail = async (email) => {
    const user = await prisma.Users.findUnique({
        where: {
            email: email
        }
    })

    return user;
}

// Find user by id
const findUserById = async (userId) => {
    const user = await prisma.Users.findUnique({
        where: {
            user_id: parseInt(userId, 10)
        }
    });

    return user;
}

// Create new user
const addUser = async (userData) => {
    const user = await prisma.Users.create({
        data: userData
    });

    return user;
}

module.exports = {
    findUsers,
    findUserByEmail,
    findUserById,
    addUser
}

