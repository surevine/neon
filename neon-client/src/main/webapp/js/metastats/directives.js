define([ 'angular', './directives/userstats', './directives/commitgraph' ],
		function(angular, userStats, commitgraph) {
			return angular.module('profiles.directives', []).directive(
					'metastatsUser', userStats).directive(
					'metastatsCommitGraph', commitgraph);
		});