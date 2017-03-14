var notifiSocket;
var notifiSkipNext;
function showMenu() {
	$('body').append(
		'<div class="menu">'
		+ '<div class="menuRow">'
		+ '<div class="menuCellLeft"><a id="commandNew" href="javascript:void(0)"><i class="fa fa-plus" style="font-size: 1.5em;"></i></a></div>'
		+ '<div class="menuCellLeft"><a id="commandReload" href="javascript:void(0)"><i class="fa fa-refresh" style="font-size: 1.3em;"></i></a></div>'
		+ '<div class="menuCellCenter"><input class="menuSearchText" type="text"></div>'
		+ '<div class="menuCellCenter2"><a id="commandSearch" href="javascript:void(0)"><i class="fa fa-search" style="font-size: 1.2em;"></i></a></div>'
		+ '<div class="menuCellRight"><a id="commandSignOut" href="javascript:void(0)"><i class="fa fa-sign-out" style="font-size: 1.6em;"></i></a></div>'
		+ '</div>'
		+ '</div>'
		+ '<div id="menuFooter" class="footer"></div>');
	$('#commandReload').click(onReload);
	$('#commandNew').click(onNew);
	$('#commandSignOut').click(onSignOut);
	$('.menuSearchText').keypress(function (event) {
		if (event.which == 13) {
			onSearch();
		}
	});
	$('#commandSearch').click(onSearch);
	notifiSkipNext = false;
	notifiSocket = new WebSocket('ws://localhost:8080/mango/websocket');
	notifiSocket.onmessage = function() {
		if (notifiSkipNext) {
			notifiSkipNext = false;
		} else {
			$('#commandReload').addClass('menuNeedReload');
		}
	}
}
function hideMenu() {
	$('.menu, #menuFooter').remove();
	notifiSocket.close();
}
function onReload() {
	$('#commandReload').removeClass('menuNeedReload');
	hideNewNote();
	hideNotes();
	showNotes();
}
function showNewNote() {
	$('#menuFooter').after(
		'<div class="newNote">'
		+ '<textarea class="newNoteText" rows="10"></textarea>'
		+ '<a id="commandPost" href="javascript:void(0)">Post</a>'
		+ '<div class="newNoteCounter"></div>'
		+ '</div>'
		+ '<div id="newNoteFooter" class="footer"></div>');
	$('#commandPost').click(onPost);
	$('.newNoteText').focus();
	$('.newNoteText').bind('input propertychange', function() {
		$('.newNoteCounter').html(
			' '
			+ this.value.length
			+ ' / 255');
		if (this.value.length > 255) {
			$('#commandPost').addClass('newNoteDisabled');
			$('.newNoteCounter').addClass('newNoteFailure');
		} else {
			$('#commandPost').removeClass('newNoteDisabled');
			$('.newNoteCounter').removeClass('newNoteFailure');
		}
	});
}
function hideNewNote() {
	$('.newNote, #newNoteFooter').remove();
}
function onNew() {
	if ($('.newNote').length == 0) {
		showNewNote();
	} else {
		hideNewNote();
	}
}
function onPost() {
	if($('.newNoteText').val().length > 255)
		return;

	notifiSkipNext = true;
	$.post('rest/notes', JSON.stringify({ note: $('.newNoteText').val() }), function() {
		$('#commandReload').removeClass('menuNeedReload');
		hideNewNote();
		hideNotes();
		showNotes();
	});
}
function onSearch() {
	searchText = $('.menuSearchText').val().replace(/#/g, '').replace(/ /g, '+');
	if (searchText) {
		$.get('rest/searchnotes/' + searchText, function (response) {
			hideNotes();
			showNotesImpl(response);
		});
	} else {
		$.get('rest/notes/', function (response) {
			hideNotes();
			showNotesImpl(response);
		});
	}
}
function getLocalDate() {
	d = new Date();
	s = d.getFullYear();
	s += '-';
	if (d.getMonth() + 1 < 10) {
		s += '0';
	}
	s += d.getMonth() + 1;
	s += '-';
	if (d.getDate() < 10) {
		s += '0';
	}
	s += d.getDate();
	return s;
}
function getTimeOrDate(date, currentDate) {
	if (date.substring(0, 10) === currentDate) {
		date = date.substring(11, 16);
	} else {
		date = date.substring(11, 16)
			+ ' on '
			+ date.substring(0, 10);
	}
	return date;
}
function showNotesImpl(notes) {
	notes.sort(function(a, b) {
		return a.sortOrder - b.sortOrder;
	});
	currentDate = getLocalDate();
	for (var i = 0; i < notes.length; i++) {
		if (notes[i].completed) {
			stripeClass = '';
			headerClass = 'noteHeader noteHeaderRound';
			bodyClass = 'noteBody noteBodyRound';
		} else {
			if (notes[i].note.indexOf('#highpriority') != -1) {
				stripeClass = 'noteStripe noteRed';
			} else if (notes[i].note.indexOf('#lowpriority') != -1) {
				stripeClass = 'noteStripe noteGreen';
			} else {
				stripeClass = 'noteStripe noteBlue';
			}
			headerClass = 'noteHeader';
			bodyClass = 'noteBody';
		}
		if (notes[i].completed) {
			header =
				'Posted by '
				+ notes[i].user.username
				+ ' at '
				+ getTimeOrDate(notes[i].dateCreated, currentDate)
				+ '<span class="noteHeaderCompleted">Completed by '
				+ notes[i].userCompleted.username
				+ ' at '
				+ getTimeOrDate(notes[i].dateCompleted, currentDate)
				+ '</span>';
		} else {
			header =
				notes[i].user.username
				+ ' at '
				+ getTimeOrDate(notes[i].dateCreated, currentDate);
		}
		var text = notes[i].note;
		var text2 = '';
		var k = 0;
		var beg = -1;
		var end = 0;
		while (true) {
			if (k == text.length) {
				if (beg == -1) {
					text2 += text.substring(end);
				} else {
					text2 +=
						'<span class="tag">'
						+ text.substring(beg)
						+ '</span>';
				}
				break;
			}
			if (text[k] == '#') {
				text2 += text.substring(end, k);
				beg = k;
			}
			if (text[k] == ' ' && beg != -1) {
				text2 +=
					'<span class="tag">'
					+ text.substring(beg, k)
					+ '</span>';
					beg = -1;
					end = k;
			}
			k++;
		}
		var note =
			'<div class="note" data-noteId="'
			+ notes[i].noteId
			+ '">'
			+ '<div class="noteRow">';
		if (stripeClass) {
			note +=
			'<div class="'
			+ stripeClass
			+ '">'
			+ '</div>';
		}
		note +=
			'<div class="'
			+ headerClass
			+ '">'
			+ header
			+ '</div>'
			+ '</div>'
			+ '<div class="noteRow">';
		if (stripeClass) {
			note +=
			'<div class="noteEmpty">'
			+ '</div>';
		}
		note +=
			'<div class="'
			+ bodyClass
			+ '">'
			+ text2
			+ '</div>'
			+ '</div>'
			+ '</div>'
			+ '<div class="footer">'
			+ '</div>';
		$('body').append(note);
	}
	$('.noteRed, .noteBlue, .noteGreen').click(onComplete);
	$('.tag').click(onTag);
}
function showNotes() {
	$.get('rest/notes', function (response) {
		showNotesImpl(response);
	});
}
function hideNotes() {
	$('.note, .noteHeader, .noteBody').remove();
	$('.footer').not('#menuFooter, #newNoteFooter').remove();
}
function onComplete() {
	$.ajax({
		type: 'PUT',
		url: 'rest/notes/complete/' + $(this).parent().parent().attr('data-noteId'),
		success: function() {
			hideNotes();
			showNotes();
		}
	});
}
function onTag() {
	v = $('.menuSearchText').val();
	if (v) {
		v += ' ';
	}
	v += $(this).text();
	$('.menuSearchText').val(v);
}
function moveTabToFront(a, b) {
	a.css('background-color', '#ffffff');
	a.append('<span>' + a.children().remove().text() + '</span>');
	$('#username, #password').val('');
	$('#username').focus();
	$('#password').unbind('keypress').keypress(function (event) {
		if (event.which == 13) {
			b();
		}
	});
	$('.signSubmit').unbind('click').click(b);
	$('.signLoading, .signSuccess, .signFailure').remove();
}
function moveTabToBack(a, b) {
	a.css('background-color', '#e0f0ff');
	a.append('<a href="javascript:void(0)">' + a.children().remove().text() + '</a>');
	a.children().click(b);
}
function onSignLeftTab() {
	moveTabToFront($('.signLeftTab'), onSignIn);
	moveTabToBack($('.signRightTab'), onSignRightTab);
}
function onSignRightTab() {
	moveTabToFront($('.signRightTab'), onSignUp);
	moveTabToBack($('.signLeftTab'), onSignLeftTab);
}
function showSign() {
	$('body').append(
		'<div class="sign">'
		+ '<div class="signHeader">'
		+ '<span class="signLeftTab">'
		+ '<span>Sign in</span>'
		+ '</span>'
		+ '<span class="signRightTab">'
		+ '<span>Sign up</span>'
		+ '</span>'
		+ '</div>'
		+ '<div class="signBody">'
		+ '<div class="signItem">'
		+ '<input id="username" class="signInput" type="text" placeholder="Username">'
		+ '</div>'
		+ '<div class="signItem">'
		+ '<input id="password" class="signInput" type="password" placeholder="Password">'
		+ '</div>'
		+ '<div class="signItem">'
		+ '<a class="signSubmit" href="javascript:void(0)">Submit</a>'
		+ '</div>'
		+ '</div>'
		+ '</div>');
	moveTabToFront($('.signLeftTab'), onSignIn);
	moveTabToBack($('.signRightTab'), onSignRightTab);
}
function hideSign() {
	$('.sign').remove();
}
function onSignIn() {
	$('.signSubmit').unbind('click');
	$('.signLoading, signSuccess, .signFailure').remove();
	$('.signBody').append('<div class="signLoading">Signing in...</div>');
	$.ajax({
		type: 'PUT',
		url: 'rest/sessions',
		data: JSON.stringify({ username: $('#username').val(), password: $('#password').val() }),
		success: function(response) {
			if (response.status) {
				hideSign();
				showMenu();
				showNotes();
			} else {
				$('.signSubmit').click(onSignIn);
				$('.signLoading').remove();
				$('.signBody').append('<div class="signFailure">Incorrect username or password!</div>');
			}
		},
		error: function(xhr, textStatus, errorThrown) {
			$('.signSubmit').click(onSignIn);
			$('.signLoading').remove();
			$('.signBody').append('<div class="signFailure">' + errorThrown + '</div>');
		}
	});
}
function onSignOut() {
	$.ajax({
		type: 'DELETE',
		url: 'rest/sessions',
		async: false
	});
	hideMenu();
	hideNewNote();
	hideNotes();
	showSign();
}
function onSignUp() {
	$('.signSubmit').unbind('click');
	$('.signLoading, signSuccess, .signFailure').remove();
	$('.signBody').append('<div class="signLoading">Signing up...</div>');
	$.post('rest/users', JSON.stringify({ username: $('#username').val(), password: $('#password').val() }), function(response) {
		if (response.status) {
			$('.signLoading').remove();
			$('.signBody').append('<div class="signSuccess">You have been signed up!</div>');
		} else {
			$('.signSubmit').click(onSignUp);
			$('.signLoading').remove();
			$('.signBody').append('<div class="signFailure">' + response.message + '</div>');
		}
	});
}
function isSignedIn() {
	var a = false;
	$.ajax({
		type: 'GET',
		url: 'rest/sessions',
		async: false,
		success: function (response) {
			a = response.status;
		}
	});
	return a;
}
$(function () {
	$.ajaxSetup({ contentType: 'application/json' });
	if (isSignedIn()) {
		showMenu();
		showNotes();
	} else {
		showSign();
	}
});
