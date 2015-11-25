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
            // validate("login__form")

            var userDetails = {
                email: 'admin',
                password: 'admin'
            };
            
            this.model.save(userDetails, {
                success: function(user) {
                    console.log(user);
                },
                error: function(msg) {
                    console.log(msg);
                }
            })

            return false;
            
        },

    });


    return new View();
});
