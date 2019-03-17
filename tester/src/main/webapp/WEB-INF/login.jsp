<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html>
   <head>
      <meta charset="ISO-8859-1">
      <title>Invoice Builder</title>
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
      <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/invoiceProj.css" />
   </head>
   <body style="background-color: #1971A8;">
      <div align="center">
         <h1 class="heading">INVOICE BUILDER</h1>
      </div>
      <button type="button" onclick="autoPopulate()" hidden="true">AUTO_POPULATE</button>
      <div id="containerDiv">
         <div class="col-sm-2"></div>
         <div class="col-sm-8" style="background-color: #F1FAEE;">
            <form action="adminLogin" method="POST" id="form">
               <div align="left">
                  <h3>LOGIN<span id="mainErrorDiv"></span></h3>
                  
               </div>
               <div class="form-group col-sm-8">
                  <label for="name" class="control-label">UserName<span class="required">*</span></label>
                  <input type="text" class="form-control" id="username" name="username" placeholder="Please enter name">
               </div>
               <div class="form-group col-sm-8">
                  <label for="place" class="control-label">Password<span class="required">*</span></label>
                  <input type="password" class="form-control" id="password" name="password" placeholder="Please enter place">
               </div>
               <div class="form-group col-sm-8">
                  <button id="submitBtn" type="button" class="btn btn-primary" onclick="submitLogin()" style="width: 100%;">Submit</button>
               </div>
               
            </form>
         </div>
         <div class="col-sm-2"></div>
      </div>
   </body>
   <script type="text/javascript">
   var productids="";
   var quant={};
   var tableMap={};
   var status="";
   $(document).ready(function(){
	    status='<c:out value="${userForm.status}"></c:out>';
	   if(status=='1'){
		   $("#mainErrorDiv").after("<span class='error'>Wrong Username</span>");
	   }else if(status=='2'){
		   $("#mainErrorDiv").after("<span class='error'>Wrong Password</span>");
	   }
   });
   
   
   function submitLogin(){
	   $('.error').remove();
	   if(valid()){
		   $('#form').submit();
	   }else{
		   return false;
	   }
   }
   function valid(){
	   var valid=true;
	  if ($('#username').val() == null|| $('#username').val() == ""|| $('#username').val() == 'undefined') {
		  $("#username").after("<span class='error'>UserName is mandatory</span>");
			valid = false;
	  }
	  if ($('#password').val() == null|| $('#password').val() == ""|| $('#password').val() == 'undefined') {
		  $("#password").after("<span class='error'>Password is mandatory</span>");
			valid = false;
	  }
	  return valid;
   }
   
   
   
   function autoPopulate(){
	   $('#name').val("Pradeep Mascarenhas");
	   $('#place').val("Btm Layout");
	   $('#email').val("Pradeep@gmail.com");
	   $('#contactNumber').val("9980835293");
	   productids="1,2,3";
	   quant["1"]="3";
	   quant["2"]="6";
	   quant["3"]="1";
	  
	   
   }
   
  
   
   </script>
</html>