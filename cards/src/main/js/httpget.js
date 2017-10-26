/**
 * A call to a service : GET
 */


var http = require('http');  /* Import Node.js HTTP module */

var get = function(url) {
	
	var result = http.get(url, 
			function(response) {
				
				var content= "";
				var data="";
				
				response.on('data', 
						function(chunk) {
							content += chunk;
						}
				);
							
				response.on('end',
						function() {
							if (response.statusCode === 200 ) {
								try {
									data = JSON.parse(content);
									console.log(data);
								}
								catch(error) {
									console.log(error.message);
								}
							} else {
								console.log(response.statusCode);
							} 
						}
					
				);
						
				response.on('error', 
						function(error) {
					console.log(error.message);
						}
				);
				
			}
	);
}



/**
 * EXPORTS
 */
module.exports.getsvc = get;	/* Export the local function 'get' with the name 'getsvc' 
									-- after the function is defined*/

