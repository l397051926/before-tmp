/**
 * Created by chen-song on 16/4/12.
 */
var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var url = require("url");
var conf = require('../conf/db');
var sql = require('../sql/sql');
var result = require('../entity/Result');
var pool = mysql.createPool(conf.mysql);

router.get('/UserFinishedSampleList', function(req, res) {
    console.log(JSON.stringify(req.query));
    var p = req.query.param;
    var pp = JSON.parse(p);
    console.log("pp="+JSON.stringify(pp));
    userFinishedSampleList(pp,res);
});

function userFinishedSampleList(paramJson,res) {
    var limit = paramJson.limit;
    var uid = paramJson.uid;
    var lim = limit.split(",");
    var re = new result();
    if(lim.length != 2){
        errorFunc('参数解析出错',res);
    }else{
        var pageNo = parseInt(lim[0]);
        var pageCounter = parseInt(lim[1]);
        var args = [];
        args.push(uid);
        args.push(pageNo);
        args.push(pageCounter);
        pool.getConnection(function(err, connection) {
            if (err) {
                re.code = 0;
                re.info = "无法获取连接";
                res.send(JSON.stringify(re));
            }
            getFinishedSampleList(args,connection,res,re);
        });
    }
}

function getFinishedSampleList(args,connection,res,re) {
    console.log("uid="+args[0]+",pageNo="+args[1]+",pageCounter="+args[2]);
    console.log("sql="+sql.project.getFinishedProjects);
    connection.query(sql.project.getFinishedProjects,args,function(err, rows) {
        connection.query(sql.project.getFinishedProjectCounter,args,function (err,rows) {
            if (err) {
                re.code = 0;
                re.info = "无法获取连接";
                res.send(JSON.stringify(re));
                return;
            }else {
                var obj = {};
                obj.counter = rows;
                re.info = obj;
                return;
            }
        });


        for(var j in rows){
            var project = {};
            project.projectID = rows[j].projectID;
            project.projectName = rows[j].projectName;
            project.creater = rows[j].creater;
            project.uname = rows[j].uname;
            resultValue.push(person)
        }
        res.send(JSON.stringify(rows));
    });
    //回收pool
    connection.release();
}

function errorFunc(err,res) {
    var re = new result();
    re.code = 0;
    re.info = err;
    res.send(JSON.stringify(re));
}

module.exports = router;