const { upload } = require('../utils/configFile');

const express = require('express');
const {
    postPredictData,
} = require('../controllers/predict');

const {
    getSkins,
    getSkinById,
    deleteSkin,
    getSkinByUserId
} = require('../models/Skin');

const router = express.Router();

router.get('/', getSkins);
router.get('/:skinId', getSkinById);
router.get('/user/:userId', getSkinByUserId);
router.delete('/:skinId', deleteSkin);
router.post('/predict', upload.single('image'), postPredictData)

module.exports = router;