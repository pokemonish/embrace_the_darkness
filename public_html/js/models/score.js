define([
    'backbone'
], function(
    Backbone
){

    var Model = Backbone.Model.extend({
        defaults: {
            'email': '',
            'password': '',
            'name': '',
            'score': 0
        },
        sync: function(method, model, options) {
            switch(method) {
                case 'delete':
                    console.log('delete');
                    // options.url = '/api/orders/cancelOrder';
                    // return Backbone.sync('create', model, options);
                case 'login':
                    options.url = '/api/v1/auth/signin';
                    return Backbone.sync('create', model, options);
                case 'signup':
                    options.url = '/api/v1/auth/signup';
                    return Backbone.sync('create', model, options);
                case 'score':
                    options.url = '/score';
                    return Backbone.sync('create', model, options);
            }
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