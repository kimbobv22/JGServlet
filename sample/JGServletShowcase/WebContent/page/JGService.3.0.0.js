(function(window){
	if($ === undefined){
		console.error("can't not initialize JGService, JQuery not found");
		return;
	}
	
	if(JGDataset === undefined){
		console.error("can't not initialize JGService, JGDataset not found");
		return;
	}
	
	var JGService = window.JGService = {
		_requestURLs : {}
		/**
		 * 요청URL을 설정/반환합니다.<br>
		 * 1. 두번째 매개변수가 객체일 경우, 매개변수와 함께 GET방식의 URL로 반환합니다.<br>
		 * 2. 두번째 매개변수가 문자열일 경우 해당 문자열을 요청URL로 설정합니다.
		 *
		 *@method requestURL
		 *@param {String} urlKey_ URL키
		 *@param {Object} [objects_] JSON형식의 매개변수 <b>또는</b> 설정할 요청URL
		 *@return {String} 요청URL
		 *@example
		 *	//요청 URL 설정하기
		 *	JGService.requestURL("key", "http://localhost/context/{mData1}");
		 *	
		 *	//요청 URL 반환하기(매개변수 형식)
		 *	var requestURL_ = JGService.requestURL("key",{
		 *		appendPath : "sample", //경로확장
		 *		parameters : {param1 : "abc"}, //GET파리미터
		 *		mappingData : {mData1 : "xyz"} //매핑데이터
		 *	});
		 *	//결과값
		 *	// http://localhost/context/xyz/sample?param1=abc
		 **/
		,requestURL : function(requestURLKey_, data_){
			var appendPath_ = null;
			var parameters_ = null;
			var mappingData_ = null;
			
			if(this._requestURLs[requestURLKey_] === undefined){
				if($.type(data_) === "string"){
					var lastSeparator_ = data_.lastIndexOf("/");
					if(lastSeparator_ === data_.length - 1)
						data_ = data_.substring(0,lastSeparator_);
					
					this._requestURLs[requestURLKey_] = data_;
					return this._requestURLs[requestURLKey_];
				}else{
					console.error("request URL must be String");
					return;
				}
			}else if($.type(data_) === "object"){
				appendPath_ = data_.appendPath;
				parameters_ = data_.parameters;
				mappingData_ = data_.mappingData;
			}
			
			var requestURL_ = this._requestURLs[requestURLKey_];
			
			if(!isNull(appendPath_)){
				var firstSeparator_ = appendPath_.indexOf("/");
				if(firstSeparator_ != 0)
					appendPath_ = "/"+appendPath_;
				
				var lastSeparator_ = appendPath_.lastIndexOf("/");
				if(lastSeparator_ === appendPath_.length - 1)
					appendPath_ = appendPath_.substring(0,lastSeparator_);
				
				requestURL_ = requestURL_+appendPath_;
			}
			
			if(!isNull(mappingData_)){
				$.each(mappingData_,function(name_, value_){
					var regexp_ = new RegExp("\\{" + name_ + "\\}");
					if(regexp_.test(requestURL_)){
						requestURL_ = requestURL_.replace(regexp_, value_);
					}
				});
			}
			
			if(!isNull(parameters_)){
				requestURL_ += (requestURL_.indexOf("?") < 0 ? "?" : "&")+$.param(parameters_);
			}
			
			return requestURL_;
		}
		/**
		 * 요청URL과 함게 비동기 호출을 수행합니다.<br>
		 * 옵션은 jQuery AJAX 옵션형식을 따릅니다.
		 *
		 *@method ajax
		 *@param {String} requestURLKey_ URL키
		 *@param {Object} [urlData_] JSON형식의 매개변수
		 *@param {Object} [ajaxOptions_] jQuery AJAX 옵션
		 *@example
		 *	JGService.ajax("main",{
		 *		appendPath : "samplePath",
		 *		parameters : {}, // 이 항목은 무시됩니다.
		 *		mappingData : {}
		 *	},{
		 *		// jQuery AJAX Options
		 *	});
		 **/
		,ajax : function(requestURLKey_, arg1_, arg2_){
			var urlData_,ajaxOptions_;
			
			if(arg2_ === undefined){
				ajaxOptions_ = arg1_;
				urlData_ = null;
			}else{
				urlData_ = arg1_;
				ajaxOptions_ = arg2_;
			}
			
			delete urlData_.parameters; //ignore
			
			var requestURL_ = this.requestURL(requestURLKey_, urlData_);
			ajaxOptions_ = $.extend($.extend({
				type : "POST"
				,contentType: "application/x-www-form-urlencoded; charset=UTF-8"
			},ajaxOptions_),{
				url : requestURL_
			});
		
			$.ajax(ajaxOptions_);
		}
		/**
		 * 요청URL로 매개변수와 함께 POST를 요청합니다.
		 *
		 *@method postService
		 *@param {String} urlKey_ URL키
		 *@param {Object} [urlData_] JSON형식의 매개변수
		 *@example
		 *	JGService.postService("main",{
		 *		appendPath : "samplePath",
		 *		parameters : {},
		 *		mappingData : {}
		 *	});
		**/
		,postService : function(urlKey_, data_){
			var parameters_ = $.extend({},data_.parameters);
			delete data_.parameters;
			
			var tempForm_ = $("<form />");
			tempForm_.hide();
			tempForm_.attr("method","POST");
			tempForm_.attr("action",this.requestURL(urlKey_, data_));
			
			$.each(parameters_, function(name_, value_){
				$("<input type='hidden' />")
					.attr("name", name_).attr("value", value_)
					.appendTo(tempForm_);
			});
			
			$(document.body).append(tempForm_);
			tempForm_.submit();
			tempForm_.remove();
		}
		/**
		 * 요청URL로 매개변수와 함께 전송합니다.
		 *
		 *@method forwardService
		 *@param {String} urlKey_ URL키
		 *@param {Object} [urlData_] JSON형식의 매개변수
		 *@example
		 *	JGService.forwardService("main",{
		 *		appendPath : "samplePath",
		 *		parameters : {},
		 *		mappingData : {}
		 *	});
		**/
		,forwardService : function(urlKey_, data_){
			window.location.href = this.requestURL(urlKey_, data_);
		}
		/**
		 * 파일선택창을 호출합니다. 파일 선택 시, 콜백함수를 통해서 input요소를 반환합니다.
		 * 
		 * 	//옵션항목	
		 * 	{
		 * 		multiple : "파일다중선택여부"
		 * 		,accept : "허용파일항목"
		 * 	};
		 * 
		 * @method getFile
		 * @param {Function} callback_ 콜백함수
		 * @param {Object} [options_] 옵션
		 * @return {jQuery Object} Input요소
		 **/
		,getFile : function(callback_, options_){
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
			return fileInput_;
		}
		/**
		 * 요청URL로 매개변수와 함께 Multi-part 요청을 수행합니다.<br>
		 * inputFile_ 매개변수가 존재하지 않을 경우 파일선택창을 호출하여 요청을 진행합니다.
		 * 
		 * 옵션항목은 jQuery AJAX Options가 동일하며 기타 옵션은 아래와 같습니다.
		 * 	//기타옵션항목 
		 * 	{
		 * 		,multiple : "파일다중선택여부"
		 * 		,accept : "허용파일항목"
		 * 	};
		 * 
		 * @method sendMultipart
		 * @param {String} urlKey_ URL키
		 * @param {Object} [urlData_] JSON형식의 매개변수
		 * @param {Object} [ajaxOptions_] jQuery AJAX 옵션
		 * @param {jQuery Selector} [inputFile_] Input요소 jQuery Selector
		 */
		,sendMultipart : function(requestURLKey_, urlData_, options_, inputFile_){
			if(isNull(inputFile_)){
				var that_ = this;
				
				this.getFile(function(file_){
					that_.sendMultipart(requestURLKey_, urlData_, options_, file_);
				}, options_);
				return;
			}else{
				
				options_ = $.extend({
					async : false
				},options_);
				
				var parameters_ = $.extend({},urlData_.parameters);
				delete urlData_.parameters;
				
				var requestURL_ = this.requestURL(requestURLKey_, urlData_);
				var uploadForm_ = new FormData();
				
				$.each(parameters_, function(name_, value_){
					uploadForm_.append(name_, value_);
				});
				
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
			
			options_ = $.extend(options_,{
					type : "POST"
					,contentType: "application/x-www-form-urlencoded; charset=UTF-8"
					,url : requestURL_
					,data : uploadForm_
					,cache: false
					,contentType: false
					,processData: false
					,async : true
				});
			
			$.ajax(options_);
		}
		/**
		 * 쿠키를 설정/반환합니다.<br>
		 * 첫번째 매개변수만 있을 경우 반환, 아닐 경우 설정합니다.
		 * 
		 * @method cookie
		 * @param {String} name_ 쿠키명
		 * @param {String} value_ 값
		 * @param {Date} expireDay_ 만료일
		 * @return {String} 쿠키값
		 */
		,cookie : function(name_, value_, expireDay_){
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
			
			if(value_ === null){
				this.removeCookie(name_);
				return;
			}
			
			var cookies_ = name_ + "=" + escape(value_) + ";path=/;";
			var cookieDay_ = new Date();
			if(typeof expireDay_ === "string")
				cookieDay_ = new Date(expireDay_);
			else if(typeof expireDay_ === "number"){
				cookieDay_.setDate(cookieDay_.getDate() + cookieDay_);
			}
			
			cookies_ += "expires=" + cookieDay_.toGMTString() + ";";
			document.cookie = cookies_;
		},
		/**
		 * 쿠키를 삭제합니다.
		 * 
		 * @method removeCookie
		 * @param {String} name_ 쿠키명
		 */
		removeCookie : function(name_){
			document.cookie = (name_ + "=;expires=Thu, 01 Jan 1970 00:00:01 GMT; path=/;");
		}
	};
	
	return JGService;
	
})(window);