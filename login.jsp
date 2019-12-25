<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s"  uri="/struts-tags" %>
<!DOCTYPE>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="./css/style.css">
	<link rel="stylesheet" href="./css/table.css">
	<title>LOGIN画面</title>
	<%@ include file="header.jsp" %>
</head>
<body>
	<div id="main">
		<h1>ログイン画面</h1>

		<!-- 入力チェックでエラーがあればエラーを表示する -->
		<s:if test="userIdCheckList!=null && userIdCheckList.size()>0">
			<div class="messageError">
				<s:iterator value="userIdCheckList">
					<s:property/><br>
				</s:iterator>
			</div>
		</s:if>
		<s:if test="passwordCheckList!=null && passwordCheckList.size()>0">
			<div class="messageError">
				<s:iterator value="passwordCheckList">
					<s:property/><br>
				</s:iterator>
			</div>
		</s:if>

		<!-- 認証チェックでエラーがあればエラーを表示する -->
		<s:if test="isNotUserInfoMessage!=null && !isNotUserInfoMessage.isEmpty()">
			<div class="messageError">
				<s:property value="isNotUserInfoMessage"/><br>
			</div>
		</s:if>

		<!-- ログイン情報入力 -->
		<s:form action="LoginAction">
			<table class="vertical-list-table">
				<tr>
					<th><s:label value="ユーザーID"/></th>
					<!-- 保存したユーザーIDを表示する -->
					<s:if test="#session.savedUserIdFlg==true">
					<td><s:textfield name="userId" class="txt" placeholder="ユーザーID" value="%{#session.userId}"/></td>
					</s:if>
					<s:else>
					<td><s:textfield name="userId" class="txt" placeholder="ユーザーID" value="%{userId}"/></td>
					</s:else>
				</tr>
				<tr>
					<th><s:label value="パスワード"/></th>
					<td><s:password name="password" class="txt" placeholder="パスワード"/></td>
				</tr>
			</table>
			<div class="box">
				<s:if test="#session.savedUserIdFlg==true && #session.userId!=null && !#session.userId.isEmpty()">
					<s:checkbox name="savedUserIdFlg" checked="checked"/>
				</s:if>
				<s:else>
					<s:checkbox name="savedUserIdFlg"/>
				</s:else>
				<s:label value="ユーザーID保存"/>
			</div>
			<div class="submit_btn_box">
				<s:submit value="ログイン" class="submit_btn"/>
			</div>
		</s:form>

		<!-- 新規ユーザー登録ボタン -->
		<s:form action="CreateUserAction">
		<div class="submit_btn_box">
			<s:submit value="新規ユーザー登録" class="submit_btn"/>
			</div>
		</s:form>

		<!-- パスワード再設定ボタン -->
		<s:form action="ResetPasswordAction">
		<div class="submit_btn_box">
			<s:submit value="パスワード再設定" class="submit_btn"/>
		</div>
		</s:form>
	</div>
</body>
</html>