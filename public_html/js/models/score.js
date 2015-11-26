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
        send: function(path, method, data){ //название может быть какое угодно
            data = JSON.stringify(data);

            return this.fetch({
                contentType: 'application/json',
                type:method || 'POST', //здесь можно писать и GET и POST
                cache:false,
                data: data,
                url:path
            });
        }


    });

    return Model;
});