/**
 * CONTROLLER (TO BE SOURCED)
 */

/*
 * The controller is the main code
 * 
 */


// MODEL AND COLLECTIONS
// ---------------------
var mod = new Person();
var col = new Persons();

// VIEWS
// -----
var pv = new PersonsView({
	model : mod,
	collection : col,
	el : $('#main')
});
		
var lv = new listView({
	collection : col
});
		

// EVNENTS
// -------
EventBus().trigger('anEvent', "Hello");