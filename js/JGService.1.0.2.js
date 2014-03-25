(function(window,$){
	if($ === undefined || $ === null){
		console.error("can't not initialize JGService, JQuery not found");
		return;
	}
	if(JGDataset === undefined || JGDataset === null){
		console.error("can't not initialize JGService, JGDataset not found");
		return;
	}
	
	$.fn.hasClicked = (function(mouseEvent_){
		return (this.is(mouseEvent_.target)
			|| this.has(mouseEvent_.target).length > 0);
	});
	
	window.JGUtils = {
		_mobileCheckMetaData : {
			android: function() {
				return navigator.userAgent.match(/Android/i);
			},blackberry: function() {
				return navigator.userAgent.match(/BlackBerry/i);
			},ios: function() {
				return navigator.userAgent.match(/iPhone|iPad|iPod/i);
			},opera: function() {
				return navigator.userAgent.match(/Opera Mini/i);
			},windows: function() {
				return navigator.userAgent.match(/IEMobile/i);
			},any : function(){
				return (this._mobileCheckMetaData.android() || this._mobileCheckMetaData.blackberry() || this._mobileCheckMetaData.ios() || this._mobileCheckMetaData.opera() || this._mobileCheckMetaData.windows());
			}
		},checkMobile : function(option_){
			return this._mobileCheckMetaData[Object.NVL(option_,"any").toLowerCase()];
		}
	};
	
	var JGService = window.JGService = {
		_requestURLs : {}
		,putRequestURL : function(key_, url_){
			this._requestURLs[key_] = url_;
		},requestURL : function(key_, JSONParameters_){
			var requestURL_ = this._requestURLs[key_];
			if(!Object.isNull(JSONParameters_)){
				requestURL_ += (requestURL_.indexOf("?") < 0 ? "?" : "&")+$.param(JSONParameters_);
			}
			return requestURL_;
		},ajax : function(urlKey_, options_){
			options_ = $.extend($.extend({
				type : "POST"
				,contentType: "application/x-www-form-urlencoded; charset=UTF-8"
			},options_),{
				url : this.requestURL(urlKey_)
			});
		
			$.ajax(options_);
		},postToService : function(urlKey_, parameters_){
			var tempForm_ = $("<form />");
			tempForm_.hide();
			tempForm_.attr("method","POST");
			tempForm_.attr("action",this.requestURL(urlKey_));
			
			$.each(parameters_, function(name_, value_){
				$("<input type='hidden' />")
					.attr("name", name_).attr("value", value_)
					.appendTo(tempForm_);
			});
			
			$(document.body).append(tempForm_);
			tempForm_.submit();
			tempForm_.remove();
		},forwardService : function(urlKey_, parameters_){
			window.location.href = this.requestURL(urlKey_,parameters_);
		},makeServiceKey : function(srvMap_, srvID_, options_){
			return $.extend({srvMap : srvMap_, srvID : srvID_},options_);
		},getFile : function(callback_, options_){
			options_ = $.extend({
				"multiple" : false
				,"accept" : "*/*"
			},options_);
			
			$("input[type='file'][jgmodule-common-file-input]").remove();
			fileInput_ = $("<input type='file' jgmodule-common-file-input style='display:none;'/>");
			$(document.body).append(fileInput_);
			
			fileInput_.attr("multiple",options_.multiple);
			fileInput_.attr("accept",options_.accept);
			
			fileInput_.change(function(){
				callback_(this);
			});
			
			fileInput_.click();
		},sendMultipart : function(urlKey_, parameters_, options_, inputFile_){
			options_ = $.extend({
				async : false
			},options_);
			parameters_ = $.extend({},parameters_);
			
			var uploadForm_ = new FormData();
			uploadForm_.append("path", options_.path);
			
			//append paraemters from form fields
			for(var key_ in parameters_){
				uploadForm_.append(key_, parameters_[key_]);
			}
			
			if(Object.isNull(inputFile_)){
				var that_ = this;
				
				this.getFile(function(file_){
					that_.sendMultipart(urlKey_, parameters_, options_, file_);
				}, options_);
				return;
			}else{
				var stackedIndex_ = 0;
				
				if(inputFile_.length === undefined)
					inputFile_ = $(inputFile_);
				
				var inputFileLength_ = inputFile_.length;
				for(var index_=0;index_<inputFileLength_;++index_){
					$.each(inputFile_[index_].files, function(index_, file_){
						uploadForm_.append("file-"+stackedIndex_,file_);
						++stackedIndex_;
					});
				}
			}
			this.ajax(urlKey_, {
				data : uploadForm_
				,cache: false
				,contentType: false
				,processData: false
				,async : true
				,success : function(result_){
					if(result_.result == 0){
						if(!Object.isNull(options_.success)) options_.success(result_.message);
					}else{
						if(!Object.isNull(options_.fail)) options_.fail(result_.result,result_.message);
					}
					
				},error : function(response_, error_, thrown_){
					if(!Object.isNull(options_.error)) options_.error(reponse_, error_, thrown_);
				}
			});
		},cookie : function(name_, value_, expireDay_){
			if(value_ === undefined){
				name_ = name_ + "=";
				var cookieData_ = document.cookie;
				var start_ = cookieData_.indexOf(name_);
				if(start_ !== -1){
					start_ += name_.length;
					var end_ = cookieData_.indexOf(";", start_);
					if(end_ === -1) end_ = cookieData_.length;
					return unescape(cookieData_.substring(start_, end_));
				}
				return null;
			}
			
			var cookies_ = name_ + "=" + escape(value_) + "; path=/ ";
			if(expireDay_ !== undefined){
				var expire_ = new Date();
				expire_.setDate(expire_.getDate() + expireDay_);
				cookies_ += ";expires=" + expire_.toGMTString() + ";";
			}
			document.cookie = cookies_;
		},removeCookie : function(name_){
			document.cookie = name_ + "=;expires=Thu, 01 Jan 1970 00:00:01 GMT;";
		}
	};
	
	return JGService;
	
})(window,$);