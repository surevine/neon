define([ 'angular', './directives/userstats', './directives/commitgraph', './directives/punchcard' ],
		function(angular, userStats, commitgraph, punchcard) {
			return angular.module('profiles.directives', []).directive(
					'metastatsUser', userStats).directive(
					'metastatsCommitGraph', commitgraph).directive(
							'metastatsPunchcard', punchcard);
		});