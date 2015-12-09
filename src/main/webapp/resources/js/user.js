var SERVER_URL = "http://192.168.19.105:8080";
function addPatient() {
	var key = $("#key").val();
	var uid = $("#uid").val();
	var address = $("#address").val();
	var username = $("#username").val();
//	var sex = $("#sex").val();
	var age = $("#age").val();
    var sex = "MALE";
    var yibaoId=$("#yibaoId").val();
	if(!key){
		return ;
	}else if(!uid){
		return;
	}else if(!username){
		return;
	}else if(!sex){
		return;
	}else if(!age){
		return;
	}
	else if(!address){
        return;
    }
	$.post(SERVER_URL+"/user_register", {
		key : key,
		ut : "patient",
		uid : uid,
		address : address,
		username : username,
		sex : sex,
        yibaoId:yibaoId
	}, function(result) {
        if(result.r==0){
            $("#tinybox_1").show();
        }
        if(result.r==3){
            alert("用户名已存在");
        }


	});

}



function searchInfo(){

    var uid=$("#UserId").val();
    if(!uid){
        return false;
    }
    $.post(SERVER_URL+"/user_info_uid",{
            uid:uid
        },function(result){
            alert(result.r);
           if(result.r==0){
               alert(result.name);
               $("#show_info").show();
               $("#show_result").show();
               $("#User_ResultId").html(result.id);
               $("#p_name").html(result.name);
               $("#p_sex").html(result.sex);
               $("#p_name").html(result.name);
               $("#p_age").html(result.age);
               $("#p_number").html(result.yb);
               $("#p_phone").html(result.phone);
               $("#p_ID").html(result.uid);
               $("#p_address").html(result.addr);
           }

            else if(result.r==1){
               alert(result.r+"不存在该病患");
               $("#NoPatient").show();

           }
        }
    )
}

function handleLogin(){
	var key = $("#rkey").val();
	var password = $("#rpassword").val();
	if(!key){
		return false;
	}else if(!password){
		return false;
	}
	$.post(SERVER_URL+"/html/salt", function(result) {
		var salt = result.salt;
		if(!salt){
			return false;
		}
		var md5 = MD5(password);
		var passwd = MD5(md5+salt);
		password=null;
		$.post("/user_login", {
			key : key,
			ut : "doctor",
			password : passwd
		}, function(result) {
			if(result.r == 0){
				window.location.href='patient_library';
			}
			if(result.r==4){
				var hint=document.getElementById("Login_hint");
				hint.innerHTML="用户名或密码错误";
			}
			else{
				alert(result.r);
			}
			
		});
	});

}


