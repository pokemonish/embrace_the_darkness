define([
    'backbone',
    'tmpl/login',
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
            'click #login':  function (e) {
                e.preventDefault();
                this.login();
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
        login: function() {
            // Validate here
            // validate("login__form")
            
            console.log("LOL");

            var userDetails = {
                email: 'admin',
                password: 'admin'
            };
            
            this.model.save(userDetails, {
                success: function(user) {
                    console.log(user.attributes.Status);
                },
                error: function(msg) {
                    console.log(msg);
                }
            });

            return false;
            
        }

    });


    return new View();
});
