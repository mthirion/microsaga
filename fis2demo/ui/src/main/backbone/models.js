/**
 *  MODELS TO BE SOURCED
 */

var OpenAccount = Backbone.Model.extend({
		
				defaults: {
					Nom: "Michael",
					Prenom: "Thirion",
					Age:"36",
					Email:"mthirion@redhat.com",
					Tel:"0497138768",
					NationalNumber:"81.02.26-000-069",
					AccountType:"",
					CardType:""
				},
					
			    
			    constructor: function (attributes, options) {
			        Backbone.Model.apply(this, arguments);	// call the default constructor --> like super();
			    },
					
				initialize: function() {
					
					this.on('change',  function() {	// Listen to model changes !
				     	if(this.hasChanged('Nom')){
				                console.log('The Name has been changed');
				         }
				         if(this.hasChanged('Prenom')){
				                console.log('The first name has been changed');
				         }
				         if(this.hasChanged('Age')){
				                console.log('The age has been changed');
				         }
				         if(this.hasChanged('Email')){
				                console.log('The email has been changed');
				         }
				         if(this.hasChanged('Tel')){
				                console.log('The tel has been changed');
				         }
				         if(this.hasChanged('NationalNumber')){
				                console.log('The account type has been changed');
				         }
				         if(this.hasChanged('Account')){
				                console.log('The tel has been changed');
				         }
				         if(this.hasChanged('Card')){
				                console.log('The card type has been changed');
				         }
				     });
						
					 this.on('change:Email', function () {
					         console.log('The Email has been changed');
					 });
					 
					 this.on('invalid', function () {
				         console.log('The data is not valid');
				         alert("invalid data");
					 });
					 
				     this.on('error', function(model,error){
				        	  console.log(error); 
				        	  alert("error");
				      });	 
						
				},					
				
				validate: function (attributes) {
					if (attributes.Nom ==='') {
			            return "Le nom est vide";
			        }
			        if (attributes.Prenom === "" ) {
			            return "Le prenom est vide";
			        }
			        if (attributes.Age < 0 ) {
			            return "L age n'est pas correct";
			        }
			        if (attributes.Email === '' ) {	// does not contain @
			            return "L email n'est pas correct";
			        }
			        if (attributes.Tel === '' ) {	// is not a correct number --> Util ?
			            return "L age n'est pas correct";
			        }
			        if (attributes.NationalNumber ==='' ) {
			            return "L age n'est pas correct";
			        }
			    },
			    
			    
			    // 	-----------------
			    //		Caching
			    // 	-----------------
			    //localStorage : new Store(myDB), 
			    
			    
			    //	--------------
			    //		REST
			    //	--------------
			    urlRoot: 'http://localhost:8080/api/open_account',
			    //idAttribute: 'personId',
			    
				url: function() {
					//return this.urlroot + '?name=' + this.id;
					return this.urlroot;
				},
			    
			    sync: function (method, model, options) {
			    	
			        switch(method) {
			        case 'update':
			        	alert('update');
			          options.url = this.urlRoot;
			          return Backbone.sync('create', model, options);
			        case 'create':
			        	alert('create');
			          options.url = this.urlRoot;
			          return Backbone.sync('create', model, options);
			        case 'delete':
			        	alert('delete');
			          // handle update ...
			        case 'read':
			        	alert('read');
			          // handle create ...
			      }
			    	
			    }
			   
					
});
			


//--------------------------------------------------------------------------------------------
//	Examples
//--------------------------------------------------------------------------------------------

//var p = new Person();
// create = save = POST ; update = save = PUT ; read = fetch = GET ; delete = destroy = DELETE


//p.save({}, {
//	 success: function (model, response, options) {
//		  console.log("The model has been saved to the server");
//	 },
//	 error: function (model, xhr, options) {
//		  console.log("Something went wrong while saving the model");
//	 }
//});
//
//
//var p2 = new Person({'id': '45'}).fetch();	// fetch by id
//p2.destroy();
//
//---------------------------------------------------------------------------------------------