document.addEventListener('DOMContentLoaded', function() {
    var menuButton = document.querySelector('.menu-button');
    var menu = document.querySelector('.menu');

    menuButton.addEventListener('click', function() {
        menu.classList.toggle('active');
    });
});
