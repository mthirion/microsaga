/**
 * ANGULAR CONTROLLERS
 * FACTORIES --> CONTROLLER [ SCOPE ]
 */

//angular.module("angular_ui")

/* Create Factory 
 * --> create a controller with a scope
 * 	   the scope is associated to a template in the rendered DOM 
 * 	   will be referenced in the view with "ng-controller" */
app.controller("mainController", mainFunction);

app.factory('myResource',["$resource",function ($resource) {

//	return $resource('http://localhost:8081/api/user', {}, {  
//			send: {
//				method:'POST'
//			}
//		}
//	);
	
	return $resource('http://localhost:8080/api/open_account');
	//return $resource('/user/:userId', {userId:'@id'});
}])

function mainFunction($scope, $http, aService, myResource) {	// passing services $http=internal ; aService=custom
			
//			----------------
//			INIT STATIC DATA
//			-----------------
			$scope.inputLastName="Thirion";		
			$scope.inputFirstName="Michael";
			$scope.inputAge="67";
			$scope.inputEmail="mthirion@redhat.com";
			$scope.inputTel="0497138768";
			$scope.inputRegistration="81.02.26-069.82";
			
			$scope.inputAccount="Standard";
			$scope.inputCard="Visa";
			
	
//			---------------------------------
//			MODELS AND COLLECTIONS MANAGEMENT
//			---------------------------------
//			The JSON object should look like this:
//			$scope.records={ persons: [ 
//                {Name:"Thirion",FirstName:"Michael", Age:"36"}, 
//                {Name:"Doe",FirstName:"John", Age:"26"}]
//			};
//			It's composed of a JSON object named "persons", which is an array of object
//			The view will loop through the array (ng-repeat bounded to "$scope.persons")
			
			
			$scope.records = {persons: []};
			$scope.persons = $scope.records.persons;
			

// 			--------------------
//			BUTTONS AND SERVICES
//			--------------------
			$scope.clickButton=function() {
	
				$scope.persons.push(
					{	"LastName":$scope.inputLastName,
						"FirstName":$scope.inputFirstName, 
						"Age":$scope.inputAge,
						"Tel":$scope.inputTel,
						"Email": $scope.inputEmail,
						"NationalNumber":$scope.inputRegistration,
						
						"Account":$scope.inputAccount,
						"Card":$scope.inputCard
						} 
				)
				
				//$scope.clickIt=aService.call('it');
				
			};
			
			$scope.sendButton=function() {	
				
				
				// 	----------------------------
				//	RESTFUL RESOURCES MANAGEMENT
				// 	----------------------------		
//				JSON object:
//					{
//						owner:{Nom, Prenom, Age, Address, Email, Tel, NatNum},
//						account: {type:}
//						card: {type}
//					}
				
				var res = new myResource();
			
				res.Owner={};
				res.Owner.Nom=$scope.inputLastName;
				res.Owner.Prenom=$scope.inputFirstName;
				res.Owner.Age=$scope.inputAge;	
				res.Owner.Tel=$scope.inputTel;
				res.Owner.Email=$scope.inputEmail;
				res.Owner.NationalNumber=$scope.inputRegistration;
				
				res.Account={};
				res.Account.Type=$scope.inputAccount;
				res.Card={};
				res.Card.Type=$scope.inputCard;
				
				res.$save({});
				alert('data sent!');
				
				
				//var user = User.get({userId:123}, function() {} );
				
				
				// 	---------------------------
				//	EXTERNAL SERVICES FUNCTIONS
				// 	---------------------------
				//		$http.get('url').success(function(response) { // get the record from external URL
				//			$scope.persons = response.persons;
				//		});				
			};
			
};


//var helloApp = angular.module("helloApp", []);
//helloApp.controller("HelloCtrl", function($scope) {
//	$scope.name = "Calvin Hobbes";
//});

