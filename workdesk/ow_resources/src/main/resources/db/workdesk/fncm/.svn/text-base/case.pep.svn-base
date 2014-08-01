<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE WorkFlowDefinition SYSTEM "wfdef4.dtd">
<WorkFlowDefinition ApiVersion="4.0"
Origin="JavaAPI"
	Subject="&quot;case&quot;"
	Name="case1"
	MainAttachment="&quot;A1&quot;"
	AuthorTool="Process Designer"
	versionAgnostic="false"
	validateUsingSchema="true">
	<Field
		Name="A1"
		ValueExpr="&quot;||0|0||&quot;"
		Type="attachment"
		IsArray="false"
		MergeType="override"/>
	<Map
		Name="Workflow"
		MaxStepId="9" >
		<Step
			StepId="0"
			Name="LaunchStep"
			XCoordinate="50"
			YCoordinate="50"
			RequestedInterface="Approval Launch HTML (FileNet)"
			JoinType="none"
			SplitType="or"
			CanReassign="true"
			CanViewStatus="true"
			CanViewHistory="false"
			IgnoreInvalidUsers="false">
<Parameter
	Name="A1"
	ValueExpr="A1"
	Type="attachment"
	IsArray="false"
	Mode="inout"/>
			<Route
				SourceStepId="0"
				DestinationStepId="8"/>
<ModelAttributes>
	<ModelAttribute
		Name="UI_StepType"
		Type="int"
		IsArray="false">
			<Value Val="1"/>
	</ModelAttribute>
</ModelAttributes>
		</Step>
		<Step
			StepId="3"
			Name="General"
			XCoordinate="161"
			YCoordinate="191"
			RequestedInterface="Approval HTML (FileNet)"
			QueueName="Inbox"
			JoinType="or"
			SplitType="or"
			CanReassign="true"
			CanViewStatus="true"
			CanViewHistory="false"
			IgnoreInvalidUsers="false">
			<Participant Val="F_Originator" />
<ModelAttributes>
	<ModelAttribute
		Name="UI_StepType"
		Type="int"
		IsArray="false">
			<Value Val="2"/>
	</ModelAttribute>
</ModelAttributes>
		</Step>
		<CompoundStep
			StepId="8"
			Name="Component"
			XCoordinate="172"
			YCoordinate="80"
			JoinType="or"
			SplitType="or">
			<Instruction
				Id="9"
				Action="execute">
				<Expression Val="CE_Operations" />
				<Expression Val="setStringProperty" />
				<Expression Val="A1" />
				<Expression Val="&quot;CASEREF&quot;" />
				<Expression Val="F_WobNum+&quot;,&quot;+F_Subject" />
			</Instruction>
			<Route
				SourceStepId="8"
				DestinationStepId="3"/>
<ModelAttributes>
	<ModelAttribute
		Name="UI_StepType"
		Type="int"
		IsArray="false">
			<Value Val="8"/>
	</ModelAttribute>
</ModelAttributes>
		</CompoundStep>
	</Map>
</WorkFlowDefinition>
