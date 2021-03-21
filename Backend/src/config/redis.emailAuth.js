var redis = require('redis');

const client = redis.createClient({
  host: 'j4f001.p.ssafy.io',
  port: 6379,
  //no_ready_check: true,
  auth_pass: 'solar123@',
});
module.exports = client;
// client.on('error', function (err) {
//     console.log('Error ' + err);
// });

// client.set("hello", "Node.js");

// client.get("hello", function(err, val) {
//         console.log(typeof val);
//         client.quit();
// });
