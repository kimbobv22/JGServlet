(function(b){if($==undefined||$==null){console.error("can't not initialize JGService, JQuery not found");return;}if(JGDataset==undefined||JGDataset==null){console.error("can't not initialize JGService, JGDataset not found");return;}var a=(function(){});a.prototype.formToJSON=(function(e){var f={};var g=e.serializeArray();$.each(g,function(){if(f[this.name]){if(!f[this.name].push){f[this.name]=[f[this.name]];}f[this.name].push(this.value||"");}else{f[this.name]=this.value||"";}});return f;});a.prototype.JSONToForm=(function(j,e){var h=$(document.createElement("form"));j=Object.NVL(j,{});if(!Object.isNull(e)){if(!Object.isNull(e.enctype)){h.attr("enctype",e.enctype);}if(!Object.isNull(e.method)){h.attr("method",e.method);}else{h.method="post";}if(!Object.isNull(e.action)){h.attr("action",e.action);}}for(var g in j){if(j.hasOwnProperty(g)){var f=$(document.createElement("input"));f.attr("type","hidden");f.attr("name",g);f.attr("value",j[g]);h.append(f);}}return h;});a.prototype.countProperties=(function(g){var e=0;for(var f in g){if(g.hasOwnProperty(f)){++e;}}return e;});a._mobileCheckMetaData={android:function(){return navigator.userAgent.match(/Android/i);},blackberry:function(){return navigator.userAgent.match(/BlackBerry/i);},ios:function(){return navigator.userAgent.match(/iPhone|iPad|iPod/i);},opera:function(){return navigator.userAgent.match(/Opera Mini/i);},windows:function(){return navigator.userAgent.match(/IEMobile/i);},any:function(){return(a._mobileCheckMetaData.android()||a._mobileCheckMetaData.blackberry()||a._mobileCheckMetaData.ios()||a._mobileCheckMetaData.opera()||a._mobileCheckMetaData.windows());}};a.prototype.checkMobile=(function(e){e=Object.NVL(e,"any").toLowerCase();var f=a._mobileCheckMetaData[e];if(!Object.isNull(f)){return f();}else{return undefined;}});a.prototype.hasClicked=(function(f,e){var g=$(f);return(g.is(e.target)||g.has(e.target).length>0);});var d=(function(){});d.prototype._requestURLs={};d.prototype.putRequestURL=(function(e,f){d.prototype._requestURLs[e]=f;});d.prototype.requestURL=(function(h,j,g){var f=d.prototype._requestURLs[h];var e="?";if(!Object.isNull(j)){f+=(e+"srvID="+j);e="&";}if(!Object.isNull(g)){f+=e+$.param(g);}return f;});d.prototype.ajax=(function(f,g,e){e=$.extend($.extend({},e),{type:"POST",url:this.requestURL(f,g)});$.ajax(e);});d.prototype.submitForm=(function(e){document.body.appendChild(e);e.submit();document.body.removeChild(e);});d.prototype.postToURL=(function(g,h,f){var e=a.JSONToForm(h,f);e.method="POST";e.action=g;this.submitForm(e);});d.prototype.postToService=(function(f,h,g,e){this.postToURL(this.requestURL(f,h),g,e);});d.prototype.forwardURL=(function(e){b.location.href=e;});d.prototype.forwardService=(function(e,g,f){this.forwardURL(this.requestURL(e,g,f));});d.prototype.fileDownload=(function(g,f){var e={workType:"get",path:f.path};if(!Object.isNull(f.makeThumb)){e.makeThumb=f.makeThumb;e.resizeRatio=f.resizeRatio;}if(!Object.isNull(f.mimeType)){e.mimeType=f.mimeType;}this.forwardService(g,null,e);});d.prototype.fileDelete=(function(g,f){var e={method:"GET",success:function(h){if(h.result==0){if(!Object.isNull(f.success)){f.success(h.message);}}else{if(!Object.isNull(f.fail)){f.fail(h.result,h.message);}}},error:function(j,k,h){if(!Object.isNull(f.error)){f.error(j,k,h);}}};e.data={workType:"del",path:f.path};this.ajax(g,null,e);});d.prototype.getFile=(function(e,f){f=$.extend({multiple:false,accept:"*/*"},f);$("input[type='file'][jgmodule-common-file-input]").remove();fileInput_=$("<input type='file' jgmodule-common-file-input style='display:none;'/>");$(document.body).append(fileInput_);fileInput_.attr("multiple",f.multiple);fileInput_.attr("accept",f.accept);fileInput_.change(function(){e(this);});fileInput_.click();});d.prototype.fileUpload=(function(k,j,h){j=$.extend({},j);var g=new FormData();g.append("path",j.path);if(Object.isNull(h)){var e=this;this.getFile(function(l){e.fileUpload(k,j,l);},j);return;}else{$.each($(h).get(0).files,function(m,l){g.append("file-"+m,l);});}this.ajax(k,null,{data:g,processData:false,contentType:false,success:function(l){if(l.result==0){if(!Object.isNull(j.success)){j.success(l.message);}}else{if(!Object.isNull(j.fail)){j.fail(l.result,l.message);}}},error:function(m,n,l){if(!Object.isNull(j.error)){j.error(reponse_,n,l);}}});if(!Object.isNull(j.progress)){var e=this;var f=null;f=setInterval(function(){e._fileUploadProgress(k,{success:function(l){j.progress(l.currentIndex,l.bytesRead,l.totalLength);},fail:function(){clearInterval(f);},error:function(m,n,l){clearInterval(f);if(!Object.isNull(j.error)){j.error(reponse_,n,l);}}});},Object.NVL(j.progressCheckDelay,500));}});d.prototype._fileUploadProgress=(function(f,e){this.ajax(f,null,{data:{workType:"prg"},method:"GET",success:function(g){if(g.result==0){if(!Object.isNull(e.success)){e.success(g.message);}}else{if(!Object.isNull(e.fail)){e.fail();}}},error:function(h,j,g){if(!Object.isNull(e.error)){e.error(reponse_,j,g);}}});});var c=(function(){});c.prototype.getCookieVal=(function(e){var f=document.cookie.indexOf(";",e);if(f==-1){f=document.cookie.length;}return unescape(document.cookie.substring(e,f));});c.prototype.getCookie=(function(h){var f=h+"=";var e=f.length;var k=document.cookie.length;var j=0;while(j<k){var g=j+e;if(document.cookie.substring(j,g)==f){return getCookieVal(g);}j=document.cookie.indexOf(" ",j)+1;if(i==0){break;}}return null;});c.prototype.putCookie=(function(h,f){var e=arguments.length;var g=(2<e)?arguments[2]:null;var j=(3<e)?arguments[3]:null;var l=(4<e)?arguments[4]:null;var k=(5<e)?arguments[5]:false;document.cookie=h+"="+escape(f)+((g==null)?"":("; expires="+g.toGMTString()))+((j==null)?"":("; path="+j))+((l==null)?"":("; domain="+l))+((k==true)?"; secure":"");});d.prototype.cookie=new c();b.JGUtils=new a();b.JGModule=new d();})(window);