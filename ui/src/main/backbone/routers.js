/**
 *  ROUTES TO BE SOURCED
 */

var myRouter = Backbone.Router.extend({
      
    initialize: function() {
    },

    // Once instanciated, Backbone will start listening to routes
    // The routes are identified by the URL
    // --> http://.../...#view1
    routes: {
        '': "handleDefaultRoute",
        'view1': "handleRoute1",
        'view2': "handleRoute2",
        'view/:viewid': "handleSpecificRoute"		// route with parameters
        											// the parameters are optional
    },

    handleDefaultRoute: function() {
    	alert('we re on the default route');
    },
    
    handleRoute1: function () {
    	alert('we re on road 1');
    },

    handleRoute2: function () {
    	alert('we re on road 2');
    },
    
    // will be called by ' http://.../...#view/1  ;  http://.../...#view/2  ;  etc...' 
    handleSpecificRoute: function(viewid) {
    	alert('A specific route');
    }

});

myRouter.navigate('view1');