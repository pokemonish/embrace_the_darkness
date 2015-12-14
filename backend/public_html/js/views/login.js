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
        el: $('#login'),

        events: {
            'click .menu-btn': 'backToMain',
            'click #login':  function (e) {
                // e.preventDefault();
                return this.login();
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
            console.log("loginView.hide()");
            $(this.el).hide();
        },
        show: function () {
            console.log("loginView.show()");
            $(this.el).show();
            Backbone.trigger(this.getName(), this.$el);
        },
        getName: function () {
            return "login:show"
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
                  console.log(data)
                  if(data.Status=='Login passed' || data.Status=='You are alredy logged in') {
                    localStorage.setItem('logined',true)
                  }
                });
                return false;
            }

            return true;
        },
    });


    return new View();
});
