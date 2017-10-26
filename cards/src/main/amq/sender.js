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
	console.log("connection open -> creating sender");
    context.connection.open_sender(queue);
});

container.on('sendable', function (context) {
		
		console.log("message sendable");
		var msg_header = '{"id": "1"}';
		var msg_body ='{"text": "my text"}';
		var msg={ properties: msg_header , body: msg_body };
		
		context.sender.send(msg);
		context.sender.detach();
        //context.connection.send(msg);
});

container.on('accepted', function (context) {
		
		console.log("message confirmed");
		var msg = context.message;
        context.connection.close();
        console.log("closing");
});


var connection = container.connect({'port':port, 'host': host, 'username':username, 'password':password});
