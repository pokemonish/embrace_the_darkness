define([
    'backbone',
    'tmpl/signup',
    'models/score'
], function(
    Backbone,
    tmpl,
    User
){

    var View = Backbone.View.extend({

        model: new User(),
        template: tmpl,
        el: $('#signup'),

        events: {
            'click .menu-btn': 'backToMain',
            'click #signup':  function (e) {
                // e.preventDefault();
                return this.signup();
            }
        },

        initialize: function () {
            this.render();
            this.hide();
        },
        render: function () {
            $(this.el).html(this.template);
        },
        hide: function () {
            console.log("signupView.hide()");
            $(this.el).hide();
        },
        show: function () {
            console.log("signupView.show()");
            console.log(this.model)
            $(this.el).show();
            Backbone.trigger(this.getName(), this.$el);
        },
        getName: function () {
            return "signup:show"
        },
        backToMain: function() {
            Backbone.history.navigate('#', {trigger: true});
        },
        signup: function() {
            // Validate here
            
            var data = $(".signup__form").serialize().split("&");
            var userDetails={};
            
            for(var key in data) {
                userDetails[data[key].split("=")[0]] = data[key].split("=")[1];
            };


<<<<<<< Updated upstream
            if (userDetails['email'] && userDetails['password']) {
                this.model.attributes['email'] = userDetails['email']
                this.model.attributes['password'] = userDetails['password']

                this.model.sync('signup', this.model, {
                    success: function(model, response, options) {
                        alert(model.Status);
                        Backbone.history.navigate('#login', {trigger: true});
                    },
                    error: function(model, response, options) {
                        console.log("signup error");
                        alert(model.Status);
                    }
                });
                return false;
            }
            /*
=======
>>>>>>> Stashed changes
            if (userDetails['email'] && userDetails['password']) {
                this.model.attributes['email'] = userDetails['email']
                this.model.attributes['password'] = userDetails['password']

                this.model.sync('signup', {
                    success: function(model, response, options) {
                        alert(model.Status);
                        Backbone.history.navigate('#login', {trigger: true});
                    },
                    error: function(model, response, options) {
                        console.log("signup error");
                        alert(model.Status);
                    }
                });
                return false;
            }
            */

            return true;
            
        },

    });


    return new View();
});
