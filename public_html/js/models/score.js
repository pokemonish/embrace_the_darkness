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
         // Lets create function which will return the custom URL based on the method type
    getCustomUrl: function (method) {
        switch (method) {
            case 'read':
                console.log('read');
                return 'http://localhost:51377/api/Books/' + this.id;
                break;
            case 'create':
                console.log('create');
                return 'http://localhost:51377/api/Books';
                break;
            case 'update':
                console.log('update');
                return 'http://localhost:51377/api/Books/' + this.id;
                break;
            case 'delete':
                console.log('delete');
                return 'http://localhost:51377/api/Books/' + this.id;
                break;
        }
    },
    // Now lets override the sync function to use our custom URLs
    sync: function (method, model, options) {
        options || (options = {});
        options.url = this.getCustomUrl(method.toLowerCase());
        
        // Lets notify backbone to use our URLs and do follow default course
        return Backbone.sync.apply(this, arguments);
    },

        // sync: function(method, options) {
        //     switch(method) {
        //         case 'delete':
        //             console.log('delete');
        //             // options.url = '/api/orders/cancelOrder';
        //             // return Backbone.sync('create', model, options);
        //         case 'login':
        //             options.url = '/api/v1/auth/signin';
        //             return Backbone.sync('create', this, options);
        //         case 'signup':
        //             options.url = '/api/v1/auth/signup';
        //             return Backbone.sync('create', this, options);
        //         case 'score':
        //             options.url = '/score';
        //             return Backbone.sync('create', this, options);
        //     }
        // },
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