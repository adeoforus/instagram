<?php
namespace Client\Component;

class Client {

    private $address;
    private $port;

    public function __construct($address, $port){
        set_time_limit(0);
        $this->address = $address;
        $this->port = $port;
        $this->init();
    }

    private function init(){

        //create socket
        if(! $socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP)){
            $this->showError('socket create');
        }
        echo "Server Created\n";

        //bind socket
        if(!socket_bind($socket, $this->address, $this->port)){
            $this->showError('socket bind');
        }
        echo "Server bind to $this->address and $this->port \n";

        if(!socket_listen($socket)){
            $this->showError('socket listen');
        }
        echo "Server Listening \n";

        do{
            $client = socket_accept($socket);
            echo "connection established\n";

            $message = "\n Hey! welcome to the server\n";
            socket_write($client, $message, strlen($message));

            do{
                if(! socket_recv($socket, $clientMessage, 2045, MSG_WAITALL)){
                    $this->showError('socket receive');
                }
//                if(!$clientMessage = socket_read($client, 10000, PHP_NORMAL_READ)){
//                    $this->showError('socket read');
//                }
                $message = "Command Received\n";
                echo $clientMessage;

                socket_send($client, $message, strlen($message), 0);

                if(!$clientMessage = trim($clientMessage)){
                    continue;
                }

                if(trim($clientMessage) == 'close'){
                    socket_close($client);
                    echo "\n\n--------------------------------------------\n".
                        "ClientRequest terminated\n";
                    break 1;
                }
            }while(true);

        }while(true);

        socket_close($socket);
        echo "Client Server Ended\n";

    }

    private function showError($message){
        echo ("Error: ".$message);

        exit(666);

    }

}

$address=$port=null;
include __DIR__.'/../Resources/config/parameters.php';
echo "Testing Client Server\n";
$client = new Client($address, 9000);
