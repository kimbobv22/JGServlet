(function(window,$){
	if($ === undefined){
		console.error("can't not initialize JGService, JQuery not found");
		return;
	}
	if(JGDataset === undefined){
		console.error("can't not initialize JGService, JGDataset not found");
		return;
	}
	
	function convertRequestURL(requestURL_, objects_){
		if(!isNull(objects_)){
			var copy_ = $.extend(true, objects_);
			for(var paramName_ in copy_){
				var regexp_ = new RegExp("\\{" + paramName_ + "\\}");
				if(regexp_.test(requestURL_)){
					requestURL_ = requestURL_.replace(regexp_,objects_[paramName_]);
					delete objects_[paramName_];
				}
			}
		}
		return requestURL_;
	}
	
	/**
	 * JGServlet과 통신을 돕기 위한 라이브러리
	 * 
	 * @class JGService
	 */
	var JGService = window.JGService = {
		/**
		 *요청URL을 적재하는 변수입니다.
		 *
		 *@property _requestURLs
		 *@type Object
		 *@protected
		 **/
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
		 **/
		,requestURL : function(urlKey_, objects_){
			if($.type(urlKey_) === "string" && $.type(objects_) === "string"){
				this._requestURLs[urlKey_] = objects_;
				return this._requestURLs[urlKey_];
			}
			var requestURL_ = convertRequestURL(this._requestURLs[urlKey_], objects_);
			if(!isNull(objects_)){
				requestURL_ += (requestURL_.indexOf("?") < 0 ? "?" : "&")+$.param(objects_);
			}
			return requestURL_;
		}
		/**
		 * 요청URL과 함게 비동기 호출을 수행합니다.<br>
		 * 옵션은 jQuery AJAX 옵션형식을 따릅니다.
		 *
		 *@method ajax
		 *@param {String} urlKey_ URL키
		 *@param {Object} [options_] jQuery AJAX 옵션
		 **/
		,ajax : function(urlKey_, options_){
			var requestURL_ = convertRequestURL(this.requestURL(urlKey_), options_.data);
			options_ = $.extend($.extend({
				type : "POST"
				,contentType: "application/x-www-form-urlencoded; charset=UTF-8"
			},options_),{
				url : requestURL_
			});
		
			$.ajax(options_);
		}
		/**
		 * 요청URL로 매개변수와 함께 POST를 요청합니다.
		 *
		 *@method postService
		 *@param {String} urlKey_ URL키
		 *@param {Object} [parameters_] JSON형식의 매개변수
		**/
		,postService : function(urlKey_, parameters_){
			var tempForm_ = $("<form />");
			tempForm_.hide();
			tempForm_.attr("method","POST");
			tempForm_.attr("action",convertRequestURL(this.requestURL(urlKey_), parameters_));
			
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
		 *@param {Object} [parameters_] JSON형식의 매개변수
		**/
		,forwardService : function(urlKey_, parameters_){
			window.location.href = this.requestURL(urlKey_,parameters_);
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
		 * 	//옵션항목
		 * 	{
		 * 		async : "비동기여부"
		 * 		,multiple : "파일다중선택여부"
		 * 		,accept : "허용파일항목"
		 * 		,success : "요청 성공시 콜백함수"
		 * 		,fail : "요청 실패시 콜백함수"
		 * 		,error : "에러 발생시 콜백함수"
		 * 	};
		 * 
		 * @method sendMultipart
		 * @param {String} urlKey_ URL키
		 * @param {Object} [parameters_] URL매개변수
		 * @param {Object} [options_] 옵션
		 * @param {jQuery Selector} [inputFile_] Input요소 jQuery Selector
		 */
		,sendMultipart : function(urlKey_, parameters_, options_, inputFile_){
			options_ = $.extend({
				async : false
			},options_);
			parameters_ = $.extend({},parameters_);
			
			var requestURL_ = convertRequestURL(this.requestURL(urlKey_), parameters_);
			var uploadForm_ = new FormData();
			uploadForm_.append("path", options_.path);
			
			for(var key_ in parameters_){
				uploadForm_.append(key_, parameters_[key_]);
			}
			
			if(isNull(inputFile_)){
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
			options_ = $.extend(options_,{
					type : "POST"
					,contentType: "application/x-www-form-urlencoded; charset=UTF-8"
					,url : requestURL_
					,data : uploadForm_
					,cache: false
					,contentType: false
					,processData: false
					,async : true
					,success : function(result_){
						if(result_.result === 0){
							if(!isNull(options_.success)) options_.success(result_.message);
						}else{
							if(!isNull(options_.fail)) options_.fail(result_.result,result_.message);
						}
						
					},error : function(response_, error_, thrown_){
						if(!isNull(options_.error)) options_.error(reponse_, error_, thrown_);
					}
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
			
			var cookies_ = name_ + "=" + escape(value_) + "; path=/ ";
			if(expireDay_ !== undefined){
				if($.type(expireDay_) === "string")
					expireDay_ = new Date(expireDay_);
				cookies_ += ";expires=" + expireDay_.toGMTString() + ";";
			}
			document.cookie = cookies_;
			return this.cookie(name_);
		}
		/**
		 * 쿠키를 삭제합니다.
		 * 
		 * @method removeCookie
		 * @param {String} name_ 쿠키명
		 */
		,removeCookie : function(name_){
			document.cookie = name_ + "=;expires=Thu, 01 Jan 1970 00:00:01 GMT;";
		},_mobileCheckMetaData : {
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
		}
		/**
		 * 모바일여부를 검증합니다.
		 * 
		 * 	//검증가능 플렛폼
		 * 	{
		 * 		android : "안드로이드"
		 * 		,blackberry : "블랙베리"
		 * 		,ios : "iOS"
		 * 		,opera : "오페라"
		 * 		,windows : "윈도우즈"
		 * 		,any : "모든 모바일기기"
		 * 	};
		 * 
		 * @method removeCookie
		 * @param {String} [platform_] 플렛폼
		 * @return {Boolean} 검증여부
		 */
		,checkMobile : function(options_){
			return this._mobileCheckMetaData[NVL(platform_,"any").toLowerCase()];
		}
	};
	
	return JGService;
	
})(window,$);