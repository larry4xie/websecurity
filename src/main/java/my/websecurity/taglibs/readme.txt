<!-- 
	domain 只能获取权限的域,与配置中的webdomain的name属性一致
	granted\anyGranted\allGranted\notGranted同时只能配置一个,配置多个时只有第一个有效  
-->
<sec:authorize domain = "user" granted = "" anyGranted = "" allGranted = "" notGranted = "">
	这段内容只能被授权的用户看到
</sec:authorize>

<!-- 
	${sessionScope[sessionScope["SEC-DOMAIN-USER"]].name}
	property el表达式格式
-->
<sec:details domain = "user" property = "name" default =  escapeXml="true" />