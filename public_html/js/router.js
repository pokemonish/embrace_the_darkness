define([
    'backbone',
    'views/main',
    'views/game',
    'views/login',
    'views/signup',
    'views/scoreboard'
], function(
    Backbone,
    mainView,
    gameView,
    loginView,
    signupView,
    scoreboardView
){
    var page = $('#page');

    var Router = Backbone.Router.extend({
        routes: {
            'scoreboard': 'scoreboardAction',
            'game': 'gameAction',
            'login': 'loginAction',
            'signup': 'signupAction',
            '*default': 'defaultActions'
        },
        initialize: function(el) {
            this.el = el;
            
            this.loginView = loginView({template: '#login'});
        },
        defaultActions: function () {
            mainView.render();
        },
        scoreboardAction: function () {
            // scoreboardView.render();
        },
        gameAction: function () {
            // gameView.render();
            // gameView.show();
        },
        loginAction: function () {
            console.log("YAHOO!")
            mainView.hide();
            loginView.show();
            // loginView.render();
        },
        signupAction: function () {
            // signupView.render();
        },
    });

    return new Router();
});