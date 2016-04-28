%{--
    VIEW FOR RENDERING QUESTS (TASKS) ATTACHED TO THE CURRENT USER
--}%

<script>
    var attachedToID = "${attachedToID}";
    var ticker;

    $(document).ready(function () {
        if(attachedToID == ""){
            alert("No attached to ID was found.");
            return;
        }
        // get the quests.
        getQuests();

        // Set timer for updating quests.
        ticker = setInterval(function () {
            console.log('Fetching new quests at: ' + new Date());
            getQuests();
        }, 1000);

    });

    function getQuests(){
        $.post("<g:createLink controller="rest" action="jsonQuests" params="[attachedToID: attachedToID]" />", function( quests ) {
            if( quests == null || quests.length == 0){
                console.log( "No quests loaded for attachedToID: " + attachedToID );
                return;
            }

            console.log( "list of quest Loaded: " );
            $.each(quests, function( i, quest ) {
                if(quest == null || quests.length == 0){
                    console.log( "quest was null.");
                    return;
                }
                console.log( "quest Loaded: " + quest.id + ", progress: " + quest.progress);
                updateQuest(quest);
            });

        });
    }

    function reviewSettings(runParamsID){
        $.post("<g:createLink controller="runKPM" action="settingBoxByRunID" absolute="true"/>?runParamsID="+runParamsID, function( data ) {
            if( data == null || data.length == 0){
                console.log( "No quests loaded for attachedToID: " + attachedToID );
                return;
            }

            $("#reviewSettingsDialog").empty();
            $("#reviewSettingsDialog").html(data);
            $("#reviewSettingsDialog").dialog({
                modal: true,
                resizable: false,
                width:'auto',
                height:'auto',
                buttons: {
                    Ok: function () {
                        $(this).dialog("close");
                    }
                }
            });
            $(".ui-dialog-titlebar").hide();

        });
    }

    function cancelRun(runID){
//        alert(runID);
        $.post("<g:createLink absolute="true" controller="runKPM" action="cancelRun"/>?runID="+runID);
    }

    function updateQuest(quest){
        if(!$( "#quest_"+quest.id ).length){
            $( "#questList").append(
                    ' <li id="quest_'+quest.id+'" style="display:block;margin-top:50px;">' +
                            '<div class="questName"><b>'+quest.name+'</b>&nbsp;-&nbsp;</div>' +
                            ' <div class="questTitle" id="statusTitle_'+quest.id+'">'+quest.title+'</div> ' +
                        '<div class="questStatus" id="statusmsg_'+quest.id+'"></div>' +
                        '<div class="questProgressBar" id="progressbar_'+quest.id+'">' +
                            '<div class="progressText" id="progressText_'+quest.id+'"></div>' +
                            ' <input type="button" id="progressCancel_'+quest.id+'" class="minusButton" style="float:right;margin-top:3px;" onclick="cancelRun(\''+quest.runID+'\');"/>' +
                        '</div>' +
                        '<a href="#" onclick="reviewSettings(\''+quest.runParamsID+'\');" class="button">Setup review</a>'+
                    '</li>');
        }

        $('#statusTitle_'+quest.id).text(quest.title);


        $( "#statusmsg_"+quest.id ).text(quest.statusMessage);

        if(quest.isCancelled == true){
            $( "#statusmsg_"+quest.id ).text("(Cancelled)");
        }

        if(quest.isCompleted == true || quest.isCancelled == true){
            $('#statusTitle_'+quest.id).text("");
            $( "#progressbar_"+quest.id ).progressbar({
                value: 100
            });

            $('#progressCancel_'+quest.id).hide();

            if($('#quest_'+quest.id).has("#resultsLink_"+quest.id).length == 0){
                $("#quest_"+quest.id).append(
                        '<a class="button" style="float:right;margin-top:5px;" id="resultsLink_'+quest.id+'" href="<g:createLink controller="results" action="seeResults" absolute="true"/>?runID='+quest.runID+'">See results.</a>'
                );

                $("#resultsLink_"+quest.id).button();
            }

            $( "#progressText_"+quest.id ).text("100%");

            return;
        }

        $( "#progressbar_"+quest.id ).progressbar({
            value: quest.progress*100
        });

        $( "#progressText_"+quest.id ).text((quest.progress*100) + "%");
    }

</script>
%{--<a class="button" id="bookmarkButton">+ Bookmark these results</a>--}%
<ul id="questList"></ul>
<div id="reviewSettingsDialog"></div>