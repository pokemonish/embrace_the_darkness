define([
    'backbone'
], function(
    Backbone
){

    var Model = Backbone.Model.extend({
        defaults: {
            'name': '',
            'score': 0
        },

        // urlRootSignin: '/api/v1/auth/signin',
        // urlRootSignup: '/api/v1/auth/signup',

        urlRoot: '/api/v1/auth/signup',


    });

    return Model;
});