<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Neon Demonstrator</title>

<!-- Bootstrap -->
<link href="js/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="js/lib/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
<link href="css/font-awesome.min.css" rel="stylesheet">
<link href="css/main.css" rel="stylesheet">
<link href="css/metastats.css" rel="stylesheet">

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
	<div class="navbar navbar-fixed-top navbar-neon" role="navigation" ng-controller="HeaderCtrl">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
              
                <div class="app_logo">
                  <span class="separator"></span>
                  <a href="{{gitlabBaseUrl}}" class="home has_bottom_tooltip" data-original-title="Dashboard"><h1>GITLAB</h1></a>
                  <span class="separator"></span>
                </div>
              
                <a class="navbar-brand" href="#">Neon Demonstrator</a>
              
			</div>
          
			<div class="collapse navbar-collapse navbar-right">
              
                <form class="navbar-form navbar-left" role="search" id="searchForm" name="searchForm" ng-model="search" ng-controller="SearchCtrl" ng-submit="submit(search)">
                    <div class="input-group">
                      <span class="input-group-btn">
                        <button type="button" class="btn btn-default" ng-model="search.filter" data-html="1" ng-options="option.value as option.label for option in filterOptions" bs-select>
                          Action <span class="caret"></span>
                        </button>
                      </span>
                      <input type="text" class="form-control" placeholder="Search" ng-model="search.query">
                      <span class="input-group-btn">
                        <button class="btn btn-default" type="submit">
                          <i class="glyphicon glyphicon-search"></i>
                        </button>
                      </span>
                    </div>
                </form>
              
				<ul class="nav navbar-nav pull-right">
                    <li>
                      <a href="{{gitlabBaseUrl}}/public" bs-tooltip title="{{tooltips.public}}" data-placement="bottom"><i class="fa fa-globe"></i></a>
                    </li>
                    <li>
                      <a href="{{gitlabBaseUrl}}/s/root" bs-tooltip title="{{tooltips.snippets}}" data-placement="bottom"><i class="fa fa-paste"></i></a>
                    </li>
                    <li>
                      <a href="{{gitlabBaseUrl}}/admin" bs-tooltip title="{{tooltips.admin}}" data-placement="bottom"><i class="fa fa-cogs"></i></a>
                    </li>
                    <li>
                      <a href="{{gitlabBaseUrl}}/projects/new" bs-tooltip title="{{tooltips.create}}" data-placement="bottom"><i class="fa fa-plus"></i></a>
                    </li>
                    <li>
                      <a href="{{gitlabBaseUrl}}/profile" bs-tooltip title="{{tooltips.profile}}" data-placement="bottom"><i class="fa fa-user"></i></a>
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

</body>
</html>