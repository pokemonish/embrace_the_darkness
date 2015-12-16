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
        defaultActions: function () {
            mainView.render();
        },
        scoreboardAction: function () {
            scoreboardView.render();
        },
        gameAction: function () {
            gameView.render();
            // gameView.show();
        },
        loginAction: function () {
            loginView.render();
        },
        signupAction: function () {
            signupView.render();
        },
    });

    return new Router();
});