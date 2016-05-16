/**
 * Created by chen-song on 16/3/30.
 */
var mongoose = require('mongoose');
var db = mongoose.createConnection('mongodb://192.168.1.111:27017/CRF_Model');

var Schema = mongoose.Schema;
db.on('error', function(error) {
    console.log(error);
});
var groupSchema = new Schema({
    id:String,
    name:String,
    isParent:Boolean,
    rankID:Number,
    checked:Boolean,
    children:[]
});
var PersonSchema = new Schema({
    code:Number,
    crf_id:String,
    children:[groupSchema],
    status:String,
    isParent:Boolean,
    checked:Boolean
});


db.model("crf_Model",PersonSchema);
var PersonModel = db.model('crf_Model');
var personEntity = new PersonModel({
    code:1,
    crf_id:"nodejs",
    children:[{
        id:"患者基本信息",
        name:"患者基本信息",
        isParent:true,
        rankID:1,
        checked:true,
        children:[

        ]
    }],
    status:"创建",
    isParent:true,
    checked:true
});
personEntity.save();
PersonModel.findOne({})