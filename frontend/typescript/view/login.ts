/// <reference path="../imports.ts"/>

class LoginView extends Backbone.View<AppState>{
    el = $('#gui')
    template = loginTmpl
    render(): LoginView {
        modal.show();
        $(this.el).html(this.template(AppState));
        new LoginForm();
        return this;
    }
}