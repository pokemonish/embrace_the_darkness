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

        urlRoot: '/api/v1/auth/signin'
    });

    return Model;
});