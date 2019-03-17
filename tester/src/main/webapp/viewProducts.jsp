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
      <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
      <jsp:include page="accountDropDown.jsp"></jsp:include>
      <style type="text/css">
      button {
	    margin: 0px 0px 0px 45px;
	    background: transparent;
	    border: none;
	    color: #0D3D5B;
}

      
      </style>
</head>
<body>
	<div class="col-sm-1"></div>
	<div class="col-sm-10" style="background-color: #F1FAEE;">
		<div class="container">
			<h2>Product Details</h2>
			<div class="col-sm-10">
				<table class="table table-hover invoiceTable">
					<thead class="thead-dark">
						<tr>
							<th scope="col">Sl No</th>
							<th scope="col">Product Name</th>
							<th scope="col">Price</th>
							<th scope="col">GST</th>
							<th scope="col">Create Date</th>
							<th scope="col">Status</th>
							<th scope="col">Modify</th>
							<th scope="col">Delete</th>
							<th scope="col">Hide</th>
						</tr>
					</thead>
					<tbody id="productTable"></tbody>
				</table>
			</div>
			<div class="col-sm-2"></div>
			<div class="form-group btn-container col-sm-8">
				<div class="col-sm-4">
					<button type="button" id="goBack" onclick="goBack()" class="btn btn-primary" style="width: 100%;">Back</button>
				</div>
				<div class="col-sm-4">
					<button type="button" id="createProduct" onclick="loadCreateProduct()" class="btn btn-primary" style="width: 100%;">Create Product</button>
				</div>
			</div>
		</div>
	</div>
	<div class="col-sm-1"></div>
</body>
<script type="text/javascript">

$(document).ready(function(){
	   $('.accountDropdown').hide();
	   loadProduts();
});

function loadProduts(){
	 var dataArray = {}
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "viewProduct",
			data : JSON.stringify(dataArray),
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				$("#productTable").append(data.htmlContent);
				$('.accountDropdown').hide();
			},
			error : function(e) {
				alert("ERROR: ", e);
			},
			done : function(e) {
				alert("DONE");
			}
		});
}


function deleteProduct(productId){
	
	var dataArray = {}
	dataArray["productId"]=productId;
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "deleteProduct",
		data : JSON.stringify(dataArray),
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			if (data.status == 0) {
				$('#productTable').empty();
				loadProduts();
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


function hideProduct(productId,display){
	
	var dataArray = {}
	dataArray["productId"]=productId;
	dataArray["display"]=display;
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "hideProduct",
		data : JSON.stringify(dataArray),
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			if (data.status == 0) {
				$('#productTable').empty();
				loadProduts();
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

function loadCreateProduct(){
	var dataArrays = {};

	$.ajax({
				type : "POST",
				url : "loadCreateProduct",
				data : JSON.stringify(dataArrays),
				timeout : 100000,
				success : function(data) {
					$('#containerDiv').html(data);			
				},
				error : function(e) {
					alert("ERROR: ", e);
				},
				done : function(e) {
					alert("DONE");
				}
			});
}

function modifyProduct(productId){
	var dataArray = {}
	dataArray["productId"]=productId;
	$.ajax({
		type : "POST",
		url : "getDetailsForModify",
		data : dataArray,
		timeout : 100000,
		success : function(data) {
			$('#containerDiv').html(data);	
		},
		error : function(e) {
			alert("ERROR: ", e);
		},
		done : function(e) {
			alert("DONE");
		}
	});
}

</script>
</html>