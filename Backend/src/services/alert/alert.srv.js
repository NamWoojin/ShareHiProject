'use strict'

const RabbitmqWrapper = require('../../modules/rabbitmq')
const amqp = require('amqplib/callback_api')
// const async = require('async');

const sendMSG = async(req, res) => {
    amqp.connect('amqp://j4f001.p.ssafy.io:5672', (err, connetion) => {
        
    })
    const mem_id = req.query.mem_id;
    try {
        const url = 'amqp://j4f001.p.ssafy.io:5672';
        const queueName = 'MQ_test' + mem_id;
        const rq = new RabbitmqWrapper(url, queueName);

        await rq.send_helloWorld();

        res.sendStatus(204);
    } catch (e) {
        console.error(e);
        next(e);
    }
}

module.exports = {
    sendMSG
}