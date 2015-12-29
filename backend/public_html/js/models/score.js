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
<<<<<<< Updated upstream
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
=======
         // Lets create function which will return the custom URL based on the method type
>>>>>>> Stashed changes

        sync: function(method, options) {
            switch(method) {
                case 'delete':
                    console.log('delete');
                    // options.url = '/api/orders/cancelOrder';
                    // return Backbone.sync('create', model, options);
                case 'login':
                    options.url = '/api/v1/auth/signin';
                    return Backbone.sync('create', this, options);
                case 'signup':
                    options.url = '/api/v1/auth/signup';
                    return Backbone.sync('create', this, options);
                case 'score':
                    options.url = '/score';
                    return Backbone.sync('create', this, options);
                case 'top':
                    options.url = '/top';
                    return Backbone.sync('create', this, options);
            }
        },
        checkScore: function() {
            score = localStorage.getItem('score')

            if (score != null) {
                setTimeout(function() {Backbone.trigger("score:newScore", score);}, 3000)
            }
        },
        setScore: function(distanceMeter) {
            localStorage.setItem('score', distanceMeter);
        },
        removeScore: function() {
            localStorage.removeItem('score');
        },
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