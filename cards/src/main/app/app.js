

var db = require("mongodb");
var dbclient = db.MongoClient;

var dbname="fis2demo";
var dbcollection="Cards";

var dblocation = "mongodb://localhost:27017/";
var dburl=dblocation + dbname;

var conn = dbclient.connect(dburl,
	function(error, db) {
	
		if (error) {
			console.log("cannot connect to db");
		}
		else {
			console.log("connected to database : " + dbname);
			//console.log(db);
		}
		
		/* -------------------
		 * INIT ACTIVE MQ CONN
		 * -------------------
		 */
		var container = require('rhea');

		var host="localhost";
		var port="5672";	// amqp protocol (tcp is stomp !)
		//var url="amqp://"+host+":"+port;
		var username='admin';
		var password='admin';

		var queue="new_card";

		container.on('connection_open', function (context) {
			console.log("connection open -> creating receiver");
		    context.connection.open_receiver(queue);
		});

		container.on('message', function (context) {
				
				var msg = context.message;
				console.log("message received :" + msg);
				
				db.collection(dbcollection, 
						function(err_get, collection) {
						
							if (err_get) {
								console.log("cannot get collection " + dbcollection + ": " +err_get);
							}else{
								console.log ("opened collection " + dbcollection);
							}
							
							// 3a. create a document
							collection.insert(msg, function (err_insert, document) {
								if (err_insert) {
									console.log("error inserting document : " +err_insert);
								}else{
									console.log ("document inserted :" + document);
								}
								
							});
						});
		});
		
		console.log ("starting AMQP receiver");
		var connection = container.connect({'port':port, 'host': host, 'username':username, 'password':password});
		
});
