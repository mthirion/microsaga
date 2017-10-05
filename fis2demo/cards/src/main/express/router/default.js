/**
 * Default routing path
 */

var default_route = function(request, reply, next) {
	reply.write('Hello \n');
	reply.end();
	
	// if response already sent 
	if (reply._header) return; 
	
	// in that case we don't call next()
	// next() is to build a chain which ends by sending data
	
	//next();
}

module.exports.default_route = default_route;