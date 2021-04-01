'use strict'

const RabbitmqWrapper = require('../../modules/rabbitmq')
const amqp = require('amqplib/callback_api')
const { connection_id } = require('../../config/redis/redis.emailAuth')
// const async = require('async');

const sendMSG = async(req, res) => {
    amqp.connect('amqp://j4f001.p.ssafy.io:5672', (err1, connetion) => {
        if(err1) {
            console.log(err);
            throw err;
        }
        connetion.createChannel((err2, channel) => {
            if(err2) {
                console.log(err2);
                throw err;
            }

            let queue = 'hello';
            let msg = 'hello world';
            channel.assertQueue(queue, {
                durable: false;
            });
            channel.sendToQueue(queue, Buffer.from(msg));
            console.log('send %s', msg);
        })
    })
    // serTimeout
    // const mem_id = req.query.mem_id;
    // try {
    //     const url = 'amqp://j4f001.p.ssafy.io:5672';
    //     const queueName = 'MQ_test' + mem_id;
    //     const rq = new RabbitmqWrapper(url, queueName);

    //     await rq.send_helloWorld();

    //     res.sendStatus(204);
    // } catch (e) {
    //     console.error(e);
    //     next(e);
    // }
}

module.exports = {
    sendMSG
}