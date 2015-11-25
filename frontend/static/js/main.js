require.config({
    urlArgs: "_=" + (new Date()).getTime(),
    baseUrl: "js",
    paths: {
        jquery: "lib/jquery",
        underscore: "lib/underscore",
        backbone: "lib/backbone",
        app: "app"
    },
    shim: {
        'backbone': {
            deps: ['underscore', 'jquery'],
            exports: 'Backbone'
        },
        'underscore': {
            exports: '_'
        },
    }
});

// requirejs(['app/helloworld']);

define([
    'backbone',
    'router',
    'app/dino'
], function(
    Backbone,
    router,
    Dino
){
    Backbone.history.start({root: ""})
});
