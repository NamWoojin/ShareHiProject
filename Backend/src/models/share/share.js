'use strict';

const insertSharInfo = `select * from device where mem_id=? and status='ON'`;
// const getOfflineDevice = `select * from device where mem_id=? and status='OFF'`;
// const insertDevice = 'insert into device set ?';
// const deletetDevice = 'delete from device where dev_id=?';
// const checkDevice = 'select * from device where mem_id=? and dev_name=? and dev_type=?';
// const updateStatus = 'update device set dev_status=?';

module.exports = {
    insertShareInfo,
    // getOfflineDevice,
    // insertDevice,
    // deletetDevice,
    // checkDevice,
    // updateStatus
};
