/**
 * RECEIVE MESSAGES FROM APACHE ACTIVE_MQ
 */


/**
 *  SEND MESSAGES TO APACHE ACTIVE_MQ
 */

var container = require('rhea');

var host="localhost";
var port="5672";	// amqp protocol (tcp is stomp !)
//var url="amqp://"+host+":"+port;
var username='admin';
var password='admin';


var queue="fis2demo";


var container = require('rhea');

container.on('connection_open', function (context) {
	console.log("connection open -> creating receiver");
    context.connection.open_receiver(queue);
});

container.on('message', function (context) {
		
		console.log("message received");
		var msg = context.message;
		console.log(msg);		

		context.receiver.detach();
        context.connection.close();
        console.log("closing");
});



var connection = container.connect({'port':port, 'host': host, 'username':username, 'password':password});
