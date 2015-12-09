$(document).ready(function(){
	$("#select").click(function(){
		getMedcine();
	});
});

var SERVER_URL = "http://192.168.19.111:8080";

var presList;

function getMedcine(){
	var selects = $("[name='bingzheng']");
	var syms = new Array();
	$.each(selects,function(index,select){
		var id = $(select).val();
		if(id>0){
			syms.push(id);
		}
	});
	$.post(SERVER_URL+"/core_medcine",
		{
			syms:syms,
			desc:"病人描述"
		},function(result){
			if(result.r==0){
				presList = result.m;
				console.log(presList);
			}
			selectPres("柴胡桂枝汤");
		}
	);
}


function selectPres(selectedName){
	$.each(presList, function(index, pres){
		if(pres.name == selectedName){
			var meds = pres.meds;
			$.each(meds,function(medIndex, med){
				console.log(med.name+":"+med.unit);
			})
		}
	});
}