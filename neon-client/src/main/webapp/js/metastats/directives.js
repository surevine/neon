define([ 'angular', './directives/userstats', './directives/commitgraph',
		'./directives/punchcard', './directives/languages' ], function(angular,
		userStats, commitgraph, punchcard, languages) {
	return angular.module('profiles.directives', []).directive('metastatsUser',
			userStats).directive('metastatsCommitGraph', commitgraph)
			.directive('metastatsPunchcard', punchcard).directive(
					'metastatsLanguages', languages);
});