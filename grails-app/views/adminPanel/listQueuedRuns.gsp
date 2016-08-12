<html>

<head>
    <meta name='layout' content='main'/>
    <g:set var="entityName" value="${message(code: 'role.label', default: 'Role')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <r:script>
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

        var questsFinished = 0;
        var questsCancelled = 0;
        var questsQueued = 0;

        function getQueuedQuestAmount(){
            $.post("<g:createLink controller="quests" action="jsonQuestsInQueue" />", function( questCount ) {
                console.log( "Waiting quests: " + questCount );
                questsQueued = parseInt(questCount);
            });
        }

        function getCancelledQuestAmount(){
            $.post("<g:createLink controller="quests" action="jsonQuestsCancelled" />", function( questCount ) {
                questsCancelled = parseInt(questCount);
            });
        }

        function getFinishedQuestAmount(){
            $.post("<g:createLink controller="quests" action="jsonQuestsFinished" />", function( questCount ) {
                questsFinished = parseInt(questCount);
            });
        }

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Jobs', 'Number'],
          ['Completed', questsFinished],
          ['Queued', questsQueued],
          ['Cancelled',  questsCancelled]
        ]);

        var options = {
          title: 'Job overview'
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart'));

        chart.draw(data, options);
      }
        $(document).ready(function () {
            // get the quests.
            getQueuedQuestAmount()

            // Set timer for updating quests.
            ticker = setInterval(function () {
                console.log('Fetching new quests count at: ' + new Date());
                getQueuedQuestAmount();
                getFinishedQuestAmount();
                getCancelledQuestAmount();
                drawChart();
            }, 1000);

            $('#killAllButton').click(function () {
                $.post("<g:createLink controller="quests" action="jsonKillAllQuests" params="[password: killAllQuestsPassword]" />", function( succeeded ) {
                    if(succeeded == "true"){
                        alert("Success: all active runs are deleted.");
                    }else{
                        alert("Something went wrong.");
                    }

                });
            });
        });

    </r:script>
</head>

<body>
            <div id="piechart" style="width: 900px; height: 500px;"></div>

            <div id="killAllButton" class="red-round-button">
                <div class="red-round-button-inner">KILL ALL</div>
            </div>

            <g:include controller="quests" action="queuedQuests"/>

</body>
</html>