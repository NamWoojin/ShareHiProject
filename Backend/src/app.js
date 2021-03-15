'use strict';



const express = require('express');
const app = express();
const PORT = 8080;

// ----------------------- 추가한 부분 2 ---------------------------
const options = {
  definition: {
    openapi: "3.0.0",
    info: {
      title: "Meditator's Node Express API with Swagger",
      version: "0.1.0",
      description:
        "This is a simple CRUD API application made with Express and documented with Swagger",
      license: {
        name: "MIT",
        url: "https://spdx.org/licenses/MIT.html",
      },
      contact: {
        name: "Meditator",
        url: "https://velog.io/@yongh8445",
        email: "yonghoon95@gmail.com",
      },
    },
    servers: [
      {
        url: "http://localhost:3000/books",
      },
    ],
  },
  apis: ["./routes/books.js"],
};

const specs = swaggerJsdoc(options);
// --------------------------------------------------------------

const home = require('./controller/home');
app.use('/api', home);

// ----------------------- 추가한 부분 3 ---------------------------
app.use("/api-docs",
  swaggerUi.serve,
  swaggerUi.setup(specs)
);
// --------------------------------------------------------------
app.listen(PORT, function () {
  console.log('서버가동');
});
