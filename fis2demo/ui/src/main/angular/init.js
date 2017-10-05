/**
 * ANGULAR MODULE INIT
 */

var app = angular.module("angular_ui", ['ngRoute', 'ngResource']);


app.config(['$resourceProvider', function($resourceProvider) {
//	  // Don't strip trailing slashes from calculated URLs
//	  $resourceProvider.defaults.stripTrailingSlashes = false;
//
//	  $resourceProvider.defaults.actions.update = {method: 'POST' };
	}]);


module.exports.app=app;


