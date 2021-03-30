'use strict';

const multer = require('multer');
var upload = function (req, res) {
    var deferred = Q.defer();
    var storage = multer.diskStorage({
      // 서버에 저장할 폴더
      destination: function (req, file, cb) {
        cb(null, './volumese/profile/');
      },
  
      // 서버에 저장할 파일 명
      filename: function (req, file, cb) {
        console.log(file);
        file.uploadedFile = {
          name: req.params.filename,
          ext: file.mimetype.split('/')[1]
        };
        cb(null, file.uploadedFile.name + '.' + file.uploadedFile.ext);
      }
    });
  
    var upload = multer({ storage: storage }).single('file');
    upload(req, res, function (err) {
      if (err) deferred.reject();
      else deferred.resolve(req.file.uploadedFile);
    });
    return deferred.promise;
  };

  module.exports = upload;