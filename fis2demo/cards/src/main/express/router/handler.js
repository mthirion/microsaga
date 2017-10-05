/**
 * ------------------------------------
 * HANDLE ROUTING (PASS THROUGH BACKEND)
 * -------------------------------------
 */

var http = require('http'); /* Import Node.js HTTP module */
var mdb = require('../db/mongo.js');



var get = function(url, retval) {
	var result = http.get(url, function(response) {
	
		var content = "";
		var data = "";
	
		response.on('data', function(chunk) {
			content += chunk;
		});
	
		response.on('end', function() {
			if (response.statusCode === 200) {
				try {
					data = JSON.parse(content);
					//console.log(data);
					retval(data);
				} catch (error) {
					console.log(error.message);
				}
			} else {
				console.log(response.statusCode);
			}
		});
	
		response.on('error', function(error) {
			console.log(error.message);
		});
		
	
	});
}


/**
 * EXPORTS
 */
module.exports.handle = get;