// JavaScript Document

function jsAuto(instanceName,objID)
{
	this._msg = [];
	this._x = null;
	this._o = document.getElementById( objID );
	if (!this._o) return;
	this._f = null;
	this._i = instanceName;
	this._r = null;
	this._c = 0;
	this._s = false;
	this._v = null;
	this._o.style.visibility = "hidden";
	this._o.style.position = "absolute";
	this._o.style.zIndex = "9999";
this._o.style.overflow = "auto";
this._o.style.height = "50";
	return this;
};

jsAuto.prototype.directionKey=function() { with (this)
{
	var e = _e.keyCode ? _e.keyCode : _e.which;
	var l = _o.childNodes.length;
	(_c>l-1 || _c<0) ? _s=false : "";

	if( e==40  &&  _s )
	{
		
		(_c >= l-1) ? _c=0 : _c ++;
		_o.childNodes[_c].style.background="#ffffff";
		
	}
	if( e==38  &&  _s )
	{
		
		_c--<=0 ? _c = _o.childNodes.length-1 : "";
		_o.childNodes[_c].style.background="#ffffff";
	}
	if( e==13 )
	{
		if(_o.childNodes[_c]  &&  _o.style.visibility=="visible")
		{
			_r.value = _x[_c];
			_o.style.visibility = "hidden";
		}
	}
	if( !_s )
	{
		_c = 0;
		_o.childNodes[_c].style.background="#ffffff";
		_s = true;
	}
}};

// mouseEvent.
jsAuto.prototype.domouseover=function(obj) { with (this)
{
	_o.childNodes[_c].className = "mouseout";
	_c = 0;
	obj.tagName=="DIV" ? obj.style.background="#ffffff" : obj.parentElement.style.background="#ffffff";
}};
jsAuto.prototype.domouseout=function(obj)
{
	obj.tagName=="DIV" ? obj.style.background="#3F9" : obj.parentElement.style.background="#3F9";
};
jsAuto.prototype.doclick=function(msg) { with (this)
{
	if(_r)
	{
		_r.value = msg;
		_o.style.visibility = "hidden";
	}
	else
	{
		alert("javascript autocomplete ERROR :\n\n can not get return object.");
		return;
	}
}};

// object method;
jsAuto.prototype.item=function(msg)
{
	if( msg.indexOf(",")>0 )
	{
		var arrMsg=msg.split(",");
		for(var i=0; i<arrMsg.length; i++)
		{
			arrMsg[i] ? this._msg.push(arrMsg[i]) : "";
		}
	}
	else
	{
		this._msg.push(msg);
	}
	this._msg.sort();
};
jsAuto.prototype.append=function(msg) { with (this)
{
	_i ? "" : _i = eval(_i);
	_x.push(msg);
	var div = document.createElement("DIV");
	//bind event to object.
	div.onmouseover = function(){_i.domouseover(this)};
	div.onmouseout = function(){_i.domouseout(this)};
	div.onclick = function(){_i.doclick(msg)};
	var re  = new RegExp("(" + _v + ")","i");
	div.style.lineHeight="140%";
	div.className = "mouseout";
	if (_v) div.innerHTML = msg.replace(re , "<strong>$1</strong>");
	div.style.fontFamily = "verdana";

	_o.appendChild(div);
}};
jsAuto.prototype.display=function() { with(this)
{
	if(_f && _v!="")
	{
		_o.style.left = _r.offsetLeft;
		_o.style.width = _r.offsetWidth;
		_o.style.top = _r.offsetTop + _r.offsetHeight;
		_o.style.visibility = "visible";
	}
	else
	{
		_o.style.visibility="hidden";
	}
}};
jsAuto.prototype.handleEvent=function(fValue,fID,event) { with (this)
{
	var re;
	_e = event;
	var e = _e.keyCode ? _e.keyCode : _e.which;
	_x = [];
	_f = false;
	_r = document.getElementById( fID );
	_v = fValue;
	_i = eval(_i);
	re = new RegExp("^" + fValue + "", "i");
	_o.innerHTML="";

	for(var i=0; i<_msg.length; i++)
	{
		if(re.test(_msg[i]))
		{
			_i.append(_msg[i]);
			_f = true;
		}
	}

	_i ? _i.display() : alert("can not get instance");

	if(_f)
	{
		if((e==38 || e==40 || e==13))
		{
			_i.directionKey();
		}
		else
		{
			_c=0;
			_o.childNodes[_c].style.background="#ffffff";
			_s=true;
		}
	}
}};
window.onerror=new Function("return true;");
//-->
function test(){
	var a=document.getElementById("sel").value;
	var b=document.getElementById("auto");
	b.value=a;
	
	}