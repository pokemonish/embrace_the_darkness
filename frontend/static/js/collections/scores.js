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

    var collection = new Collection;

    // Sort here
    collection.comparator = function(chapter) {
      return -chapter.get("score");     // minus <=> reverse
    };

    var N = 10;
    for (var i = 0; i < N; ++i) {
        var value = getRandomArbitary(0, 100500);
        var name = "Летучий Голландец #" + value;
        var score = value;

        collection.add({name: name, score: score});
    }

    return collection;

    function getRandomArbitary(min, max) {
        return Math.round(Math.random() * (max - min) + min);
    }
    
});