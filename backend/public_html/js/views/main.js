define([
    'backbone',
    'tmpl/main',
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({

        template: tmpl,
        el: $('#main'),

        events: {
            'click .login-button': 'login',
            'click .scoreboard-button': 'scoreboard',
            'click .game-button': 'game',
            'click .signup-button': 'signup'
        },

        initialize: function () {
            this.render();
            this.hide();
        },
        render: function () {
            this.$el.html(this.template());
        },
        hide: function () {
            console.log("mainView.hide()");
            $(this.el).hide();
        },
        show: function () {
            console.log("mainView.show()");
            $(this.el).show();
            Backbone.trigger(this.getName(), this.$el);
        },
        getName: function () {
            return "main:show"
        },
        login: function () {
            // console.log("Go to #login from main");
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