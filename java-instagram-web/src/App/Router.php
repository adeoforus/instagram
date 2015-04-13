<?php
namespace App;

class Router {
    private $routes;
    public function __construct(){
        $base = 'java-instagram-web/';
        $this->routes=[
            'homepage'=>'/',
            'search'=>'search'
        ];
    }

    public function getRoutes($key){
        return $this->routes[$key];
    }

    public function getRouter(){
        return $this->routes;
    }
}