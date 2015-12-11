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
            // validate("signup__form")

            var data = $(".signup__form").serialize().split("&");
            var userDetails={};
            
            for(var key in data) {
                userDetails[data[key].split("=")[0]] = data[key].split("=")[1];
            };

            if (userDetails['email'] && userDetails['password']) {
               var response = this.model.send("/api/v1/auth/signup", 'POST', userDetails);
            
                response.success(function (data) {
                  alert(data.Status);
                }); 
                return false;
            }

            return true;
            
        },

    });


    return new View();
});
