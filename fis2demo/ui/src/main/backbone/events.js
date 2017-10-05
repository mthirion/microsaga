/**
 * EVENTS TO BE SOURCED
 */

/*
 * The event mechanism can be used with any Backbone object (Model, View...)
 * 
 * Specific event handlers can be created by extending the Backbone.Event
 * This allows the creation of a Bakcbone.js "event bus"
 * Other objects can emit backbone events using the trigger() method
 */


/* INIT EVENT BUS */
var EventBus = {};
_.extend(EventBus, Backbone.Events);	// Backbone.Events.extend({}); does not work !


// 'on' and 'bind' are synonymous
// on is a "newer" word		
// In another object, it's better to use listenTo
// as listenTo allows to listen to another object

EventBus.on("anEvent", function onEvent(message) 
{
	alert("Something happened : " + message);
});

EventBus.off("anEvent", function offEvent(message) 
{
	alert("The event is gone");
});

EventBus.once("anotherEvent", function doOnce(message)
{
	alert('A one time event has been received');
});


//

EventBus.stopListening('anotherEvent');
