/**
 * Created by chen-song on 16/4/9.
 */
var mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/mytest');
var db = mongoose.connection;
var kittySchema = mongoose.Schema({
    name: String
});
kittySchema.methods.speak = function () {
    var greeting = this.name
        ? "Meow name is " + this.name
        : "I don't have a name"
    console.log(greeting);
}
var Kitten = mongoose.model('Kitten', kittySchema);
var silence = new Kitten({ name: 'Silence' });
console.log(silence.name);
var fluffy = new Kitten({ name: 'fluffy' });
fluffy.speak();
fluffy.save(function (err, fluffy) {
    if (err) return console.error(err);
    fluffy.speak();
});