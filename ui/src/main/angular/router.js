/**
 * DEFINE ROUTERS
 * Router work with SPA (only one page)
 * => redirect within the single Angular html page
 * So this works with "hashes", not pages
 * The link is made in the ng-view directive
 */

app.config(function($routeProvider) {
	$routeProvider
	.when('/',{							// page.html#/
		template: "Welcome"				// text or full html
										// text goes within the ng-view segment only
	})
	.when('/persons',{					// page.html#/persons
		template: "The persons"
	})
	.when('/fragment',{					// page.html#/fragment
		templateUtl: "newpage.html"		// get another page on the same local server ! --> AJAX localhost
										// the page can be a fragment as it goes only within the ng-view section
	})
	.otherwise({
		redirectTo: '/'
	})
});