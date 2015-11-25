define([
    'backbone',
    'tmpl/main'
], function(
    Backbone,
    tmpl
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
            this.render();
        },
        render: function () {
            this.$el.html(this.template());
        },
        login: function () {
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