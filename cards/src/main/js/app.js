/**
 * Main APP file
 */

console.log(process.argv);

var svc = require('./httpget.js');

var host = "http://httpbin.org"
var service="/uuid";

var services = ["/uuid", "/ip"];
services.forEach(
		function (service) {	
			svc.getsvc(host+service);
		}
)

