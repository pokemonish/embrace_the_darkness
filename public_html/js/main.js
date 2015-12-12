require.config({
    urlArgs: "_=" + (new Date()).getTime(),
    baseUrl: "js",
    paths: {
        jquery: "lib/jquery",
        underscore: "lib/underscore",
        backbone: "lib/backbone"
    },
    shim: {
        'backbone': {
            deps: ['underscore', 'jquery'],
            exports: 'Backbone'
        },
        'underscore': {
            exports: '_'
        }
    }
});

var App = {
    initialize : function() {

        App.views = {
            clientMain : new mainView()
        }

        console.log("App.initialize()")
    },

    showView: function(view){
        if(App.views.current != undefined){
            $(App.views.current.el).hide();
        }
        App.views.current = view;
        $(App.views.current.el).show();
    },
}

define([
    'backbone',
    'router'
], function(
    Backbone,
    router
){
    Backbone.history.start();
});
