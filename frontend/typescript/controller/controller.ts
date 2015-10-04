/// <reference path="../imports.ts"/>

class Controller extends Backbone.Router {
    routes: any;
    objects: any;

    constructor(objects: any, options?: Backbone.RouterOptions) {
        this.routes = {
            ""     : "main", 
            "top"  : "top",
            "login": "login",
            "reg"  : "reg",
            "play" : "play",
        }
        this.objects = objects;
        super(options);
    }

    main() {
        Views.main.render();
    }
    top() {
        Views.top.render();
    }
    login() {
        Views.login.render();
    }
    reg() {
        Views.reg.render();
    }

    play() {
        modal.hide();
    }
}