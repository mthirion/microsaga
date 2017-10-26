/**
 * COLLECTION TO BE SOURCED
 */

var OpenAccounts = Backbone.Collection.extend({
    
	model: OpenAccount,
    //url: "http://...",
    
    initialize: function () {
        
        this.on('add', function(model) {
            console.log('something got added');
        });
        
        this.on('remove',  function(model) {
            console.log('something got removed');
        });

        this.on('change', function(model) {
            console.log('something got changed');
        });
        
        this.on('reset', function(model) {
            console.log('something got changed');
        });
    },
    
    comparator: function (cmp) {
    	var n = cmp.get("Nom");
    	var p = cmp.get("Prenom");
        return n+p;
    },
    
});



// -----------------------------------------------------------------
// 		Examples
// ------------------------------------------------------------------
//	ADD AND DELETE
//		var the_collection = new myCollection();
//		the_collection.add(new myModel());
//		the_collection.remove(new myModel());

//	FETCHING
//		this.collection.fetch({
//			success: function(response,xhr) {},
//			error: function (errorResponse) {}
//			});
// OR
//		this.collection.fetch({
//			success: this.fetchSuccess,
//			error: this.fetchErroron	
//		});
//
//		fetchSuccess: function (collection, response) {},
//    	fetchError: function (collection, response) {
//			throw new Error("Books fetch error");
//		}

// 	GOING THROUGH
//		the_collection.forEach(function(item) {
//			var n = item.get('Nom'));
//			...
//		});
//		the_collection.first().get('Nom'); the_collection.last().get('Nom');
//		var m = the_collection.at(1);
//
// ------------------------------------------------------------------
