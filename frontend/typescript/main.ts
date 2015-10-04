/// <reference path="imports.ts"/>

new Game();

var modal = new ModalControl();

var appState = new AppState();

var Views = { 
    main: new MainView(),
    top: new TopView(),
    login: new LoginView(),
    reg: new RegView(),
};


var controller = new Controller({
    views: Views,
    modal: modal,
});
    
Backbone.history.start();