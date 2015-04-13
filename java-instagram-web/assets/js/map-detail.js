(function($) {
  "use strict";
  var openMap = false;


  //Run function when window finished load
  //$(window).load(function() {
  //  $('#btn-map-toogle').click(function() {
  //    var target = this.hash, $target = $(target);
  //    setTimeout(function() {
  //      $('html, body').stop().animate({
  //        'scrollTop': $target.offset().top
  //      }, 300);
  //      if (!openMap) {
  //        initMapDetail();
  //      }
  //      openMap = true;
  //    }, 300);
  //    if (openMap) {
  //      return false;
  //    }
  //  });
  //});

    $(document).ready(function() {
        setTimeout(function() {
            if (!openMap) {
                initMapDetail();
            }
            openMap = true;
        }, 300);
        if (openMap) {
            return false;
        }
    });


    function initMapDetail() {
    $(function() {
        var details = $("#map-detail-job");
        console.log('hello world',details);

      if (details.length > 0) {
        var locationLat = details.data('lat'), locationLng = details .data('lng');
          details .gmap3({
          map: {
            options: {
              center: [locationLat, locationLng],
              zoom: 16,
              scaleControl: false,
              panControl: false,
              streetViewControl: false,
              overviewMapControl: false,
              zoomControl: true,
              scrollwheel: false,
              mapTypeId: google.maps.MapTypeId.ROADMAP,
              mapTypeControl: false,
              zoomControlOptions: {
                style: google.maps.ZoomControlStyle.SMALL
              },
              mapTypeControlOptions: {
                style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
              }
            }
          }, marker: {
            latLng: [locationLat, locationLng],
            options: {"icon": "/bundles/app/theme/images/pinmarker.png"}
          }
        });
      }

      //$(window).off('.affix');
      //$('#affix-box').removeData('bs.affix').removeClass('affix affix-top affix-bottom');
      //initAffix();
    });
  }

})(jQuery);

