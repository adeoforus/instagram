<?php
namespace Client\Component;

/**
 * Class responsible for communicating with Java Server
 * Class ClientRequest
 * @package Client\Component
 */
class ClientRequest {
    private $address;
    private $port;
    private $command;

    /**
     * Constructor
     * @param $address
     * @param $port
     * @param $command
     */
    public function __construct($address, $port, $command){
        $this->address = $address;
        $this->port = $port;
        $this->command = $command;
        $this->init();
    }

    /**
     * Setup socket connection
     */
    private function init(){

        if(! $socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'))){
            $this->showError("socket create");
        };

        socket_connect($socket, $this->address, $this->port);

        $message = $this->command."\n";
        socket_write($socket, $message, strlen($message)); //Send data

        echo "Listening to Server\n";

        if(!$response = socket_read($socket, 2048, PHP_NORMAL_READ)){
            $this->showError("socket read");
        }

        echo $response;

        socket_close($socket);
    }

    /**
     * Show error
     * @param $message
     */
    private function showError($message){
        echo ("Error: ".$message);
        exit(666);

    }

}

require_once dirname(__DIR__) . '/../../vendor/autoload.php';

$address=$port=null;
include __DIR__.'/../Resources/config/parameters.php';
echo "Testing Client Server\n";

$request = new Request("smile",0,0);
$clientRequest = new ClientRequest($address, $port, json_encode($request->getCommand()));


