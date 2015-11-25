var $page = $('#page'),
    currentScreen = 'main';

/* Constructor of the ScoreboardScreen */
function showScoreboardScreen() {
    hideMainScreen(); "Главный"
    currentScreen = 'scoreboard';
    $page.html(scoreboardTmpl());
    // Init handlers
    $page.find('.js-back').on('click', showMainScreen);
}

/* Destructor of the ScoreboardScreen */
function hideScoreboardScreen() {
    // Remove the installed event handlers
    $page.find('.js-back').off('click', showMainScreen);
}

/* Constructor of the GameScreen */
function showGameScreen() {
    hideMainScreen();
    currentScreen = 'game';
    $page.html(gameTmpl());
    // Init handlers
    $page.find('.js-back').on('click', showMainScreen);
}

/* Destructor of the GameScreen */
function hideGameScreen() {
    // Remove the installed event handlers
    $page.find('.js-back').off('click', showMainScreen);
}

/* Constructor of the LoginScreen */
function showLoginScreen() {
    hideMainScreen();
    currentScreen = 'login';
    $page.html(loginTmpl());
    
    // Init handlers
    $page.find('.js-back').on('click', showMainScreen);
}

/* Destructor of the LoginScreen */
function hideLoginScreen() {
    // Remove the installed event handlers
    $page.find('.js-back').off('click', showMainScreen);
}

function showMainScreen() {
    if (currentScreen === 'scoreboard') {
        hideScoreboardScreen();
    } else if (currentScreen === 'game') {
        hideGameScreen();
    } else if (currentScreen === 'login') {
        hideLoginScreen();
    }
    currentScreen = 'main';
    $page.html(mainTmpl());

    // Init handlers
    $page.find('.js-scoreboard').on('click', showScoreboardScreen);
    $page.find('.js-start-game').on('click', showGameScreen);
    $page.find('.js-login').on('click', showLoginScreen);
}

function hideMainScreen() {
    // Delete handlers
    $page.find('.js-scoreboard').off('click', showScoreboardScreen);
    $page.find('.js-start-game').off('click', showGameScreen);
    $page.find('.js-login').off('click', showLoginScreen);
}

showMainScreen();