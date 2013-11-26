

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title></title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width">

        <link rel="stylesheet" href="resources/css/bootstrap.min.css">
        <style>
            body {
                padding-top: 50px;
                padding-bottom: 20px;
            }
        </style>
        <link rel="stylesheet" href="resources/css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="resources/css/main.css">

        <script src="resources/js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>
        <script src="resources/js/vendor/jquery-1.10.1.min.js"></script>
        

		<script type="text/javascript">
            
             function onSearch(){  
              var searchtext = $('#searchtext').val();  
              $('#content').html("Searching...");
              $.ajax({  
                type: "POST",  
                url: "http://localhost:8080/QAWeb/testQA",
                contentType: "application/x-www-form-urlencoded",
                data: "sText="+encodeURIComponent( searchtext ),  
                success: function(result){  
                  $('#content').html(result.answers);
                }                
              });  
            };;     
           


    </script>
<title>QA Search</title>
</head>
<body class="container">
		<br> <br>
		 <center><div class="Three-Dee">QA Search</h1></center> 
		<br> <br>
			
			<div class="row">
			  <div class="col-xs-6 col-md-3"></div>
			    <div class="input-group col-xs-6 col-md-6 ">
			      <input type="text" class="form-control" id="searchtext" onkeyup="if (event.keyCode == 13) document.getElementById('searchbtn').click();"
				size="50"/>
			      <span class="input-group-btn">
			        <button class="btn btn-default" type="button" id="searchbtn" onclick="onSearch()">Search</button>
			      </span>
			    </div>
			  <div class="col-xs-6 col-md-3"></div>
			</div>
			
			
			<!-- <div class="row">
		     <div class= ".col-xs-6 .col-sm-4 .col-md-3"> a </div>		
			 <div class=".col-xs-6 .col-sm-4 .col-md-6">
			    <div class="input-group  ">
			      <input type="text" class="form-control" id="searchtext" onkeyup="if (event.keyCode == 13) document.getElementById('searchbtn').click();"
				size="50"/>
			      <span class="input-group-btn">
			        <button class="btn btn-default" type="button" id="searchbtn" onclick="onSearch()">Search</button>
			      </span>
			    </div>
			  </div>
			</div> -->
	
	<br>
	<br>
	<div id="content"
		style="margin-left: auto; margin-right: auto;; width: 60%; font-family: Courier New, Helvetica, sans-serif;">

	</div>
	<div id="selector">
	</div>
</body>
</html>
