var SERVER_URL = "http://192.168.19.105:8080";

var UID=opener.document.getElementById("User_ResultId").innerHTML;
alert(UID);
function readPatient(){
    if(UID!=null){
        $.post(SERVER_URL+"/user_info_uid",{ uid:UID},
            function(result){


            }
            )
    }
}