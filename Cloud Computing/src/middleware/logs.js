const logRequest = (req, res, next) => {
    console.log(`Terjadi request ${req.method} ke ${req.url} : ${res.statusCode}`);
    next();
}

module.exports = logRequest