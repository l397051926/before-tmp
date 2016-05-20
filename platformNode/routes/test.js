/**
 * Created by chen-song on 16/4/11.
 */
var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var conf = require('../conf/db');
var orgBean = require('../entity/orgBean')
var sql = require('../sql/sql')
var pool = mysql.createPool(conf.mysql);

router.post('/', function(req, res) {
    var values = ['chensong','ls123456']
    pool.getConnection(function(err, connection) {
        if (err) throw err;
        connection.query(sql.user.testOrg,function(err, rows) {
            if (err) throw  err;
            for(var i= 0; i < rows.size;i++){
                var entity = rows[i];
                console.log(entity);
            }
            res.send(JSON.stringify(rows[0]))
        });
        //回收pool
        connection.release();
    });
});

module.exports = router;