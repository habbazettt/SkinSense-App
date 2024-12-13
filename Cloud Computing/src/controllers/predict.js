const response = require('../response');
const predictedClassification = require('../services/inferenceService');
const loadModel = require('../services/loadModel');
const { addSkin } = require('../models/Skin');
const { findUserById } = require('../models/Users');
const { uploadFile } = require('../utils/configFile');

let model;

(async () => {
    try {
        model = await loadModel();
    } catch (error) {
        console.error('Error loading model:', error);
        process.exit(1)
    }
})();

const postPredictData = async (req, res) => {
    try {
        const { user_id } = req.body;
        const image = req.file.buffer;

        const file = req.file;

        const userExists = await findUserById(user_id);

        if (!userExists) {
            return response(400, 'error', 'User not found', res);
        }

        if (!image) {
            return response(400, 'error', 'Image is required', res);
        }

        if (req.file.mimetype !== 'image/jpeg' && req.file.mimetype !== 'image/png') {
            return response(400, 'error', 'Invalid image format', res);
        }

        if (!model) {
            return response(500, 'error', 'Model not loaded', res);
        }

        const { confidenceScore, label, result, suggestion } = await predictedClassification(image, model);

        // Upload image to storage
        if (!file) {
            return response(400, 'error', 'File is required', res);
        }

        const imageData = await uploadFile(file);

        // Upload to database
        const skinData = {
            user_id: parseInt(user_id, 10),
            result: result === 'Melanoma' ? 'Melanoma' : 'Normal',
            suggestion: suggestion,
            imageUrl: imageData
        };

        let skin;
        try {
            skin = await addSkin(skinData);
        } catch (error) {
            console.error("Error adding skin data:", error.message);
            return response(400, 'error', 'Failed to create skin entry: Skin data may already exist or user not found.', res);
        }

        return response(200, { confidenceScore, label, result, suggestion }, 'Success', res);

    } catch (error) {
        console.error("Internal Server Error:", error.message);
        return response(500, 'error', 'Internal Server Error', res);
    }
};


module.exports = {
    postPredictData,
}