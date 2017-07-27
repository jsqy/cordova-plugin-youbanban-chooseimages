# cordova-plugin-youbanban-chooseimages
# html
#   <button ng-click="getImages()" class="button button-positive" id="chooseimages">选择图片</button>
# controller
# $scope.getImages = function(){
       $("#chooseimages").attr("disabled", "disabled"); //使按钮不能被点击
       var ChooseImages = cordova.require('cordova-plugin-youbanban-chooseimages.chooseimages');
       ChooseImages.getCamera(function(message) {
         var imgPaths = message.split(",");
         if(message == ""){
           alert("并没有选择图片");
         }else{
           for(var i =0;i<imgPaths.length;i++){
             alert(imgPaths[i]);
           }
         }
         setTimeout(function() {
           $("#chooseimages").removeAttr("disabled"); //使按钮能够被点击
         },500);
         //isClick = "1";
       }, function(message) {
         setTimeout(function() {
           $("#chooseimages").removeAttr("disabled"); //使按钮能够被点击
         },500);
       }, [9]);
     }
