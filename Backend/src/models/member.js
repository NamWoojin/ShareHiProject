'use strict';


conn.connect();

conn.query('select * from member', function(err, results, fields) {
    if(err) {
        console.log(err);
    }
    console.log(results);
});