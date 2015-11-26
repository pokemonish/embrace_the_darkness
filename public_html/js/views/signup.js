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
            
            var response = this.model.send("/api/v1/auth/signup", 'POST', userDetails);
            
            response.success(function (data) {
              alert(data.Status);
            });

            return false;
            
        },

    });


    return new View();
});
