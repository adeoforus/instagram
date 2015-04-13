<?php

require_once dirname(__DIR__) . '/../../vendor/autoload.php';
use Assetic\Asset\AssetCollection;
use Assetic\Asset\FileAsset;
use Assetic\Asset\GlobAsset;
use Assetic\AssetWriter;

$styles = new AssetCollection(array(
    new GlobAsset('./assets/css/*'),
    new GlobAsset('./assets/plugins/bootstrap-3.3.2/css/*'),
    new GlobAsset('./assets/plugins/magnific-popup/*'),
//    new FileAsset('/assets/js/app.js'),
));
$styles->dump();

$am = new \Assetic\AssetManager();

// Set target path, relative to the path passed to the
// AssetWriter constructor as an argument shortly
$styles->setTargetPath('styles.css');
$am->set('styles', $styles);

$writer = new AssetWriter('/assets/build');
$writer->writeManagerAssets($am);