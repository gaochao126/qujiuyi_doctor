function onSubmitMedcine(){
	var medcines = $("[name='resultDose']");
	var numOfAll = medcines.length;
	var num = numOfAll/3;
	var meds = "";
	for(var index=0;index<num;index++){
		var med = {};
		meds += $(medcines[index]).html();
		meds += $(medcines[index+num]).html();
		meds += $(medcines[index+2*num]).html();
	}
}