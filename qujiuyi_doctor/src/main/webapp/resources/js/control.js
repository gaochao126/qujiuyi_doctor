function judge(){
	var sex=document.getElementById("sex").innerHTML;
 	var age=document.getElementById("age").innerHTML;
	var element=document.getElementById("wenzhen");
	
	for(var keshi in data){
		if(age>12){
			if(sex=="女")
			{
				
				if(keshi=="妇科"||keshi=="公共部分"){
				addSelect();}
			}
			if(sex=="男")
			{
				if(keshi=="男科"||keshi=="公共部分"){
				addSelect();}
			}
		}
		if(age>0&&age<13){
			if(keshi=="儿科"||keshi=="公共部分"){
			addSelect();
			}
		}
	}/*for*/
function addSelect(){
	
	var ctg=data[keshi];
				for(var i in ctg)
				{	var s_div=document.createElement("div");
					s_div.className="col-lg-4";
					var m_select=document.createElement("select");
					m_select.className="col-lg-7";
					var ctg1=ctg[i];
					for(var j in ctg1){
						var name=ctg1[j];
						if(j=="catagory"){
						for(var m in name){
							if(m=="name"){
								var name1=name[m];
								var spannode=document.createTextNode(name1);
								var m_span=document.createElement("span");
								m_span.className="col-lg-5";
								m_span.appendChild(spannode);
								}
							}
						}
						if(j=="symsList"){
							for(var n in name)
							{
								
								var list1=name[n];
                                var name1=list1.name;
                                var node=document.createTextNode(name1);
								var m_option=document.createElement("option");
                                m_option.setAttribute("value",list1.id);
                                m_select.setAttribute("name","bingzheng");
							    m_option.appendChild(node);
								m_select.appendChild(m_option);
							}
						}
						
					}
					s_div.appendChild(m_span);
					s_div.appendChild(m_select);
					element.appendChild(s_div);
				}
	
	}
		
	}
function add_basic(){
	var element=document.getElementById("drug_info");
	var div = document.createElement("div");
	div.innerHTML='<div class="row drug_tr"><span class="col-lg-4"><input type="text"></span><span class="col-lg-4"><input type="number"></span><span class="col-lg-4"><a href="#" onClick="deletedrug(this)">删除</a></span></div>';
	element.appendChild(div);
	}
function add_self(){
	var element=document.getElementById("self_drug");
	var div = document.createElement("div");
	div.innerHTML='<div class="row drug_tr"><span class="col-lg-4"><input type="text"></span><span class="col-lg-4"><input type="number"></span><span class="col-lg-4"><a href="#" onClick="add_self()" style="margin-right:20px">add</a><a href="#" onClick="deletedrug(this)">删除</a></span></div>';
	element.appendChild(div);
	}
function deletedrug(obj){
	obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
	}