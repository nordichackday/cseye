var app = {
  formatNr: function (x, addComma) {
    x = x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '&nbsp;');
    x = x.replace('.', ',');
    if (addComma === true && x.indexOf(',') === -1) {
      x = x + ',0';
    }
    return (x == '') ? 0 : x;
  },
  roundNr: function (x, d) {
    return parseFloat(x.toFixed(d));
  },
  setPath: function () {
    if (location.href.match('http://yle.fi/plus/yle')) {
      app.path = 'http://yle.fi/plus/yle/2015/' + app.projectName + '/';
    }
    else if (location.href.match('http://yle.fi')) {
      app.path = 'http://yle.fi/plus/2015/' + app.projectName + '/';
    }
    else {
      app.path = '2015/' + app.projectName + '/';
    }
  },
  getScale: function () {
    var width = $('#esi-vis').width();
    if (width >= 578) {
      $('#esi-vis').addClass('wide');
      return true;
    }
    if (width < 578) {
      $('#esi-vis').removeClass('wide');
      return false;
    }
  },
  initMediaUrls: function () {
    $.each($('.handle_img', '#esi-vis'), function (i, el) {
      $(this).attr('src', app.path + 'img/' + $(this).attr('data-src'));
    });
  },
  getData: function () {
    $.ajax({
      url: 'http://localhost/cs-eye/cseye/groovy-parser/src/main/resources/response.json',
      method: 'GET',
      dataType: 'json',
      success: function (data) {
        app.printDataWrapper(data);
      }
    });
  },
  printDataWrapper: function (data) {
    app.printPlayerTable(data);
    //app.printRoundStatistics();
  },
  printPlayerTable: function(data) {
    var table = '';
    _.each(data.teams, function(team) {
      _.each(team.players, function(player) {
        table += '<tr><td>' + player.name + '</td>';
        table += '<td>' + player.kills + '</td>';
        table += '<td>' + player.assists + '</td>';
        table += '<td>' + player.deaths + '</td>';
        table += '<td>';
        if(player.deaths) {
          table += Math.round(player.kills / player.deaths * 100) / 100;
        }
        table += '</td>';
        table += '<td data-order="';
        if(player.kills) {
          table += Math.round(player.headshots / player.kills * 100);
        }
        table += '">';
        if(player.kills) {
          table += Math.round(player.headshots / player.kills * 100) / 1 ;
        }
        table += ' %</td>';
        table += '<td>' + player.points + '</td></tr>';
      });
    });
    $('.player_table_container table tbody').html(table);
    app.initSortTable();
  },
  initSortTable: function () {
    var paging = false;
    var searching = false;
    // http://datatables.net/
    $('.dataTable').DataTable({
      language: {
        searchPlaceholder: 'Hae taulukosta',
        url: app.path + 'js/libs/English.json'
      },
      order:[[1, 'desc']],
      paging:paging,
      searching:searching
    });
  },
  initBars: function () {
    $.fn.peity.defaults.bar = {
      delimiter: ",",
      fill: ["#c0beb8", '#c6a71f'],
      height: 200,
      max: null,
      min: 0,
      padding: 0,
      width: $('.bar_wrapper').width()
    }
    $('.values_bar').width($('.bar_wrapper').width())
    $('span.bar').peity('bar')
  },
  init: function () {
    app.projectName = '';
    app.path = '';
    app.initBars();
    app.getData();
  }
}


$(document).ready(function () {
  app.init();
});