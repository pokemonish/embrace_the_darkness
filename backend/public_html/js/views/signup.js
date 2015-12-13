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
        el: $('#page'),

        events: {
            'click .main__btn': 'backToMain',
            'click #signup':  function (e) {
                e.preventDefault();
                this.signup();
            }
        },

        initialize: function () {
            this.render();
        },
        render: function () {
            $(this.el).html(this.template);
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
            
            this.model.urlRoot = "/api/v1/auth/signup"
            this.model.save(userDetails, {
                success: function(user) {
                    console.log("success");
                    console.log(user);
                },
                error: function(msg) {
                    console.log("error");
                    console.log(msg);
                }
            })


            return false;
            
        },

    });


    return new View();
});
