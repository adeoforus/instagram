<?php
require_once dirname(__DIR__) . '/../../vendor/autoload.php';
// http://localhost:8080/java-instagram-web/?tag=smile&sort=likes&cloud=on
/**
 * Handling Form
 */
$tag = $_GET['tag'];

//If exist sort
if(isset($_GET['sort'])){
    $sort = $_GET['sort'];
}

//If exist cloud
if(isset($_GET['cloud'])){
    $cloud = $_GET['cloud'];
}

/**
 * Communicating with Server
 */
$address=$port=null;
include __DIR__.'/../Resources/config/parameters.php';
$request = new \Client\Component\Request("smile",0,0);

$clientRequest = new \Client\Component\ClientRequest(
    $address, $port, json_encode($request->getCommand())
);

/**
 *  Rendering Template
 */

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

$template = $twig->loadTemplate('search.html.twig');
$router = new App\Router();
$params = array();
$template->display(array(
    'param'=>$params,
    'router'=>$router->getRouter()
));


