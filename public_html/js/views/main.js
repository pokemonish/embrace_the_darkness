define([
    'backbone',
    'tmpl/main',
    'views/login'
], function(
    Backbone,
    tmpl,
    loginView
){

    var View = Backbone.View.extend({

        template: tmpl,
        el: $('#page'),

        events: {
            'click .login-button': 'login',
            'click .scoreboard-button': 'scoreboard',
            'click .game-button': 'game',
            'click .signup-button': 'signup'
        },

        initialize: function () {
            View.views = {
                loginView : loginView
            }

            console.log("App.initialize()")

            console.log(View.views)
            this.render();
        },
        render: function () {
            this.$el.html(this.template());
            // this.hide();
        },
        hide: function () {
            console.log("mainView.hide()");
            $(this.el).hide();
        },
        show: function () {
            console.log("mainView.show()");
            $(this.el).show();
        },
        login: function () {
            console.log("Go to #login from main");
            Backbone.history.navigate('#login', {trigger: true});
        },
        signup: function () {
            Backbone.history.navigate('#signup', {trigger: true});
        },
        scoreboard: function () {
            Backbone.history.navigate('#scoreboard', {trigger: true});
        },
        game: function () {
            Backbone.history.navigate('#game', {trigger: true});
        }

    });

    return new View();
});