<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Neon Demonstrator</title>

<!-- Bootstrap -->
<link href="js/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="js/lib/bootstrap/css/bootstrap-theme.min.css"
	rel="stylesheet">
<link href="css/main.css" rel="stylesheet">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
<script data-main="js/main" src="js/lib/requirejs/require.js"></script>
  
</head>
<body role="document">

	<!-- Fixed navbar -->
	<div class="navbar navbar-fixed-top navbar-inverse" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">Neon Demonstrator</a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav pull-right">
                    <li class="dropdown" ng-controller="SearchCtrl">
                      <a class="dropdown-toggle" id="find-people-toggle">Find people <b class="caret"></b></a>
                      <ul class="dropdown-menu" id="find-people-dropdown">
                        
                        <form id="searchForm" name="searchForm" ng-model="search" ng-submit="submit(search)">
                              
                          <p>Find people with</p>
                          
                          <div class="row">
                            <div class="col-xs-4">
                              <select class="form-control" ng-model="search.filter" ng-options='option.value as option.name for option in filterOptions'  ng-click="$event.stopPropagation()"></select>
                            </div>
                            <div class="col-xs-8">
                              <div class="input-group">
                                <input type="text" class="form-control" placeholder="Search" ng-model="search.query"  ng-click="$event.stopPropagation()">
                                <span class="input-group-btn">
                                  <button class="btn btn-default" type="submit">
                                    <i class="glyphicon glyphicon-search"></i>
                                  </button>
                                </span>
                              </div>
                            </div>
                    
                          </div>
                          
                        </form>
                        
                      </ul>
                    </li>
                  
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>

	<div class="container" role="main">
		<div ng-view></div>
	</div>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="js/lib/jquery/jquery-1.11.0.js" />
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/lib/bootstrap/js/bootstrap.min.js" />
</body>
</html>