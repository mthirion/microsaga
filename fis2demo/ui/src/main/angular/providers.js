/**
 * 
 */

app.provider("date", dateFunction);	// creates a dataprovider object for app.config

function dateFunction() {
	return {												// return of the Provider object
		$get : function() {
			return {										// return of the injector object
				showdate: function() {return new Date();}	// return of the function
			}		
															// we will use date.showdate(); in a controller
															// -> app.controller($scope, date) {}
			
															// also dateProvider.$get.showdate(); in app.config
															// -> app.config(function(dateProvider)) {}
		}											
	}
};