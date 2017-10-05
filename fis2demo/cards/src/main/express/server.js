/**
 * CREATE THE MAIN EXPRESS SERVER
 */


/* INITIALIZE EXPRESS */
var express    =    require('express');
var app        =    express();
//var app 		= 	require('express')(); 			// require + initialize at the same time



/* RENDERING CONFIGURATION */
app.set('views',__dirname + '/views');				
//app.set('view engine', 'ejs');					// file.ejs in view.  Also Jade
//app.engine('html', require('ejs').renderFile);

app.use(express.static("public"));					// static resources in /public folder (css...)
app.use('/public', express.static("public"))



/* MIDDLEWARE */
var BodyParser = require('body-parser');
app.use(bodyParser.json());



/* DEFINE ROUTERS */
require('./router/main')(app);

var def = require ('./router/default.js');
app.use('/default', def.default_route);


// EASY HELLO WORLD ROUTE (USING LAMBDA)
//app.get('/api', (req,resp) => {
//	resp.send("text");
//});




/* START */
var port = 9997;
var server     =    app.listen(port,
	function(){
		var host = server.address().address;
		var port = server.address().port;
    	console.log("We have started our server [ bind address = "+host + ":"+port + " ]");
	}
);


