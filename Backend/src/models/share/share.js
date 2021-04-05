'use strict';

const insertSharInfo = `select * from device where mem_id=? and status='ON'`;

module.exports = {
    insertShareInfo,
};
