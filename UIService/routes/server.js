/**
 * Created by chen-song on 16/3/30.
 */
var express = require("express");

var app = express();
var fs = require("fs");

app.get('/listUsers',function (req,res) {
    fs.readFile(__dirname+'/../conf/users.json','utf8',function (err,data) {
        console.log(data);
        res.end(data);
    })
})
app.get('/:id', function (req, res) {
    // 首先我们读取已存在的用户
    fs.readFile( __dirname + "/../conf/users.json", 'utf8', function (err, data) {
        data = JSON.parse( data );
        var user = data["user" + req.params.id]
        console.log( user );
        res.end( JSON.stringify(user));
    });
})
app.get('/deleteUser/:id', function (req, res) {
    // First read existing users.
    fs.readFile( __dirname + "/../conf/users.json", 'utf8', function (err, data) {
        data = JSON.parse( data );
        delete data["user" + req.params.id];
        console.log( data );
        res.end( JSON.stringify(data));
    });
})

var server = app.listen(8081,function () {
    var host = server.address().address;
    var port = server.address().port;
    console.log("应用实例,访问地址为 http://%s:%s",host,port);
})