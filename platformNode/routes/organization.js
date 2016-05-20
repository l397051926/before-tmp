/**
 * Created by chen-song on 16/4/11.
 */
var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var url = require("url");
var conf = require('../conf/db');
var sql = require('../sql/sql');
var result = require('../entity/Result');
var pool = mysql.createPool(conf.mysql);

router.get('/List', function(req, res) {
    orgList(res);
});
router.post('/List', function(req, res) {
    orgList(res);
});

function orgList(res) {
    var re = new result();
    pool.getConnection(function(err, connection) {
        if (err) {
            re.code = 0;
            re.info = "无法获取连接";
            res.send(JSON.stringify(re));
        }

        connection.query(sql.org.getOrgList,function(err, rows) {
            if (err) throw  err;
            re.code = 1;
            re.date = rows;
            res.send(JSON.stringify(re));
        });
        //回收pool
        connection.release();
    });
}


router.get('/Member', function(req, res) {
    console.log(JSON.stringify(req.query));
    var p = req.query.param;
    var pp = JSON.parse(p);
    memberList(pp.data,res);
});
router.post('/Member', function(req, res) {
    console.log("start="+JSON.stringify(req.body));
    memberList(req.body.data,res);
});


function memberList(values,res) {
    var resultValue = [];
    var re = new result();
    pool.getConnection(function(err, connection) {
        if (err) {
            re.code = 0;
            re.info = "无法获取连接";
            res.send(JSON.stringify(re));
        }
        execFunction(0,values,connection,resultValue,res,re);
    });
}

function execFunction(index,values,connection,resultValue,res,re) {
    if(values.length == index){
        re.code = 1;
        re.data = resultValue;
        res.send(JSON.stringify(re));
        //回收pool
        connection.release();
        return;
    }else{
        connection.query(sql.org.getOneOrgList,values[index] ,function(err, rows) {
            if (err) {
                re.code = 0;
                re.info = "无法获取连接";
                res.send(JSON.stringify(re));
                return;
            }
            for(var j in rows){
                var person = {};
                person.orgName = rows[j].orgName;
                person.uid = rows[j].UID;
                person.orgID = rows[j].ORGID;
                person.uname = rows[j].uname;
                resultValue.push(person)
            }
            index ++;
            execFunction(index,values,connection,resultValue,res,re)
        });
    }
    
}



module.exports = router;