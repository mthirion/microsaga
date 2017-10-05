/**
 * ROUTER FOR EXPRESS SERVER
 */


var svc = require('./handler.js');
var session = require('npm-session');

var host = "http://httpbin.org";
	
//module.exports
var route = function(app)
{
     app.get('/index',function(req,res){
        res.render('index.html');
        //req.session.key[ixd]=val;					// using session 
     });
     
     
     app.get('/about',function(req,res){
        res.render('about.html');
        //res.render('about.html', {var: 'val'});	// with variable passed to the page
    });
     
     app.get('/uuid',function(req,res){
         var dat = svc.handle(host+"/uuid",
        	 function(ret){
             	res.send(ret);
         	}
         );
      });
      
     app.get('/ip',function(req,res){
         var dat = svc.handle(host+"/ip",
        	 function(ret){
             	res.send(ret);
         	}
         );  
      });

     app.post('/create',function(req,res){
         var request_data = req.body;
         console.log (request_data); 
      });
}

module.exports = route;