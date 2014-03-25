#JGServlet for JAVA
###with JGService for JavaScript
###사용하기 전, 반드시 라이센스를 확인하세요

<a name="index"></a>
#색인

###[JAVA 메뉴얼](#forJava)

[라이브러리 개요](#JavaSummary)<br>
[라이브러리 사용을 위한 환경](#JavaEnvironment)<br>
[라이브러리 설치 및 운용방법](#JavaSetup)<br>

[비지니스로직 구현하기](#javaIndex1)<br>

###[JavaScript 메뉴얼](#forJavascript)

[라이브러리 개요](#JavaScptSummary)<br>
[라이브러리 사용을 위한 환경](#JavaScptEnvironment)<br>

[JGService 기본기능](#javaScptIndex1-1)<br>
[JGServlet으로 Multipart 요청하기](#javaScptIndex2)<br>

<a name="forJava"></a>
#JAVA 메뉴얼

<a name="JavaSummary"></a>
##라이브러리 개요

* JGServlet은 JAVA Servlet환경에서 MVC패턴을 보다 쉽게 개발,유지를 할 수 있도록 설계되었습니다.
* MVC패턴, DB접속 등 다양한 라이브러리를 제공합니다.
* DB질의문과 서비스를 XML로 관리하도록 설계되었습니다.

<br>
###계략적 구조

	// 기본 Servlet
	JGMainServlet
		- JGServletErrorHandlerDef
		- JGFilterChain
		
	// Http 요청 및 응답 Servlet
	JGHttpServlet <-(상속)- JGMainServlet
		- 요청/응답 제어 -> JGActionHandler -(인스턴스호출)-> JGAction
		
	// 파일 업로드/다운로드 제어(Multipart)
	JGFileServlet
	
	// Context 리스너
	JGMainServletContextListener -(호출/적재)-> JGMainLoader
	
	// 액션핸들러
	JGActionHandler <-(해석/적재)- 서비스XML파일
		- 서비스를 해석하여 JGAction 호출
		
	// 유틸리티 클래스
	JGCommonUtils
	JGDrawUtils
	JGEncryptionUtil
	JGFileUtils
	JGReflectionUtils
	JGServletUtils
	JGStringUtils
	
	// 환경설정
	JGMainConfig <-(해석/적재)- JGConfig.properties
	
<a name="JavaEnvironment"></a>
##라이브러리 사용을 위한 환경

###JGServlet for JAVA 라이브러리는<br>
* [JDOM](http://www.jdom.org/)이 필요합니다.<br>
* [Simple JSON](https://code.google.com/p/json-simple/)이 필요합니다.<br>
* [Apache Commons FileUpload](http://commons.apache.org/proper/commons-fileupload/)가 필요합니다.<br>
* [JGDataset 라이브러리](https://github.com/kimbobv22/JGDataset) 필요합니다.<br>
* [JGDBConnection 라이브러리](https://github.com/kimbobv22/JGDBConnection)가 필요합니다.<br>

<a name="JavaSetup"></a>
##라이브러리 설치 및 운용방법

##설치

톰캣 프로젝트에 라이브러리 jar 파일을 추가합니다.

라이브러리 사용을 위한 환경설정번들파일을 수정합니다.<br>
번들파일은 반드시 WEB-INF/classes/ 경로에 존재해야만 합니다.<br>
번들파일이름은 반드리 JGConfig.properties이어야 합니다.
<a name="howToWriteConfiguration"></a>
####환경설정파일

	jg.common.debugLevel=디버그레발
	jg.common.characterEncoding=글자엔코딩 
	
	// XML파일 최소뿌리경로는 WEB-INF/classes/ 입니다.
	jg.common.serviceDirectoryPath=서비스XML파일경로
	jg.common.DBXMLQueryDirectoryPath=DB질의문XML파일경로
	
	// 파일업로드/다운로드 경로는 절대경로입니다.
	jg.common.fileUploadRootPath=/파일업로드경로
	
	// 파일 업로드 시, 정규식으로 파일 업로드를 제한할 수 있습니다.
	jg.common.fileUploadAcceptRegexp=[\w\-\.]+[\.](php|jsp|asp)
	
	// 이미지파일 업로드 시, 이미지 압축여부입니다.
	// true일 경우 모든 이미지는 jpg 형식으로 지정됩니다.
	jg.common.compressionUploadImage=[true|false]
	
	// DB 접속을 위한 설정입니다. 
	// 복수의 DB접속환경을 정의할 수 있습니다.
	// 형식 : jg.db.config.000, jg.db.config.001, jg.db.config....
	jg.db.config.000.JDBCClassName=JDBC클래스명
	jg.db.config.000.url=DB URL
	jg.db.config.000.userName=사용자명
	jg.db.config.000.password=사용자암호
	jg.db.config.000.characterEncoding=글자엔코딩
	
	jg.db.config.001.JDBCClassName=JDBC클래스명
	jg.db.config.001.url=DB URL
	jg.db.config.001.userName=사용자명
	jg.db.config.001.password=사용자암호
	jg.db.config.001.characterEncoding=글자엔코딩
	
	...
	
	// 필요에 따라 사용자지정 환경설정을 정의할 수 있습니다.
	// 형식 : jg.custom.환경설정명
	jg.custom.myConfig1=...
	jg.custom.myConfig2=...

환경설정을 수정한 후, 서비스제어를 위한 Servlet과 를 상속받아 구현합니다.<br>

	public class MyHttpServlet extends JGHttpServlet{
	
		...
		
	}

web.xml에 구현한 Servlet을 매핑합니다.<br>
또한 Context 구동 시, 서비스XML, DB질의문, 환경설정 적재를 위한 Servlet Context Listener를 설정해야 합니다.

####web.xml
	
	sdfsdf	
	
	// servlet 설정
	<servlet>
		<servlet-name>myServlet</servlet-name>
		<servlet-class>classpath.MyHttpServlet</servlet-class>
		...
	</servlet>
	<servlet-mapping>
		<servlet-name> myServlet </servlet-name>
		<url-pattern>/myPattern</url-pattern>
		...
	</servlet-mapping>
	
	// Servlet Context Listener 설정
	<listener>
		<listener-class>com.jg.main.loader.JGMainServletContextListener</listener-class>
	</listener>
	
Servlet Context Listener가 정상적으로 설정되었다면 Context 구동 시, 개발환경에서 아래와 같은 로그가 발생합니다.

	Initializing JGServlet...
	Loading JGMainConfig...
	Loading JGActionHandler...
	Loaded service, map name : xxx
	 - Loaded action classes, total : x
	 - Loaded result pages, total : x
	 - Loaded services, total : x
	 ...

	Loading JGDBXMLQueryManager...
	Succeed to initialize JGServlet!
	
##운용방법

JGServlet의 서비스는 서비스XML을 정의하여 사용합니다.<br>
복수의 서비스XML파일을 정의할 수 있으며,<br>
환경설정 시 정의된 XML경로로부터 자동으로 서비스XML파일을 해석, 적재합니다.<br><br>

서비스 호출은 서비스키를 통하여 이루어집니다.<br>
서비스키 파라미터명은 <code>srvMap,srvID</code>입니다.<br>
<code>srvMap</code>은 서비스XML의 파일명입니다.<br>
<code>srvID</code>은 해당 서비스ID 입니다.<br>

	//test.xml
	<services>
		
		<service serviceID="service">
		...
		</service>
		
	</services>
	
	// 서비스 호출 시
	http://URL주소?srvMap=test&srvID=service

###서비스XML 작성방법
<br>
####서비스XML 기본형식

서비스XML파일에서는 __액션클래스, 결과페이지, 서비스, 필터__ 를 정의를 할 수 있습니다.<br>

	// 액션클래스 정의
	<actionClasses>
		<class name="액션클래스키값">클래스경로</class>
		...
	</actionClasses>
	
	// 결과페이지 정의
	<resultPages>
		<page name="결과페이지키값">결과페이지경로</page>
		...
	</resultPages>
	
	// 서비스 정의
	<service serviceID="서비스ID" actionClassName="액션클래스키값" mappingMethod="매핑메소드명" forwardServiceID="포워딩서비스ID" isPrivate="service사유여부">
		<result code="결과코드" pageName="결과페이지키값"/>
		...
	</service>
	
	// 필터 정의
	// 필터는 클라이언트에서 최초 서비스가 요청되었을 때 등록된 모든 필터가 호출됩니다.
	<filters>
		<filter serviceID="서비스ID" localFilter="현지필터여부">
		
		// localFilter가 true 이면 요청 서비스키와 필터 서비스키의 Map이 같아야 호출됩니다.
		// false 일 경우에는 Map과 상관없이 무조건 호출됩니다.
		
	</filters>
	
<br>
####액션클래스 정의

액션클래스 정의는 JGAction 클래스를 상속구현한 클래스를 매핑시켜 서비스호출을 용이하도록 합니다.

#####XML
	
	// 액션클래스 정의
	<actionClasses>
		<class name="testAction">com.jg.action.example.TestAction</class>
		// 복수의 액션클래스를 정의할 수 있습니다.
		...
	</actionClasses>
#####JAVA

	package com.jg.action.example;
	
	import com.jg.action.JGAction;
	import com.jg.action.handler.JGServiceBox;
	
	public class TestAction extends JGAction{
		...
	}
<br>
####결과페이지 정의

결과페이지를 미리 정의하여 사용할 수 있습니다.<br>

	// 결과페이지 정의
	<resultPages>
		<page name="testPage">/jsp/test.jsp</page>
		// 복수의 결과페이지를 정의할 수 있습니다.
		...
	</resultPages>
	
	// 서비스와 매핑(단순 포워드)
	<service serviceID="서비스ID">
		<result pageName="testPage"/>
	</service>
<br>
<a name="javaServiceDefinition"></a>
####서비스 정의 및 결과페이지 제어

JGServlet의 모든 Http 요청은 서비스를 통하여 이루어집니다.<br>
액션클래스가 작업을 수행에 따른 결과값에 따라 결과페이지를 제어할 수 있습니다.

#####XML

	// 기본 서비스 정의
	<service serviceID="서비스ID" actionClassName="액션클래스키값" mappingMethod="매핑메소드명">
		<result code="결과코드" pageName="결과페이지키값"/>
		<result code="결과코드">결과페이지경로를 직접 입력</result>
		<result code="결과코드" serviceID="다른 서비스호출"/>
		...
	</service>
	
	// 서비스 포워딩 정의
	<service serviceID="서비스ID" forwardServiceID="포워딩서비스ID">
		<result code="결과코드" pageName="결과페이지키값"/>
		<result code="결과코드">결과페이지경로를 직접 입력</result>
		<result code="결과코드" serviceID="다른 서비스호출"/>
		...
	</service>
	
	// 결과페이지가 없는 서비스 정의
	<service serviceID="서비스ID" actionClassName="액션클래스키값" mappingMethod="매핑메소드명" />
	
	// 단순 포워드 서비스
	<service serviceID="서비스ID">
		<result pageName="결과페이지키값"/>
		//OR
		<result >결과페이지경로를 직접 입력</result>
		//OR
		<result serviceID="다른 서비스호출"/>
	</service>
	
	// 서비스는 내부에서만 호출하고 싶을 때
	// private 속성값이 true일 경우 결과값에 따른 내부호출만 가능합니다.
	<service serviceID="서비스ID" isPrivate="service사유여부">
	
	...
	
	</service>

또한, 결과페이지 제어 시 간단한 표현식을 제공합니다.

1. 파라미터 매핑 형식 : <code>${파라미터키}</code>
2. 어트리뷰트 매핑 형식 : <code>#{어트리뷰트키}</code>
		
표현식 사용예제

	<service serviceID="서비스ID">
		<result pageName="${parameterKey}"/>
		<result serviceID="${parameterKey}"/>
		<result pageName="#{attributeKey}"/>
		<result serviceID ="#{attributeKey}"/>
		<result>${parameterKey}</result>
		<result>#{attributeKey}</result>
	</service>
	
#####액션클래스

	public class TestAction extends JGAction{
		
		// 리턴값에 따라 결과페이지 제어
		public int testMethod1(JGServiceBox box_) throws Exception{
			String testParam_ = box_.getParameter("testParam");
			if(testParam_.eqauls("testValue")){
				return 0;
			}else{
				return -1;
			}
		}
		
		// 결과페이지가 필요하지 않을 경우
		public void testMethod1(JGServiceBox box_) throws Exception{
			...
		}
	}
<br>
###JGAction 상속구현으로 비지니스로직 구축

JGAction 상속구현하여 서비스XML에 매핑함으로 비지니스로직을 호출할 수 있습니다.<br>
JGAction을 상속구현함으로 파일 다운로드/업로드 등 기능을 간편하게 이용할 수 있습니다.<br>
비지니스로직 구축에 대한 자세한 내용은 [여기](#javaIndex1-1)를 참조하세요.
<br>

<a name="javaIndex1"></a>
##비지니스로직 구현하기

액션클래스를 통하여 비지니스로직을 구현하도록 설계되어 있기 때문에<br>
비지니스로직은 액션클래스를 통하여 구현하는 것을 권장합니다.<br>
<br>

<a name="javaIndex1-1"></a>
###JGAction 상속구현으로 비지니스로직 구축

JGAction 상속구현하여 서비스XML에 매핑함으로 비지니스로직을 호출할 수 있습니다.<br>
<br>
####JGAction 상속구현의 기본형식	
	public class TestAction extends JGAction{
		
		// 필수 구현항목 - 액션클래스가 초기화되었을 때 호출됩니다.
		protected void initAction(JGServiceBox serviceBox_){
		....
			
			// 액션클래스 초기화 시 액션클래스를 세션에 인스턴스화 여부를 선택할 수 있습니다.
			// 인자값이 true일 경우 사용자 세션에 인스턴스화 합니다.
			setActionInstantiated();
			
			// 인스턴스화 된 액션클래스를 파기하려면 아래 메소드를 호출
			destoryAction(serviceBox_);
		}
		
		// 이 액션클래스 안에서 예외사항이 발생했을 때 호출됩니다.
		// 생략이 가능합니다.
		protected void didCaughtException(Throwable throwable_) throws Exception{
		...
		}
		
		// 이곳에 비지니스로직을 담당하는 메소드를 정의합니다.
		// Multipart가 아닌 경우는 아래와 같습니다.
		public int yourLogicMethod1(JGServiceBox box_) throws Exception{
			...
		}
		
		// Multipart일 경우는 아래와 같습니다.
		public int yourLogicMethod2(JGMultipartServiceBox box_) throws Exception{
			...
		}
		...
	}
JGAction 상속구현 시, 메소드 리턴값을 통하여 서비스 결과제어가 가능합니다.<br>

* 비지니스로직 메소드는 반드시 JGServiceBox 인자값을 지니고 있어야 합니다.<br>
* 비지니스로직 메소드의 리턴값을 생각하면 암시적으로 0의 리턴값을 가집니다.

<br>
비지니스로직 수행의 결과값에 따른 결과페이지 제어의 흐름은 [여기](#javaServiceDefinition)를 참조하세요.<br><br>
###JGServiceBox?

JGServiceBox는 HttpServletRequeset, HttpServletResponse를 담고 있는 객체이며 Client 요청과 함게 생성됩니다.<br>
또한, JGServiceBox는 Multipart 요청 시, Multipart에 대한 정보를 가질 수 있습니다.<br>
Multipart에 대한 자세한 사항은 [여기](#javaIndex2)를 참고하세요 

###AJAX요청에 따른 결과값 쓰기

AJAX요청에 따라 결과페이지 포워딩이 아닌, 결과값을 Client에 작성할 수 있습니다.

	public class TestAction extends JGAction{
		public void ajaxResponseWrite(JGServiceBox box_) throws Exception{
		
		// 문자열 쓰기 예제
		box_.writer().append("안녕");
		box_.writer().append("하세요!");
		box_.writer().print();
		
		}
	}
<br>
필요에 따라 규격화된 JSON으로 작성할 수 있습니다.

####JAVA
	box_.writer().printResultJSON(결과코드, 결과값);

####결과JSON값
	{ 
		"result" : 결과코드
		,"message" : 결과값
	}
	
###DB Connection 가져오기

비지니스로직에서 DB접속을 간단한 방법으로 호출할 수 있습니다.<br>
미리 정의된 DB접속정보를 이용해 DB 커넥션을 가져올 수 있습니다.<br>
DB접속정보 환경설정에 대한 정보는 [여기](#howToWriteConfiguration)를 참조하세요.<br>
JGDBConnection의 자세한 정보는 [여기](https://github.com/kimbobv22/JGDBConnection)를 참조하세요.

	public class TestAction extends JGAction{
		public void dbConnectionTest(JGServiceBox box_) throws Exception{
		
			//첫번째 색인으로 DB 커넥션 가져오기
			JGDBConnection connection1_ = getDBConnection();
			
			//색인으로 DB 커넥션 가져오기
			JGDBConnection connection2_ = getDBConnection(색인);
		}
	}
	
###파일 업로드/다운로드가 필요한 경우

####파일업로드
파일 업로드는 Multipart로 진행됩니다.<br>
JGAction 비지니스로직에서 JGServiceBox에 Multipart 정보를 얻을 수 있습니다.<br>
JGServiceBox에서 Multipart를 요청한 시점에 업로드는 자동진행됩니다.<br>
Multipart요청에 관한 정보는 [여기](#javaIndex1-1)를 참고하세요.

	// 아래와 같이 Multipart 데이타를 해석할 수 있습니다.
	// 만약 해당 요청이 Multipart형식이 아니라면 Exception이 발생합니다.
	public int yourLogic(JGServiceBox serviceBox_) throws Exception{
		JGMultipartData mpData_ = serviceBox_.multipartData();
		
		// 업로드된 파일결과상태 확인하기
		JGMultipartUploadResult result_ = mpData_.getUploadResult(색인);
		
		// 현재 업로드 상태확인하기
		JGUploadProgressStatus status_ = mpData_.getUploadProgressStatus();
		
		// 분석된 폼필드(파라미터) 값 가져오기 
		String value_ = mpData_.getFormFieldValue(key_);
	}

####파일다운로드 및 삭제
파일 다운로드 및 삭제는 JGFileHandler를 통해 진행됩니다.

	JGFileHandler.deleteFile(filePath_);
	JGFileHandler.sendFile();


<a name="forJavascript"></a>
#JavaScript 메뉴얼

<a name="JavaScptSummary"></a>
##라이브러리 개요

JGService for JavaScript 라이브러리는 보다 편리하게 JGservlet과 연동하도록 합니다.<br>

<a name="JavaScptEnvironment"></a>
##라이브러리 사용을 위한 환경

###JGService for JavaScript 라이브러리는<br>
* [JQuery(v1.9.x 이상)](http://jquery.com/)가 필요합니다.<br>
* JGDataset for JavaScript가 필요합니다.<br>
* JGServlet for JAVA와 연동됩니다. 

<a name="javaScptIndex1-1"></a>
##JGService 기본기능

JGService는 JavaScript 상에서 <code>JGService</code>로 호출 가능합니다.

###JGServlet으로 서비스 요청보내기

####요청 URL 가져오기
		
	//요청 URL 설정
	JGService.putRequestURL(키값,URL);
		
	//요청 URL 가져오기
	JGService.requsetURL(키값, JSON형식의 파라미터);

	//예제
	JGService.putRequestURL("test","http://localhost:8090/test");
	var requestURL_ = JGService.requsetURL("test", {
		srvMap : "test"
		,srvID : "testId"
		,hello : "world"
	});
	
	// 결과값
	http://localhost:8090/test?srvMap=test&srvID=testId&hello=world
	
필요에 따라 srvMap,srvID를 포함하여 JSON형식의 파라미터를 만들 수 있습니다.

	JGService.makeServiceKey(서비스맵,서비스ID,JSON파라미터);
	
	// 예제
	var result_ = JGService.makeServiceKey("test","testID",{hello : "world"});
	
	// 결과값
	{
		srvMap : "test"
		,srvID : "testID"
		,hello : "world"
	}

<br>	
####동기방식으로 서비스 요청하기
	
	// GET 방식
	JGService.forwardService(URL키값, JSON형식의 파라미터);
	
	// POST 방식
	JGService.postToService(URL키값, JSON형식의 파라미터);
<br>
####비동기방식으로 서비스 요청하기

JQuery 라이브러리를 이용하여 서비스를 요청합니다.

	JGService.ajax(URL키값, jQueryAJAXJSON옵션);
	
	//예제
	JGService.ajax("test", {
		data : {
			srvMap : "test"
			srvID : "testId"
		}
		,success : function(result_){
		
			...
			
		},error : function(result_){
		
			...
			
		}
	});
	

<a name="javaScptIndex2"></a>
##JGServlet으로 Multipart 요청하기

	JGService.sendMultipart(URL키값, JSON파라미터, 옵션, input태그);
	