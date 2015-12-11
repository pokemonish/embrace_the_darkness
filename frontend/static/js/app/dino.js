define([
	require
], function(
	) {
    // Load any app-specific modules
    // with a relative require call,
    // like:

    var App = function() {
        console.log("App() create");
    }

    App.prototype.start = function() {
			console.log("App.start()")
		}

    return App;
	
});

