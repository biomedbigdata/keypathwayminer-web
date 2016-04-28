<div id="status" role="complementary">
<h1>Setup review</h1>
    <table  class="withRowLine">
<g:if test="${currentSetup}">
    <g:if test="${currentSetup.parameters}">
        <tr>
            <td>Name</td>
            <td>${currentSetup.parameters.name}</td>
        </tr>
        <g:if test="${selectedGraph}">
            <tr>
                <td>Network:</td>
                <td>${selectedGraph}</td>
            </tr>
        </g:if>
        <g:if test="${currentSetup.withPerturbation}">
        <tr>
            <td>Perturbation</td>
            <td>

                [ Start: ${currentSetup.perturbation.startPercent}%,
                Step: ${currentSetup.perturbation.stepPercent}%,
                End: ${currentSetup.perturbation.maxPercent}%,
                Networks per step: ${currentSetup.perturbation.graphsPerStep} ]

            </td>
        </tr>
        </g:if>
        <tr>
            <td>Algorithm</td>
            <td> ${currentSetup.parameters.algorithm}</td>
        </tr>
        <tr>
            <td>Strategy</td>
            <td>${currentSetup.parameters.strategy}</td>
        </tr>
        <g:if test="${currentSetup.parameters.strategy.toString().trim() == 'INES'}">
        <tr>
            <td>Remove border exception nodes</td>
            <td>${(currentSetup.parameters.removeBENs) ? "Yes" : "No"}</td>
        </tr>
        </g:if>
        <tr>
            <td>Unmapped nodes:</td>
            <td> ${currentSetup.parameters.unmapped_nodes}</td>
        </tr>
        <tr>
            <td>Computed pathways:</td>
            <td>${currentSetup.parameters.computed_pathways}</td>
        </tr>
        <g:if test="${currentSetup.parameters.strategy.toString().trim() == 'INES'}">
        <tr>
            <td>Node exceptions (K):</td>
            <td>
                <g:if test="${k_values}">
                    <g:if test="${k_values.use_range}">
                        Start: ${k_values.val},
                        Step: ${k_values.val_step},
                        End: ${k_values.val_max}
                    </g:if>
                    <g:else>
                        Value: ${k_values.val}
                    </g:else>
                </g:if>
            </td>
        </tr>
        </g:if>
        <tr>
            <td>Case exceptions (L):</td>
            <td>
                <g:if test="${l_values}">
                    <g:each in="${l_values}" var="l_val">
                        <g:each in="${selectedDatasets}" var="dataset">
                            <g:if test="${dataset.id == Long.parseLong(l_val.datasetFileID)}">
                                ${dataset.name}:
                            </g:if>
                        </g:each>

                        [

                        <g:if test="${l_val.use_range}">
                            Start: ${l_val.val}%,
                            End: ${l_val.val_max}%,
                            Step: ${l_val.val_step}%
                        </g:if>
                        <g:else>
                            Value: ${l_val.val}%
                        </g:else>
                        ]
                        <br/>
                    </g:each>

                </g:if>
            </td>
        </tr>
        </g:if>

<g:if test="${selectedDatasets}">
    <tr>
        <td>Datasets  <g:if test="${currentSetup.linkType}">(Connector: ${currentSetup.linkType}) </g:if></td>
        <td>
            <g:each in="${selectedDatasets}" var="dataset">
                ${dataset.name}<br/>
            </g:each>
        </td>
    </tr>
</g:if>

</g:if>
<g:else>
    <ul>
        <li>No settings found.</li>
        <li>Must be an error.</li>
    </ul>
</g:else>
        </table>
</div>