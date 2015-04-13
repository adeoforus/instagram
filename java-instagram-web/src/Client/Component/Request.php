<?php
namespace Client\Component;

class Request {

    private $tags;
    private $sort;
    private $cloud;

    public function __construct($tags, $sort, $cloud){
        $this->tags = $tags;
        $this->sort = $sort;
        $this->cloud = $cloud;
    }

    public function getCommand(){
        return [
            'tags'=>$this->tags,
            'sort'=>$this->sort,
            'cloud'=>$this->cloud
        ];
    }

}