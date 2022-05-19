const mysql = require('mysql');
const connection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'db_server'
});


connection.connect(function (err) {
    if (err) throw err;
    console.log('DB Connected!');
});


module.exports = connection