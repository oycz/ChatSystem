
/*
    iziToast.show({
	class : '',
	title : '',
	message : '',
	color : '', // blue, red, green, yellow
	icon : '',
	iconText : '',
	iconColor : '',
	image : '',
	imageWidth : 50,
	layout : 1,
	balloon : false,
	close : true,
	rtl : false,
	position : 'bottomRight', // bottomRight, bottomLeft, topRight, topLeft, topCenter, bottomCenter, center
	target : '',
	timeout : 5000,
	pauseOnHover : true,
	resetOnHover : false,
	progressBar : true,
	progressBarColor : '',
	animateInside : true,
	buttons : {},
	transitionIn : 'fadeInUp',
	transitionOut : 'fadeOut',
	transitionInMobile : 'fadeInUp',
	transitionOutMobile : 'fadeOutDown',
	onOpen : function() {},
	onClose : function() {}
});

iziToast.settings({
	timeout : 10000,
	resetOnHover : true,
	icon : 'material-icons',
	transitionIn : 'flipInX',
	transitionOut : 'flipOutX',
	onOpen : function() {
		console.log('callback abriu!');
	},
	onClose : function() {
		console.log("callback fechou!");
	}
});

iziToast.show({
	color : 'dark',
	icon : 'icon-person',
	title : 'Hey',
	message : 'Welcome!',
	position : 'center', // bottomRight, bottomLeft, topRight, topLeft, topCenter, bottomCenter
	progressBarColor : 'rgb(0, 255, 184)',
	buttons : [
		[ '<button>Ok</button>', function(instance, toast) {
			alert("Hello world!");
		} ],
		[ '<button>Close</button>', function(instance, toast) {
			instance.hide({
				transitionOut : 'fadeOutUp'
			}, toast);
		} ]
	]
});


iziToast.info({
	title : 'Hello',
	message : 'Welcome!',
});
*/

$(".trigger-info").on('click', function(event) {
	event.preventDefault();

	iziToast.info({
		title : 'Hello',
		message : 'Welcome!',
		// imageWidth: 70,

		position : 'bottomLeft',
		transitionIn : 'bounceInRight',
		// rtl: true,
		// iconText: 'star',
		onOpen : function() {
			console.log('callback abriu! info');
		},
		onClose : function() {
			console.log("callback fechou! info");
		}
	});
});