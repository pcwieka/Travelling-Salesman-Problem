$(document).ready(function() {

    var pointSize = 4;

    $("#canvas").click(function (e) {
        getPosition(e);
    });

    var clickCounter = 0;
    var pointsCoordinates = [];
    var calculationResponse = [];
    var isDuplicate = false;

    function getPosition(event) {
        var rect = canvas.getBoundingClientRect();
        var x = event.clientX - rect.left;
        var y = event.clientY - rect.top;

        isDuplicate = false;

        for(var i = 0;i < pointsCoordinates.length; i++){

            if(pointsCoordinates[i].x == x && pointsCoordinates[i].y == y ){

                isDuplicate = true;

            }

        }

        if(!isDuplicate) {

            pointsCoordinates.push({city: clickCounter, x: x, y: y});

            console.log("Click: " + clickCounter + ", x: " + x + ", y: " + y);

            var c = document.getElementById("canvas");
            var ctx = c.getContext("2d");

            ctx.beginPath();
            ctx.moveTo(x, y);

            for (var i = 0; i < pointsCoordinates.length; i++) {

                ctx.lineTo(pointsCoordinates[i].x, pointsCoordinates[i].y);
                ctx.lineWidth = 0.04;
                ctx.moveTo(x, y);

            }

            ctx.stroke();

            clickCounter++;

            drawCoordinates(x, y);

        }
    }

    function drawCoordinates(x, y) {
        var ctx = document.getElementById("canvas").getContext("2d");

        ctx.fillStyle = "#333333"; // Grey color

        ctx.beginPath();
        ctx.arc(x, y, pointSize, 0, Math.PI * 2, true);
        ctx.fill();
    }

    function createJSON(){

        var citiesPositionsJson = JSON.stringify(pointsCoordinates);

        citiesPositionsJson = "\"cities\": " + citiesPositionsJson;

        var alpha = getSliderValue("alphaSlider");
        var beta = getSliderValue("betaSlider");
        var evaporation = getSliderValue("evaporationSlider");
        var q = getSliderValue("qSlider");
        var antFactor = getSliderValue("antFactorSlider");
        var randomFactor = getSliderValue("randomFactorSlider");
        var iterations = getSliderValue("iterationsSlider");
        var attempts = getSliderValue("attemptsSlider");

        var json = " {" +
            "\"alpha\":" + alpha +","+
            "\"beta\":" + beta +","+
            "\"evaporation\":" + evaporation +","+
            "\"q\":" + q +","+
            "\"antFactor\":" + antFactor +","+
            "\"randomFactor\":" + randomFactor +","+
            "\"attempts\":" + attempts +","+
            "\"iterations\":" + iterations +","+
            citiesPositionsJson+
            "}";

        return json;

    }

    $.ajaxSetup({
        beforeSend:function(){

            $.blockUI.defaults.css = {padding:	0,
                margin:		0,
                width:		'30%',
                top:		'40%',
                left:		'35%',
                textAlign:	'center'};
            $.blockUI({ message: '<img src="loader.gif" />' });

        },
        complete:function(){

            $.unblockUI();

        }
    });

    $("#processButton").click(function () {

        console.log("POST JSON: " + createJSON());

        $.ajax
        ({

            type: "POST",
            url: 'api/calculatePath/',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            async: true,
            data: createJSON(),
            success: function (response) {

                calculationResponse = response;

                drawOptimalPath(calculationResponse);

            },

            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert("Wystąpił problem po stronie serwera.");
            }

        });

        }
);

    var drawOptimalPath = function(calculationResponse){

        var c=document.getElementById("canvas");
        var ctx=c.getContext("2d");
        ctx.beginPath();
        ctx.moveTo(pointsCoordinates[calculationResponse[0]].x, pointsCoordinates[calculationResponse[0]].y);

        for(var i = 1; i < calculationResponse.length-1; i++){
            ctx.lineTo(pointsCoordinates[calculationResponse[i]].x,pointsCoordinates[calculationResponse[i]].y);
            //ctx.fillText(calculationResponse[i].toString(),pointsCoordinates[calculationResponse[i]].x+3,pointsCoordinates[calculationResponse[i]].y-3);
            ctx.lineWidth = 3.3;
            ctx.strokeStyle = "#2aba7c"

        }

        ctx.lineTo(pointsCoordinates[calculationResponse[0]].x, pointsCoordinates[calculationResponse[0]].y);

        ctx.stroke();

        $('#trailLength').text("Długość ścieżki: " + calculationResponse[calculationResponse.length-1]);

    };


    var getSliderValue = function(sliderId){

        var slider = document.getElementById(sliderId);

        return slider.value;

    };


    var defineSliderBehavior = function(sliderId, spanId){

        var slider = document.getElementById(sliderId);
        var output = document.getElementById(spanId);
        output.innerHTML = slider.value;

        slider.oninput = function() {
            output.innerHTML = this.value;
        }

    };

    defineSliderBehavior("alphaSlider", "alpha");
    defineSliderBehavior("betaSlider", "beta");
    defineSliderBehavior("evaporationSlider", "evaporation");
    defineSliderBehavior("qSlider", "q");
    defineSliderBehavior("antFactorSlider", "antFactor");
    defineSliderBehavior("randomFactorSlider", "randomFactor");
    defineSliderBehavior("iterationsSlider","iterations");
    defineSliderBehavior("attemptsSlider","attempts");

    $("#clearAllButton").click(function () {

        var canvas = document.getElementById('canvas');
        canvas.width = canvas.width;

        clickCounter = 0;
        pointsCoordinates = [];
        calculationResponse = [];
        $('#trailLength').text("");

    });

    $("#clearTrailsButton").click(function () {

        var canvas = document.getElementById('canvas');
        canvas.width = canvas.width;

        calculationResponse = [];
        $('#trailLength').text("");

        var c=document.getElementById("canvas");
        var ctx=c.getContext("2d");

        for(var j = 0; j < pointsCoordinates.length; j++) {

            ctx.beginPath();
            ctx.moveTo(pointsCoordinates[j].x, pointsCoordinates[j].y);

            for (var i = 0; i < pointsCoordinates.length; i++) {

                ctx.lineTo(pointsCoordinates[i].x, pointsCoordinates[i].y);
                ctx.lineWidth = 0.04;
                ctx.moveTo(pointsCoordinates[j].x, pointsCoordinates[j].y);

            }

            ctx.stroke();

            drawCoordinates(pointsCoordinates[j].x, pointsCoordinates[j].y);
        }

    });


});

