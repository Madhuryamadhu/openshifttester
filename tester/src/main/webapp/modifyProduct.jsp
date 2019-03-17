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
      <jsp:include page="accountDropDown.jsp"></jsp:include>
   </head>
   <body>
      <div class="col-sm-2"></div>
      <div class="col-sm-8" style="background-color: #F1FAEE;">
         <form>
            <div align="left">
               <h3>Modify Product</h3>
            </div>
            <div class="form-group col-sm-8">
               <label for="name" class="control-label">Product Name<span class="required">*</span></label>
               <input type="text" class="form-control" id="productName" name="productName" placeholder="Please enter product name">
            </div>
            <div class="form-group col-sm-8">
               <label for="place" class="control-label">Product Price<span class="required">*</span></label>
               <input type="text" class="form-control" id="productPrice" name="productPrice" placeholder="Please enter product price">
            </div>
            <div class="form-group col-sm-8">
               <label for="contactNumber" class="control-label">GST(In Percent)</label>
               <input type="text" class="form-control" id="gst" name="gst" placeholder="Please enter GST(In Percent)">
            </div>
			<div class="form-group col-sm-8">
 			   <input type="checkbox" class="form-check-input" id="hideProduct">
 			   <label class="form-check-label" for="hideProduct">Hide the product from customer??</label>
			</div>
            <div class="form-group col-sm-8">
   		       <div class="form-group col-sm-4">
   		            <button type="button" class="btn btn-primary" onclick="loadProductDetailsPage()" style="width: 100%;">Back</button>
       		   </div>
               <div class="form-group col-sm-4">
                    <button type="button" class="btn btn-primary" onclick="modifySubmit()" style="width: 100%;">Submit</button>
               </div>
            </div>
         </form>
      </div>
      <div class="col-sm-2"></div>
   </body>
   <script type="text/javascript">
   var productId="";
   $(document).ready(function(){
	   $('#productName').val('<c:out value="${userForm.productName}"></c:out>');
	   $('#productPrice').val('<c:out value="${userForm.productPrice}"></c:out>');
	   $('#gst').val('<c:out value="${userForm.gst}"></c:out>');
	   var display='<c:out value="${userForm.display}"></c:out>';
	   if(display=='0'){
		   $('#hideProduct').prop('checked', false);
	   }else{
		   $('#hideProduct').prop('checked', true);
	   }
     });

function modifySubmit(){
	
	if(!valid()){
		return false;
	}
	
	 var dataArray = {};
	 dataArray["productId"]='<c:out value="${userForm.productId}"></c:out>';
	 dataArray["productName"]=$('#productName').val();
	 dataArray["productPrice"]=$('#productPrice').val();
	 if($('#gst').val()!=null||$('#gst').val()!=""||$('#gst').val()!='undefined'){
		 dataArray["gst"]=$('#gst').val(); 
	 }else{
		 dataArray["gst"]="0";
	 }
	    
	 if ($('#hideProduct').is(":checked")) {
		 dataArray["display"]="1";
	 }else{
		 dataArray["display"]="0";
	 }
	 
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "modifyProduct",
			data : JSON.stringify(dataArray),
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				if(data.status==0){
					loadProductDetailsPage();
				}
			},
			error : function(e) {
				alert("ERROR: ", e);
			},
			done : function(e) {
				alert("DONE");
			}
		});
}


	function valid(){
		   var valid=true;
		  if ($('#productName').val() == null|| $('#productName').val() == ""|| $('#productName').val() == 'undefined') {
			  $("#productName").after("<span class='error'>Product Name is mandatory</span>");
				valid = false;
		  }
		  if ($('#productPrice').val() == null|| $('#productPrice').val() == ""|| $('#productPrice').val() == 'undefined') {
			  $("#productPrice").after("<span class='error'>Product Price is mandatory</span>");
				valid = false;
		  }
		  if ($('#productPrice').val() != null&& $('#productPrice').val() != ""&& $('#productPrice').val() != 'undefined') {
			  if (!(/^\d+$/.test($('#productPrice').val()))) {
		          $("#productPrice").after("<span class='error'>Product Price should only be a number</span>");
			      valid = false;
		       }
		  }
		  
		  if ($('#gst').val() != null&& $('#gst').val() != ""&& $('#gst').val() != 'undefined') {
			  if (!(/^\d+$/.test($('#gst').val()))) {
		          $("#gst").after("<span class='error'>GST should only be a number</span>");
			      valid = false;
		       }
		  }
		  
		  return valid;
	   
}
	
	
   
   </script>
</html>