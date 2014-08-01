CREATE TABLE [dbo].[OW_HISTORY] (
	[EventIndex]      [numeric]  (18,0) IDENTITY (1,1) NOT NULL,
	[OW_HIST_Type]    [numeric]  (18,0) NOT NULL,
	[OW_HIST_ID]      [varchar]  (128)  COLLATE Latin1_General_CI_AS NOT NULL,
	[OW_HIST_Status]  [numeric]  (18,0) NULL,
	[OW_HIST_Time]    [datetime]        NULL,
	[OW_HIST_User]    [varchar]  (255)  COLLATE Latin1_General_CI_AS NOT NULL,
	[OW_HIST_Summary] [varchar]  (2048)  COLLATE Latin1_General_CI_AS NULL,
	[ObjectDMSID]     [varchar]  (255)  COLLATE Latin1_General_CI_AS NULL,
	[ObjectName]      [varchar]  (1024)  COLLATE Latin1_General_CI_AS NULL,
	[ParentDMSID]     [varchar]  (255)  COLLATE Latin1_General_CI_AS NULL,
	[ParentName]      [varchar]  (1024)  COLLATE Latin1_General_CI_AS NULL,
	[Custom1]   [varchar]  (2048)  COLLATE Latin1_General_CI_AS NULL,
	[Custom2]   [varchar]  (2048)  COLLATE Latin1_General_CI_AS NULL,
	[Custom3]   [varchar]  (2048)  COLLATE Latin1_General_CI_AS NULL
) ON [PRIMARY]
GO