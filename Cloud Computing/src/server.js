const express = require("express");
const cors = require("cors");
require('dotenv').config()
const cookieParser = require('cookie-parser');
const logRequest = require('./middleware/logs')
const swaggerUi = require('swagger-ui-express');

const app = express();

const PORT = process.env.PORT

const apiDocumentation = require('./apiDocs.json')
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(apiDocumentation))

app.use(cors())
app.use(express.json())
app.use(express.urlencoded({ extended: true }))
app.use(cookieParser())
app.use(logRequest)

// test server
app.get("/", (req, res) => {
    res.send("Server is running");
})

// Handle routes users
const userRoutes = require('./routes/user')
app.use('/users', userRoutes)

const skinRoutes = require('./routes/skin')
app.use('/skin', skinRoutes)

app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`)
});
