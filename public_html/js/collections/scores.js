define([
    'backbone',
    'models/score',
], function(
    Backbone,
    Score
){

    var Collection = Backbone.Collection.extend({
        model: Score,
    });

    this.model = new Score;

    var collection = new Collection;

    // Sort here
    collection.comparator = function(chapter) {
      return -chapter.get("score");     // minus <=> reverse
    };

    this.model.attributes['top'] = 10;
    this.model.sync('top', {
        success: function(model, response, options) {
            console.log(model)
            for (name in model.highscores) {

                for (_name in model.highscores[name]) {
                    var value = _name;
                    var score = model.highscores[name][_name];

                    collection.add({name: value, score: score});
                }
            }
            console.log(collection)

            Backbone.trigger("update:top", collection);
            return collection;
        }
    });
            
    // response.success(function (highScore) {

    //   console.log(highScore)

    //   for (var i = 0; i < highScore.length; ++i) {
    //     var value = getRandomArbitary(0, 100500);
    //     var name = "Летучий Голландец #" + value;
    //     var score = value;

    //     collection.add({name: name, score: score});
    // }

      
    // });

    // var N = 10;
    // for (var i = 0; i < N; ++i) {
    //     var value = getRandomArbitary(0, 100500);
    //     var name = "Летучий Голландец #" + value;
    //     var score = value;

    //     collection.add({name: name, score: score});
    // }

    // return collection;

    function getRandomArbitary(min, max) {
        return Math.round(Math.random() * (max - min) + min);
    }


    
    
});