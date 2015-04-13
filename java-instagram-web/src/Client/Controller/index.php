<?php
require_once dirname(__DIR__) . '/../../vendor/autoload.php';

/*
 *  Rendering Template
 */
//TODO make asset manager work!
//require __DIR__."/../Component/Twig/template.php";

Twig_Autoloader::register();
$loader = new Twig_Loader_Filesystem('./src/Client/Resources/views');
if($mode=='prod'){
    $twig = new Twig_Environment($loader,array(
        'cache'=>'./tmp'
    ));
}else{
    $twig = new Twig_Environment($loader,array());
}

$template = $twig->loadTemplate('index.html.twig');
$router = new App\Router();
$params = array();
$template->display(array(
    'param'=>$params,
    'router'=>$router->getRouter()
));


