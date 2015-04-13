<?php
require_once __DIR__.'/../../vendor/autoload.php';
//$mode = 'prod';
$mode = 'dev';

// Create Router instance
$router = new \Bramus\Router\Router();
$routes = new App\Router();

// Before Router Middleware
$router->before('GET', '/.*', function() {
    header('X-Powered-By: adeoco');
});


//// Static route: / (homepage)
//$router->get($routes->getRoutes('homepage'), function() {
//    GLOBAL $mode;
//    include "Controller/index.php";
//});

// Static route: / (search)
$router->get($routes->getRoutes('homepage'), function() {
    GLOBAL $mode;
    // include "Controller/search.php";
   include "Controller/index.php";
});


// Thunderbirds are go!
$router->run();