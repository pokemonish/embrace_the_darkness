/// <reference path="../imports.ts"/>

class AppState extends Backbone.Model {
    username: string = null
    defaults() {
        return this;
    }

    login() {
        var form = $('#login_form');

        $.ajax({
            type: "POST",
            url: form.attr('action'),
            data: form.serialize(),
            success: function( response ) {
                alert( response );
            }
	   	});
    }

    signup() {
        var form = $('#signup_form');

        $.ajax({
            type: "POST",
            url: form.attr('action'),
            data: form.serialize(),
            success: function( response ) {
                alert( response );
            }
        });
    }
}