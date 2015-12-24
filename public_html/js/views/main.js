define([
    'backbone',
    'tmpl/main',
    'models/score'
], function(
    Backbone,
    tmpl,
    User
){

    var View = Backbone.View.extend({
        model: new User(),
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

            this.model.checkScore();
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