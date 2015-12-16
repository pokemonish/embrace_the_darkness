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
            
            var data = $(".login__form").serialize().split("&");
            var userDetails={};
            
            for(var key in data) {
                userDetails[data[key].split("=")[0]] = data[key].split("=")[1];
            };

            console.log(userDetails)
            
            this.model.urlRoot = "/api/v1/auth/signin"
            this.model.fetch({data: userDetails, type: 'POST'})


            return false;
            
        },

    });


    return new View();
});
