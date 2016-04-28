<html>

<head>
    <meta name='layout' content='main'/>
    <g:set var="entityName" value="${message(code: 'role.label', default: 'Role')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
    <r:script>
        $(document).ready(function () {
            // get the quests.
            getQuestAmount();

            // Set timer for updating quests.
            ticker = setInterval(function () {
                console.log('Fetching new quests count at: ' + new Date());
                getQuestAmount();
            }, 1000);

            $('#killAllButton').click(function () {
                $.post("<g:createLink controller="quests" action="jsonKillAllQuests" params="[password: killAllQuestsPassword]" />", function( succeeded ) {
                    if(succeeded == "true"){
                        alert("Success: not yet finished runs are being deleted.");
                    }else{
                        alert("Something went wrong.");
                    }

                });
            });
        });
        function getQuestAmount(){
            $.post("<g:createLink controller="quests" action="jsonQuestsInQueue" />", function( questCount ) {
                console.log( "Waiting quests: " + questCount );
                $("#questCount").html("# queued or running: " + questCount);
            });
        }
    </r:script>
</head>

<body>
            <div id="questCount" style="font-size:16px;padding:15px;"> </div>

            <div id="killAllButton" class="red-round-button">
                <div class="red-round-button-inner">KILL ALL</div>
            </div>
</body>
</html>