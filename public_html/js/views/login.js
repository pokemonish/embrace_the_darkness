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
            'click .menu-btn': 'backToMain',
            'click #login':  function (e) {
                // e.preventDefault();
                return this.login();
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

            if (userDetails['email'] && userDetails['password']) {
               var response = this.model.send("/api/v1/auth/signin", 'POST', userDetails);
            
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
