var exec = require('cordova/exec');

exports.setlocation = function(success, error,info) {
    exec(success, error, "chooseimages", "setlocation", info);
};
exports.getPhotos = function(success, error,info) {
  exec(success, error, "chooseimages", "getPhotos", info);
};


