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
      url: '/match/1',
      method: 'GET',
      dataType: 'json',
      success: function (data) {
        app.printDataWrapper(data);
      }
    });
  },
  setDataInterval: function () {
    window.setInterval(function () {
      $('.timer .seconds').text(app.timeOut);
      clearInterval(app.timer);
      app.initTimer();
      app.getData();
    }, app.timeOut * 1000);
  },
  printDataWrapper: function (data) {
    app.printPlayerTable(data);
    //app.printRoundScoreStatistics(data);
    //app.printRoundStatistics(data);
    //app.printMessages(data);
    app.data = data;
  },
  isChanged: function(previous, current, attribute) {
    if(typeof previous !== 'undefined' && typeof previous[attribute] !== 'undefined' && typeof current !== 'undefined' && typeof current[attribute] !== 'undefined') {
      if(previous[attribute] !== current[attribute]) {
        return 'changed';
      }
      return '';
    }
  },
  printPlayerTable: function(data) {
    var tbody = '';

    _.each(data.teams, function(team) {
      var oldTeamIndex;
      _.each(app.data.teams, function(oldTeam, index) {
        if(team.id === oldTeam.id) {
          oldTeamIndex = index;
        }
      });
      _.each(team.players, function(player) {
        if(typeof oldTeamIndex !== 'undefined') {
          var oldPlayer = _.find(app.data.teams[oldTeamIndex].players, function(oldPlayer) {
            return player.id === oldPlayer.id;
          });
        }
        tbody += '<tr class="' + team.side + '"><td>' + player.name + '</td>';
        tbody += '<td class="' + app.isChanged(oldPlayer, player, 'kills') + '">' + player.kills + '</td>';
        tbody += '<td class="' + app.isChanged(oldPlayer, player, 'assists') + '">' + player.assists + '</td>';
        tbody += '<td class="' + app.isChanged(oldPlayer, player, 'deaths') + '">' + player.deaths + '</td>';
        tbody += '<td class="' + app.isChanged(oldPlayer, player, 'kills') + ' ' + app.isChanged(oldPlayer, player, 'deaths') + '">';
        if (player.deaths) {
          tbody += app.roundNr(player.kills / player.deaths * 100, 1);
        }
        tbody += '</td>';
        tbody += '<td class="' + app.isChanged(oldPlayer, player, 'kills') + ' ' + app.isChanged(oldPlayer, player, 'headshots') + '" data-order="';
        if (player.kills) {
          tbody += app.roundNr(player.headshots / player.kills * 100, 1);
        }
        tbody += '">';
        if (player.kills) {
          tbody += app.roundNr(player.headshots / player.kills * 100, 1);
        }
        tbody += ' %</td>';
        tbody += '<td class="' + app.isChanged(oldPlayer, player, 'points') + '">' + player.points + '</td></tr>';
      });
    });
    var table = '<table class="dataTable"><thead><tr><th class="sorting"><span>Name</span></th><th class="sorting"><span>Kills</span></th><th class="sorting"><span>Assists</span></th><th class="sorting"><span>Deaths</span></th><th class="sorting"><span>K/D Ratio</span></th><th class="sorting"><span>Headshot %</span></th><th class="sorting"><span>Score</span></th></tr></thead><tbody>' + tbody + '</tbody></table>';
    if (app.table != table) {
      app.table = table;
      $('.player_table_container').html(app.table);
      $('.player_table_container .changed').fadeOut(0).fadeIn(5000);
      app.initSortTable();
    }
  },
  printMessages: function (data) {
    $('.chat_container').empty();
    $.each(data.rounds, function (i, round) {
      $.each(round.events, function (i, event) {
        if (event.type == 'frag') {
          $('.chat_container').append('<div class="event"><span class="timestamp">' + moment(event.timestamp).format('HH:mm:ss') + ':</span> <span class="killer">Player ' + event.fragger + '</span> killed <span class="dead">Player ' + event.fragged + '</span></div>');
        }
      });
    });
  },
  printRoundScoreStatistics: function (data) {
    if (data.teams[0].score != app.scoreT) {
      app.scoreT = data.teams[0].score;
      $('.terrorists .value').html(data.teams[0].score)
    }
    if (data.teams[1].score != app.scoreCT) {
      app.scoreCT = data.teams[1].score;
      $('.counter_terrorists .value').html(data.teams[1].score)
    }
  },
  printRoundStatistics: function (data) {
    $('.round_statistics .terrorists, .round_statistics .counter_terrorists').empty();
    $.each(data.rounds, function (i, el) {
      // Check if terrorist win.
      if (el.winner == el.t) {
        $('.round_statistics .terrorists').append('<span class="bomb result"><img src="img/' + el.endStatus + '.png" /></span>');
      }
      else {
        $('.round_statistics .terrorists').append('<span class="result"></span>');
      }
      // Check if counter terrorist win.
      if (el.winner == el.ct) {
        $('.round_statistics .counter_terrorists').append('<span class="bomb result"><img src="img/' + el.endStatus + '.png" /></span>');
      }
      else {
        $('.round_statistics .counter_terrorists').append('<span class="result"></span>');
      }
    });
  },
  initSortTable: function () {
    var paging = false;
    var searching = false;
    // http://datatables.net/
    app.data_table = $('.dataTable').DataTable({
      language: {
        searchPlaceholder: 'Hae taulukosta',
        url: app.path + 'js/libs/English.json'
      },
      order:[[1, 'desc']],
      paging:paging,
      searching:searching
    });
  },
  initOwlCarousel: function () {
    $('.owl-carousel').owlCarousel({
      navigation : false,
      slideSpeed : 300,
      paginationSpeed : 400,
      singleItem:true
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
  initTimer: function () {
    var sec = $('.timer .seconds').text();
    app.timer = setInterval(function () {
      $('.timer .seconds').text(--sec);
      // if (sec == 0) {
        // clearInterval(app.timer);
      // } 
    }, 1000)
  },
  init: function () {
    app.timeOut = 0.1;
    app.data = {};
    app.path = '';
    // app.initBars();
    app.initOwlCarousel();
    app.getData();
    app.setDataInterval();
    app.initTimer();
  }
}

$(document).ready(function () {
  app.init();
});