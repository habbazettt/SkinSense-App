const prisma = require("../db/index");
const response = require('../response')

// Get all skins
const getSkins = async (req, res) => {
    try {
        const skins = await prisma.Skin.findMany();

        return response(200, skins, 'Success to get all skins', res);
    } catch (error) {
        console.log(error.message);
        throw error;
    }
}

// Get skin by id
const getSkinById = async (req, res) => {
    try {
        const skinId = parseInt(req.params.skinId);
        const skin = await prisma.Skin.findUnique({
            where: {
                id: skinId
            }
        });

        return response(200, skin, 'Success to get skin by id', res);
    } catch (error) {
        console.log(error.message);
        throw error;
    }
}

// Get skin by user id
const getSkinByUserId = async (req, res) => {
    try {
        const userId = parseInt(req.params.userId);
        const skin = await prisma.Skin.findMany({
            where: {
                user_id: userId
            }
        });

        return response(200, skin, 'Success to get skin by user id', res);
    } catch (error) {
        console.log(error.message);
        throw error;
    }
}

// Create new skin
const addSkin = async (skinData) => {
    // Ensure skinData contains userId and other necessary fields
    if (!skinData.user_id) {
        throw new Error("userId is required to create a Skin entry.");
    }

    const skin = await prisma.Skin.create({
        data: {
            result: skinData.result,
            suggestion: skinData.suggestion,
            imageUrl: skinData.imageUrl,
            user: {
                connect: { user_id: skinData.user_id } // Connect to an existing user by user_id
            }
        }
    });

    return skin;
};

// Delete skin by id
const deleteSkin = async (req, res) => {
    try {
        const skinId = parseInt(req.params.skinId);
        const skin = await prisma.Skin.delete({
            where: {
                id: skinId
            }
        });

        return response(200, skin, 'Success to delete skin by id', res);
    } catch (error) {
        console.log(error.message);
        throw error;
    }
}

module.exports = {
    addSkin,
    getSkins,
    getSkinByUserId,
    getSkinById,
    deleteSkin
}