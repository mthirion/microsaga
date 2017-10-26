/**
 * VIEWS TO BE SOURCED
 */

var FormView = Backbone.View.extend({

	initialize : function() {
	},

	render : function() {
		return this; // need to return the View
	},

	events : {
		"click .backbone_save" : "saveClicked",
		"click .backbone_submit": "submitClicked"
	},


	saveClicked : function() {
			
		var nom = $(this.el).find('#lastname-input').val();
		var prenom = $(this.el).find('#firstname-input').val();
		var age = $(this.el).find('#age-input').val();
		var email = $(this.el).find('#email-input').val();
		var tel = $(this.el).find('#tel-input').val();
		var nationalNumber = $(this.el).find('#registration-input').val();
		
		var accountType;
		var account_radios = document.getElementsByName('AccountGroup');
		for (var i = 0, length = account_radios.length; i < length; i++) {
		    if (account_radios[i].checked) {
		        accountType = account_radios[i].value;
		        break;
		    }
		}
		
		var cardType;
		var card_radios = document.getElementsByName('CardGroup');
		for (var i = 0, length = card_radios.length; i < length; i++) {
		    if (card_radios[i].checked) {
		        cardType = card_radios[i].value;
		        break;
		    }
		}
		
		var m = new OpenAccount({});
		m.set('Nom',nom, {validate:true});
		m.set('Prenom',prenom, {validate:true});
		m.set('Age',age,{validate:true});
		m.set('Email',email, {validate:true});
		m.set('Tel',tel, {validate:true});
		m.set('NationalNumber',nationalNumber,{validate:true});
		m.set('AccountType',accountType,{validate:true});
		m.set('CardType',cardType,{validate:true});
		
		this.collection.add(m);
		
		return false;	// prevent page reload
	},
	
	submitClicked : function () {		
		this.model.save();
	},
	
    change: function () {
        // this.model.save();
    },
    destroy: function () {
        // this.model.destroy({});
    },	

});

var HistoryView = Backbone.View.extend({

	tagname: 'tr',
	
	el: '#div_list',
	
	initialize : function() {

//		this.listenTo(this.collection, "add", this.render);

		// listen to collections updates
		this.collection.bind('add', this.render, this);
//		this.collection.bind("reset", this.render, this);
		
	},

	
	render : function() {
					
		var historyView = this.$el.find('#history-view');
		historyView.empty();
		
		for (var i=0; i<this.collection.length; i++) {
			var history_line = new HistoryLineView({
				model : this.collection.at(i),	// LIFO, not FIFO
			});
			historyView.append(history_line.$el, this);
			history_line.render();
		}

		return this; // need to return the View
	},

});

var HistoryLineView = Backbone.View.extend({

	tagname : 'tr',
	
	initialize : function() {

		this.template = _.template($('#historyItem').html());
	},
	
	render : function() {

		//this.$el.html(this.template(this.model.attributes));
		this.$el.replaceWith(this.template(this.model.attributes));	// to remove the surrounding <div> added to the template by underscore.js
		return this;
	}

});

