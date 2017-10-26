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
		
		//db.createCollection("messages", { capped: true, size: 100000000 })
		db.collection(dbcollection, function(err_get, collection) {
			if (err_get) {
				console.log("cannot get collection " + dbcollection + ": " +err_get);
			}else{
				console.log ("opened collection " + dbcollection);
			}

			var cursor = collection.find( {}, { tailable: true } );
			console.log ("getting collection :" + dbcollection);
			var cursorStream = cursor.stream();
			
			cursorStream.on('data', function () {
		        console.log("DBTrigger fired!");
			});
			
		});
});
		
