const multer = require('multer');
const { Storage } = require('@google-cloud/storage');

const storage = new Storage({
    credentials: {
        type: "service_account",
        project_id: "skin-sense-443816",
        private_key_id: "e0a258fb9b9373372595b6b053739c99a156cbb7",
        private_key: "-----BEGIN PRIVATE KEY-----\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCxzcJwtequhPLv\nF8Aj1vdJjKLPcRpuIiuKTyEet1MWG9h+MUPSD17xqmRm4qTHJadyqO+is4v7EUCN\nRPUJSpolvWGiKgOZZ+7yM5ntGwUc397MNnqApCYV0yxwTcwFohEVz83/BgGXMw3Y\niWHq57MKWqkhp+hSq3uVWF1S3nvRBinyItEX6lBsb9DYe61c8ommRrL4aMehYdnZ\n4Yg8ZBzLPA4rPSI+yY9m4C4GNUqC6zrFNi52fT5+rcM6efEMpzdCRU5A+7e7Op16\nEkR9A5CKyqHWuyE770gaNdYh1vV8Q6bdOSctdXq9BsJy8Vgt8chgvDwDiRb88RuF\nedMAyp9LAgMBAAECggEAVzeSxv39afEOR6kTV6FFeR8S6AXXjKmXSQeKvAn6KDCt\nkC0zUvG2RX4rNOq9IHEAg/aJYfqH0jbCvulR92ZQWe3dXBIW5OO+KrGUMmIcLm9E\nV1Dr43lhszf8pvQKfcLjdZsTNaMpGZVyF8NANF3GjfAt4bP++rpiaz5GvPUksEFu\nyn0uUwT5OpDZRorm4hRDunKzPG6CPu32p2Oieyf2oGAqUzI2QS09ABFgBD+rY1e8\n/HthG16KDkGk+SZcgyuEMzVJcS/H97PlIlWGstjN2pHHtUZGTv0nYi5fuaT5J73K\nqm8NhVxMk/vbuYPJIuZnRXohra7m2v7dY0qBX9q7CQKBgQC/426w9wPvdFNuNNqu\nRcdWDDJH1i62q6WIYfeJ7AQUCCRucC9qs3iqS1OP3oopKpA9AaQBFCjQ265T+GrB\n9+3oa83OLG5Ohg6PfAaGmBtFO64tCoZ/lGN3/D+lk6wTscUuj1ywjiP/0S1Xi7ZM\nEpUQwxwKgseRbkbIJxqksWS8OQKBgQDtNaPvAmLc7fL4L80mRyye3aIJR5nAdJnm\ndHGX6mbQDoa34GGYQNnuDUvFos+2aPIp8EYmqDjzlsK2cIjPW6D5IYp1sDqhnKHe\ncRQPJg4aMD6OjO/2m9kubXojYke8Dydqe2fPNvTuZhQRz5hzmKwZFVrKwg3nwu4/\n342fG3r/owKBgHUk2imM00k24cwQ1kO386ZTUcrLNb9s8GRfMmhvvlmgUuVc9YWV\nRR/pVn0pJX1WL7Ies0m5TeWbh0RaDoqV2hae4YhxVXH4oyDzXWSDrXpJod57Y3q1\nUzgntY9WpKFtUGxJNxPN4qpEwAKHF2Y/neMayZ6dEkp1YR7FyJ0scsv5AoGAUmlt\ncA9jK6Cb5nAtFRxrOzKDJjgzCbM4+OEByibM24IAegfBwHTfgh4qchqTkas6Xym0\nJaASUUbSlTHZxjjuXz+w7Bxy8k0Jy897iZvOEHkscrgycnpI5SFx6rMtev/E0PDY\n6xwL0nuyg1GdhKPJPtm1dnYfAKMvxQ81E88nV4MCgYAjTaKrcqmeFLDG2vmj5uEr\naKIdU14ThiM2Mz27JX2oofcJqL1cw0ru2EkcSlm1cNOQQe4X742OiD2TaWfbxDdt\n6p0R7R6q3ebfWpH9IVpquBEL6tvxq6zP5axppBxGx7YB52yvbXfm5eyQmVkqn2q8\nEJIi4Kt/3UmL1iTVKMlE+g==\n-----END PRIVATE KEY-----\n",
        client_email: "63760737120-compute@developer.gserviceaccount.com",
        client_id: "116407507265595604746",
        auth_uri: "https://accounts.google.com/o/oauth2/auth",
        token_uri: "https://oauth2.googleapis.com/token",
        auth_provider_x509_cert_url: "https://www.googleapis.com/oauth2/v1/certs",
        client_x509_cert_url: "https://www.googleapis.com/robot/v1/metadata/x509/63760737120-compute%40developer.gserviceaccount.com",
        universe_domain: "googleapis.com"
    },
});

const bucketName = "skinsense-image";
const bucket = storage.bucket(bucketName);

const multerStorage = multer.memoryStorage();

const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg'];

const fileFilter = (req, file, cb) => {
    if (allowedTypes.includes(file.mimetype)) {
        cb(null, true);
    } else {
        cb(new Error('File hanya mendukung PNG, JPG, dan JPEG'), false);
    }
};

const upload = multer({
    storage: multerStorage,
    fileFilter: fileFilter,
    limits: { fileSize: 1024 * 1024 * 2 }
});

const uploadFile = async (file) => {
    const filename = Date.now() + '-skinsense';
    const filePath = `${filename}`;
    const blob = bucket.file(filePath);

    const blobStream = blob.createWriteStream({
        metadata: {
            contentType: file.mimetype,
        },
    });

    return new Promise((resolve, reject) => {
        blobStream
            .on('finish', () => {
                const publicUrl = `https://storage.googleapis.com/${bucketName}/${blob.name}`;
                resolve(publicUrl);
            })
            .on('error', (err) => {
                reject('Gagal upload gambar, ulangi upload gambar');
            })
            .end(file.buffer);
    });
}



module.exports = { uploadFile, upload };