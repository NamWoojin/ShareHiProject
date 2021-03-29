// with { "type": "module" } in your package.json

// import { createServer } from "http";
// import { io as Client } from "socket.io-client";
// // import { Server } from "socket.io";
// import { assert } from "chai";

// with { "type": "commonjs" } in your package.json
const { createServer } = require("http");
const { Server } = require("socket.io");
const {io} = require("socket.io-client");
const assert = require("chai").assert;

describe("my awesome project", () => {
  //let io, serverSocket, clientSocket;

  // before((done) => {
  //   const httpServer = createServer();
  //   io = new Server(httpServer);
  //   httpServer.listen(() => {
  //     // const port = httpServer.address().port;
  //     clientSocket = new Client(`https://j4f001.p.ssafy.io/file/web/`);
  //     io.on("connection", (socket) => {
  //       serverSocket = socket;
  //     });
  //     clientSocket.on("connection", done);
  //   });
  // });

  // after(() => {
  //   io.close();
  //   clientSocket.close();
  // });

  it("socket connetion test", () => {
    const client = io.connect(`http://j4f001.p.ssafy.io:9002`);
    // const client = io(`http://localhost:9000`);
    client.on('connection', () => {
      console.log("Client Connection");
    })
  })

  // it("should work", (done) => {
  //   clientSocket.on("hello", (arg) => {
  //     assert.equal(arg, "world");
  //     done();
  //   });
  //   serverSocket.emit("hello", "world");
  // });

  // it("should work (with ack)", (done) => {
  //   serverSocket.on("hi", (cb) => {
  //     cb("hola");
  //   });
  //   clientSocket.emit("hi", (arg) => {
  //     assert.equal(arg, "hola");
  //     done();
  //   });
  // });
});