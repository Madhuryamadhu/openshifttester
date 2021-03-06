<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
   <body style="background-color: #1971A8;">
      <div align="center">
         <h1 class="heading">INVOICE BUILDER</h1>
      </div>
      <button type="button" onclick="autoPopulate()" hidden="true">AUTO_POPULATE</button>
      <div id="containerDiv">
      <div class="col-sm-2"></div>
         <div class="col-sm-8" style="background-color: #F1FAEE;">
            <form>
               <div align="left">
                  <h3>Fill Details</h3>
               </div>
               <div class="form-group col-sm-8">
                  <label for="name" class="control-label">Name<span class="required">*</span></label>
                  <input type="text" class="form-control" id="name" name="name" placeholder="Please enter name">
               </div>
               <div class="form-group col-sm-8">
                  <label for="place" class="control-label">Place<span class="required">*</span></label>
                  <input type="text" class="form-control" id="place" name="place" placeholder="Please enter place">
               </div>
               <div class="form-group col-sm-8">
                  <label for="contactNumber" class="control-label">Contact Number<span class="required">*</span></label>
                  <input type="text" class="form-control" id="contactNumber" name="contactNumber" placeholder="Please enter contact number">
               </div>
               <div class="form-group col-sm-8">
                  <label for="email" class="control-label">Email</label>
                  <input type="text" class="form-control" id="email" name="email" placeholder="Please enter email id">
               </div>
               <div class="row col-sm-10">
                  <div class="form-group col-sm-7">
                     <label for="product" class="control-label">Product<span class="required">*</span></label>
                     <select class="form-control" id="product"></select>
                  </div>
                  <div class="form-group col-sm-3">
                     <label for="quantity" class="control-label">Quantity<span class="required">*</span></label>
                     <input type="text" class="form-control" id="quantity" name="quantity" placeholder="Quantity">
                  </div>
                  <div class="form-group col-sm-2">
                     <div class="form-group btn-container">
                        <label for="addMore" class="control-label" style="visibility: hidden;">inlineButton</label>
                        <button type="button" id="addMore" onclick="addProducts()" class="btn btn-primary addButton">+</button>
                     </div>
                  </div>
               </div>
               <div class="col-sm-10">
                  <table class="table table-hover" id="customers">
                     <thead>
                        <tr>
                           <th>Product Name</th>
                           <th>Quantity</th>
                        </tr>
                     </thead>
                     <tbody id="prodTable"></tbody>
                  </table>
               </div>
               <div class="form-group col-sm-4">
                  <button type="button" class="btn btn-primary" onclick="submitFunction()" style="width: 100%;">Submit</button>
               </div>
            </form>
         </div>
         <div class="col-sm-2"></div>
      </div>
       <form style="display: hidden" action="downloadPDF" method="POST" id="downloadfileForm" target="_blank">
         <input type="hidden" id="pdfFullPath" name="pdfFullPath" value="" />
         <input type="hidden" id="pdfFileName" name="pdfFileName" value="" />
      </form>
   </body>
   <script type="text/javascript">
   var productids="";
   var quant={};
   var tableMap={};
   $(document).ready(function(){
	   $('#customers').hide();
	   loadProductDropdown();
   });
   
   function addProducts(){
	   if(productids==""){
		   productids=$('#product').val();
	   }else{
		   productids=productids+","+$('#product').val();
	   }
	   
	   
	   if (quant[$('#product').val()] === undefined) {
		   quant[$('#product').val()]=$('#quantity').val();
		}else{
			quant[$('#product').val()]=Math.round(parseFloat(quant[$('#product').val()], 10)+parseFloat($('#quantity').val(), 10))+"";
		}
	   if (tableMap[$("#product option:selected").text()] === undefined) {
		   tableMap[$("#product option:selected").text()]=$('#quantity').val();
		}else{
			tableMap[$("#product option:selected").text()]=Math.round(parseFloat(tableMap[$("#product option:selected").text()], 10)+parseFloat($('#quantity').val(), 10))+"";
		}
	   $('#product').val("");
	   $('#quantity').val("");
	   
	   buildTable(tableMap);
	   
   }
   
   function buildTable(tableMap){
	   $('#customers tbody').empty();
	   $.each(tableMap, function(val, text) {
		   var html="";
		   html=html+"<tr>";
		   html=html+"<td>"+val+"</td>";
		   html=html+"<td>"+text+"</td>";
		   html=html+"</tr>";
		    $('#prodTable').append(html);
		    var html="";
		});
	   $('#customers').show();
   }
   
   function submitFunction(){
	   $('.error').remove();
	   if(!validate()){
		   return false;
	   }
	   
	   addProducts();
	   var dataArray = {}
		dataArray["name"] = $('#name').val();
		dataArray["contact"] = $('#contactNumber').val();
		dataArray["place"] = $('#place').val();
		dataArray["email"] =$('#email').val();
		dataArray["productIDs"] =productids;
		dataArray["quantityIDMap"] =quant;		
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "Submit",
			data : JSON.stringify(dataArray),
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				if(data.status==0){
					//window.location = "viewDetails.jsp";
					$('#containerDiv').empty();
					$('#containerDiv').append(data.htmlContent);
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
   
   
   function loadProductDropdown(){
	   var dataArray = {}
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "loadProductDropDown",
			data : JSON.stringify(dataArray),
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				$("#product").append(data.htmlContent);
			},
			error : function(e) {
				alert("ERROR: ", e);
			},
			done : function(e) {
				alert("DONE");
			}
		});
	   
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
   
   function validate(){
	   var valid=true;
	  if ($('#name').val() == null|| $('#name').val() == ""|| $('#name').val() == 'undefined') {
		  $("#name").after("<span class='error'>Name is mandatory</span>");
			valid = false;
	  }
	  if ($('#place').val() == null|| $('#place').val() == ""|| $('#place').val() == 'undefined') {
		  $("#place").after("<span class='error'>Place is mandatory</span>");
			valid = false;
	  }
	  if ($('#contactNumber').val() == null|| $('#contactNumber').val() == ""|| $('#contactNumber').val() == 'undefined') {
		  $("#contactNumber").after("<span class='error'>Contact Number is mandatory</span>");
			valid = false;
	  }
	  if(!Object.keys(tableMap).length>0){
		  if ($('#product').val() == null|| $('#product').val() == ""|| $('#product').val() == 'undefined') {
			  $("#product").after("<span class='error'>Contact Number is mandatory</span>");
				valid = false;
		  }
		  if ($('#quantity').val() == null|| $('#quantity').val() == ""|| $('#quantity').val() == 'undefined') {
			  $("#quantity").after("<span class='error'>Contact Number is mandatory</span>");
				valid = false;
		  }
	  }
	  
	  if ($('#email').val() != null&& $('#email').val() != ""&& $('#email').val() != 'undefined') {
		  if (!(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test($('#email').val()))) {
	          $("#email").after("<span class='error'>Please Enter a valid Email ID </span>");
		      valid = false;
	       }
	  }
	  if ($('#contactNumber').val() != null&& $('#contactNumber').val() != ""&& $('#contactNumber').val() != 'undefined') {
		  if (!(/^\d+$/.test($('#contactNumber').val()))) {
	          $("#contactNumber").after("<span class='error'>Please Enter a valid Mobile Number</span>");
		      valid = false;
	       }
	  }
	  
	  return valid;
   }
   
   
   //Page 2
   function downloadPdf(pdfFilename,pdfPath){
	   $("#pdfFileName").val(pdfFilename);
		$("#pdfFullPath").val(pdfPath);
		$("#downloadfileForm").submit();	   
	   
   }
   
   function goBack(){
	   window.location = "index.jsp";
   }
   
   </script>
</html>