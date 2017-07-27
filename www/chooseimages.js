var exec = require('cordova/exec');

exports.setlocation = function(success, error,info) {
    exec(success, error, "chooseimages", "setlocation", info);
};
exports.getCamera = function(success, error,info) {
  exec(success, error, "chooseimages", "getCamera", info);
};


