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
    if (location.href.match('cseye')) {
      var url = 'http://cseye.dev/response.json';
    }
    else {
      var url = '/match/1';
    }
    $.ajax({
      url: url,
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
      // app.initTimer();
      app.getData();
    }, app.timeOut * 1000);
  },
  printDataWrapper: function (data) {
    app.printPlayerTable(data);
    app.printWeaponsTable(data);
    app.printRoundScoreStatistics(data);
    app.printRoundStatistics(data);
    app.data = data;
    app.printMessages(data);
  },
  isChanged: function (previous, current, attribute) {
    if (typeof previous !== 'undefined' && typeof previous[attribute] !== 'undefined' && typeof current !== 'undefined' && typeof current[attribute] !== 'undefined') {
      if (previous[attribute] !== current[attribute]) {
        return 'changed';
      }
    }
    return '';
  },
  printPlayerTable: function (data) {
    var tbody = '';
    _.each(data.teams, function (team, teamIndex) {
      _.each(team.players, function (player) {
        var oldPlayer;
        if(app.data) {
          oldPlayer = _.find(app.data.teams[teamIndex].players, function (oldPlayer) {
            return player.id === oldPlayer.id;
          });
        }
        tbody += '<tr class="' + team.side + '"><td>' + player.name + '</td>';
        tbody += '<td class="' + app.isChanged(oldPlayer, player, 'kills') + '">' + player.kills + '</td>';
        tbody += '<td class="' + app.isChanged(oldPlayer, player, 'assists') + '">' + player.assists + '</td>';
        tbody += '<td class="' + app.isChanged(oldPlayer, player, 'deaths') + '">' + player.deaths + '</td>';
        tbody += '<td class="' + app.isChanged(oldPlayer, player, 'kills') + ' ' + app.isChanged(oldPlayer, player, 'deaths') + '">';
        if (player.deaths) {
          tbody += app.roundNr(player.kills / player.deaths, 1);
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
    if (app.playerTable != table) {
      app.playerTable = table;
      $('.player_table_container').html(app.playerTable);
      app.initSortTable('.player_table_container');
      $('.player_table_container .changed').fadeOut(0).fadeIn(1000).removeClass('changed');
    }
  },
  printWeaponsTable: function (data) {
    var tbody = '';
    _.each(data.weapons, function (weapon) {
      tbody += '<tr><td>' + weapon.name + '</td>';
      tbody += '<td>' + weapon.kills + '</td>';
      tbody += '<td data-order="';
      if (weapon.kills) {
        tbody += app.roundNr(weapon.headshots / weapon.kills * 100, 1);
      }
      tbody += '">';
      if (weapon.kills) {
        tbody += app.roundNr(weapon.headshots / weapon.kills * 100, 1) / 1;
      }
      tbody += ' %</td>';
      tbody += '<td>' + weapon.bought + '</td>';
    });
    var table = '<table class="dataTable"><thead><tr><th class="sorting"><span>Name</span></th><th class="sorting"><span>Kills</span></th><th class="sorting"><span>Headshot %</span></th><th class="sorting"><span>Bought</span></th></tr></thead><tbody>' + tbody + '</tbody></table>';
    if (app.weaponsTable != table) {
      app.weaponsTable = table;
      $('.weapon_table_container').html(app.weaponsTable).fadeOut(0).fadeIn(1000);
      app.initSortTable('.weapon_table_container');
    }
  },
  getUserData: function (user_id) {
    var player_info = {
      'name':'',
      'side':''
    }
    $.each(app.data.teams, function (i, team) {
      var side = false;
      $.each(team.players, function (i, player) {
        if (player.id == user_id) {
          side = true;
          player_info.name = player.name;
        }
      });
      if (side == true) {
        player_info.side = team.side;

      }
    });
    return player_info;
  },
  printMessages: function (data) {
    var chat = '';
    $.each(data.events, function (i, event) {
        if (event.type == 'frag') {
          var fragger = app.getUserData(event.fragger);
          var fragged = app.getUserData(event.fragged);
          chat += '<div class="event"><span class="timestamp">' + moment(event.time, 'd/m/YYYY - HH:mm:ss').format('HH:mm:ss') + ':</span> <span class="' + fragger.side + '">' + fragger.name + '</span> killed <span class="' + fragged.side + '">' + fragged.name + '</span></div>';
        }
        else if (event.type == 'message') {
          var player = app.getUserData(event.id);
          chat += '<div class="event"><span class="timestamp">' + moment(event.time, 'd/m/YYYY - HH:mm:ss').format('HH:mm:ss') + ':</span> <span class="' + player.side + '">' + player.name + '</span> said ' + event.text + '</div>';
        }
        else if (event.type == 'bomb') {
          var player = app.getUserData(event.id);
          chat += '<div class="event"><span class="timestamp">' + moment(event.time, 'd/m/YYYY - HH:mm:ss').format('HH:mm:ss') + ':</span> <span class="' + player.side + '">' + player.name + '</span> ' + event.text.toLowerCase().replace(/_/g, ' ') + '</div>';
        }
    });
    if (app.chatData != chat) {
      app.chatData = chat;
      $('.chat_container').html(app.chatData);
    }
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
    var roundStatsT = '';
    var roundStatsCT = '';
    var datafound = false;
    $.each(data.rounds, function (i, el) {
      if (el.endStatus != null) {
        datafound = true;
        // Check if terrorist win.
        if (el.endStatus == 'terrorists_win') {
          roundStatsT += '<span class="bomb result"><img src="img/' + el.endStatus + '.png" /></span>';
        }
        else {
          roundStatsT += '<span class="result"></span>';
        }
        // Check if counter terrorist win.
        if (el.endStatus == 'cts_win') {
          roundStatsCT += '<span class="bomb result"><img src="img/' + el.endStatus + '.png" /></span>';
        }
        else {
          roundStatsCT += '<span class="result"></span>';
        }
      }
    });
    if (datafound === false) {
      roundStatsT += '<span class="result"></span>';
      roundStatsCT += '<span class="result"></span>';
    }
    if (roundStatsT != app.roundStatsT) {
      app.roundStatsT = roundStatsT;
      $('.round_statistics .terrorists').html(app.roundStatsT);
    }
    if (roundStatsCT != app.roundStatsCT) {
      app.roundStatsCT = roundStatsCT;
      $('.round_statistics .counter_terrorists').html(app.roundStatsCT);
    }
  },
  initSortTable: function (table) {
    var paging = false;
    var searching = false;
    // http://datatables.net/
    $(table + ' table').DataTable({
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
      navigation: false,
      paginationSpeed: 400,
      singleItem: true,
      autoPlay: 3000,
      slideSpeed: 300
    });
  },
  // initBars: function () {
  //   $.fn.peity.defaults.bar = {
  //     delimiter: ",",
  //     fill: ["#c0beb8", '#c6a71f'],
  //     height: 200,
  //     max: null,
  //     min: 0,
  //     padding: 0,
  //     width: $('.bar_wrapper').width()
  //   }
  //   $('.values_bar').width($('.bar_wrapper').width())
  //   $('span.bar').peity('bar')
  // },
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
    app.data;
    app.path = '';
    // app.initBars();
    app.initOwlCarousel();
    app.getData();
    app.setDataInterval();
    // app.initTimer();
  }
}

$(document).ready(function () {
  app.init();
});