
function BtHide(id){var Div = document.getElementById(id);if(Div){Div.style.display="none"}} 
function BtShow(id){var Div = document.getElementById(id);if(Div){Div.style.display="block"}}

function BtPopload(showId){ 
// 高度减去 4px，避免在页面无滚动条时显示遮罩后出现流动条 
var h = (Math.max(document.documentElement.scrollHeight,document.documentElement.clientHeight) - 4) + 'px'; 
var w = document.documentElement.scrollWidth + 'px'; 
var popCss = "background:#FFF;opacity:0.3;filter:alpha(opacity=30);position:absolute;left:0;top:0;overflow:hidden;border:0"//遮罩背景 
var rePosition_mask = function() { 
pop_Box.style.height = h; 
pop_Box.style.width = w; 
pop_Iframe.style.height = h; 
pop_Iframe.style.width = w; 
if (document.documentElement.offsetWidth < 950) { 
//防止正常宽度下点击时 在 ff 下出现页面滚动到顶部 
document.documentElement.style.overflowX = "hidden"; 
} 
} 

var exsit = document.getElementById("popBox"); 
if (!exsit) { 
var pop_Box = document.createElement("div"); 
pop_Box.id = "popBox"; 
document.getElementsByTagName("body")[0].appendChild(pop_Box); 
pop_Box.style.cssText = popCss; 
pop_Box.style.zIndex = "10"; 
var pop_Iframe = document.createElement("iframe"); // 这里如果用 div 的话，在 ie6 不能把 <select> 遮住 
pop_Iframe.id = "popIframe"; 
document.getElementsByTagName("body")[0].appendChild(pop_Iframe); 
pop_Iframe.style.cssText = popCss; 
pop_Iframe.style.zIndex = "9"; 
rePosition_mask(); 
} 
BtShow("popIframe"); 
BtShow("popBox"); 
BtShow(showId); 
var pop_Win = document.getElementById(showId); 
pop_Win.style.position = "absolute"; 
pop_Win.style.zIndex = "11"; 
var rePosition_pop = function() { 
pop_Win.style.top = document.documentElement.scrollTop + document.body.scrollTop + document.documentElement.clientHeight/2 - pop_Win.offsetHeight/2 + 'px'; 
pop_Win.style.left = document.documentElement.scrollLeft + document.body.scrollLeft + document.documentElement.clientWidth/2 - pop_Win.offsetWidth/2 + 'px'; 
} 
rePosition_pop(); 
window.onresize = function(){ 
w = document.documentElement.offsetWidth + 'px'; // 使用 scrollWidth 不能改变宽度
rePosition_mask(); 
rePosition_pop(); 
} 
window.onscroll = function(){ 
rePosition_pop(); 
} 
} 
function BtPopShow(Bid,Did) { 
var UploadBtn = document.getElementById(Bid); 
if (UploadBtn){UploadBtn.onclick = function() {BtPopload(Did);return false;}} 
} 
function BtPopHide(Bid,Did) { 
var UploadBtn = document.getElementById(Bid); 
if (UploadBtn){UploadBtn.onclick = function() {BtHide(Did);BtHide("popBox");BtHide("popIframe");return false;}} 
} 