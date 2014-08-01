function main() {
	// Request JSON object
	if (!json.has("wfparams")) {
		status.setCode(status.STATUS_BAD_REQUEST,
				"Missing request JSON object property: wfparams.");
		return;
	}

	// Workflow Definition ID
	var workflowDefinitionId = url.templateArgs.workflowDefinitionId;
	if (workflowDefinitionId === undefined || workflowDefinitionId == null) {
		status.setCode(status.STATUS_BAD_REQUEST,
				"workflowDefinitionId missing when starting a new workflow.");
		return;
	}

	// TODO Quick-fix, workaround to handle $ being removed from workflowDefinitionId.
	// Check workflowDefinitionId contains $ and add if not.
	var separatorIndex = workflowDefinitionId.indexOf('$');
	if (separatorIndex == -1) {
		if (workflowDefinitionId.indexOf('jbpm') == 0) {
			workflowDefinitionId = 'jbpm$' + workflowDefinitionId.substring(4);
		} else {
			status
					.setCode(status.STATUS_BAD_REQUEST,
							"workflowDefinitionId missing when starting a new workflow.");
			return;
		}
	}

	// Check workflowDefinitionId is valid
	var definition = workflow.getDefinition(workflowDefinitionId);
	if (definition === null) {
		status.setCode(status.STATUS_BAD_REQUEST,
				"Invalid workflowDefinitionId when starting a new workflow.");
		return;
	}

	logger.log("Starting workflow instance with parameters:\n" + json.wfparams);

	var wfpackage = workflow.createPackage();

	var wfparamsJson = json.getJSONObject("wfparams");
	var wfparams = jsonUtils.toObject(wfparamsJson.toString());
	//TODO find a way to receive the assignee from the calling client too.
	wfparams["bpm:assignee"] = person;

	var workflowPath = definition.startWorkflow(wfpackage, wfparams);

	model.workflowDefinitionId = workflowDefinitionId;
	model.workflowPath = workflowDefinitionId;
}

main();