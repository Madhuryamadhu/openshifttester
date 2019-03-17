<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

<head>
<style type="text/css">
.dropdown {
	position: fixed;
    right: 250px;
    top: 100px;
    z-index: 10;
}
.dropdown-menu {
	min-width: 120px;
	background: transparent;
}

</style>

</head>
<body>
<div class="dropdown col-sm-6 accountDropdown">
		<button type="button" class="btn btn-primary dropdown-toggle col-sm-3 pull-right"
			data-toggle="dropdown">Admin Action</button>
		<div class="dropdown-menu col-sm-3 pull-right">
			<button onclick="loadProductDetailsPage()" class="btn btn-primary dropDownButton" style="width:100%;">
				<!-- <span class="fa fa-history"></span> -->VIEW PRODUCTS
			</button>
			<button class="btn btn-primary dropDownButton" id="logoutButton" onclick="logout()" style="width:100%;">
				<!-- <span class="glyphicon glyphicon-off"></span>  -->LOGOUT
			</button>
		</div>
	</div>
</body>
<script type="text/javascript">
var htmltable = "";

function profile() {
	$("#ProfileButton").attr('title', "Logged in as ${sessionScope.MAIL}");
}

function logout() {

	var dataArrays = {};

	$
			.ajax({
				type : "POST",
				contentType : "application/json",
				url : "logout",
				data : JSON.stringify(dataArrays),
				dataType : 'json',
				timeout : 100000,
				success : function(data) {

					window.location = "${pageContext.request.contextPath}/#tologin";
				},
				error : function(e) {
					alert("ERROR: ", e);
				},
				done : function(e) {
					alert("DONE");
				}
			});
}


function loadProductDetailsPage(){
	var dataArrays = {};

	$.ajax({
				type : "POST",
				url : "loadViewProducts",
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
</script>
</html>