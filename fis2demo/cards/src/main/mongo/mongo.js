/**
 * MONGODB ACCESS
 */
var db = require("mongodb");
var dbclient = db.MongoClient;
var dbname="fis2demo";
var dblocation = "mongodb://localhost:27017/";
var dburl=dblocation + dbname;

// 1. connect to database
var conn = dbclient.connect(dburl,
	function(error, db) {
	
		if (error) {
			console.log("cannot connect to db");
		}
		else {
			console.log("connected to database : " + dbname);
			//console.log(db);
		}
	
		// 2a. Create a collection
		//db.createCollection("Cards", function(err_create, new_collection){});
	
		var element = '{ 				\
			"card": "name", 		\
			"type": "mastercard"	\
		}';
		console.log("created a doc inline:");
		console.log(element);
		var doc = JSON.parse(element);

		// 2b. get a collection
		db.collection("Cards", 
			function(err_get, collection) {
			
				if (err_get) {
					console.log("cannot get collection : " +err_get);
				}else{
					console.log ("accessed collection : Cards");
				}
				
				// 3a. create a document
				collection.insert(doc, function (err_insert, document) {
					console.log ("document inserted");
				});
				
				// 3b. get a document
				var queryString = '{"type": "mastercard"}';
				var query = JSON.parse(queryString);
				collection.findOne(query, function(err_find, qdoc) {
					if (err_find){
						console.log ("cannot find object matching : " + queryString);
					}
					else
						console.log("found one document: " + qdoc);
				});
				
				collection.find().toArray(function(err_array, doclist) {
					if (err_arry) {
						console.log("empty collection");
					}
					else {
						console.log("found a list of documents: ");
						console.log(doclist);
					}
				});
			});
});