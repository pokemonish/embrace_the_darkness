var manager = new RunnerManager('#games');

var btn = document.getElementById('details-button');
btn.onclick = function() {
    detailsButtonClick(); toggleHelpBox();
}


/*
 * Код ниже для примера.
 * Все это перекачует в runner_manager.js
 * В runner.js лежит оригинальный динозавр
 * В pika1.js и pika2.js лежит какая-то хуйня для интерфейса 
 * http://localhost:8080/dino/
 */

//Смерть нашего игрока (лучше так не перезаписывать, а залезть в runner_manager.js)
manager.onPlayerDied = function() {
    console.log('our player died');
}

//Обнуляем менеджер и задаем сид
manager.reset(458); 

//Добавляем игроков на экран
var p1 = manager.addRunner('1'); 
var p2 = manager.addRunner('2');

// Начинаем игру 
manager.start();

//Прыгаем другого игрока
setTimeout(function() {
    p1.doAction('jump', true);
}, 5000);
//Нагинаем другого игрока
setTimeout(function() {
    p1.doAction('duck', true);
}, 7000);
//Отгинаем другого игрока
setTimeout(function() {
    p1.doAction('duck', false);
}, 10000);
//Убиваем бота (будем таки верить игрокам на слово, что здохли или ждать таймаута)
setTimeout(function() {
    p1.die();
}, 14000);
