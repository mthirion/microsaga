/**
 * ANGULAR SERVICES
 * Services are Singleton !
 */


app.service("aService", serviceFunction);

function serviceFunction() {
			this.age=Math.floor(Math.random()*10);
			this.call=function(param) {
				// external service call
				console.log("aService called");
				return this.age;
			};
};
