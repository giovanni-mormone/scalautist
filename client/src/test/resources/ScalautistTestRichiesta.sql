
-- --------------------------------------------------
-- Script has ended
-- --------------------------------------------------

USE [virolitest]

--
-- STARE ATTENTI CON LE FOREIGN KEY, CHIEDERE PRIMA !!!!!!! =)
--

--ZONA

SET IDENTITY_INSERT [dbo].[ZonaSets] ON
INSERT INTO [dbo].[ZonaSets] ([IdZona], [Zone]) VALUES (0, N'Default')
INSERT INTO [dbo].[ZonaSets] ([IdZona], [Zone]) VALUES (1, N'Cesena')
INSERT INTO [dbo].[ZonaSets] ([IdZona], [Zone]) VALUES (2, N'Cesenatico')
INSERT INTO [dbo].[ZonaSets] ([IdZona], [Zone]) VALUES (3, N'SantGiovanni')
INSERT INTO [dbo].[ZonaSets] ([IdZona], [Zone]) VALUES (4, N'Savignano')
SET IDENTITY_INSERT [dbo].[ZonaSets] OFF

--//ZONA

--TERMINALE

SET IDENTITY_INSERT [dbo].[TerminalSets] ON
INSERT INTO [dbo].[TerminalSets] ([IdTerminale], [NomeTerminale], [ZonaIdZona]) VALUES (0, N'Cansas', 0)
INSERT INTO [dbo].[TerminalSets] ([IdTerminale], [NomeTerminale], [ZonaIdZona]) VALUES (1, N'Cansas', 1)
INSERT INTO [dbo].[TerminalSets] ([IdTerminale], [NomeTerminale], [ZonaIdZona]) VALUES (2, N'Casablanca', 2)
INSERT INTO [dbo].[TerminalSets] ([IdTerminale], [NomeTerminale], [ZonaIdZona]) VALUES (3, N'Florida', 3)
INSERT INTO [dbo].[TerminalSets] ([IdTerminale], [NomeTerminale], [ZonaIdZona]) VALUES (4, N'Roma', 4)
INSERT INTO [dbo].[TerminalSets] ([IdTerminale], [NomeTerminale], [ZonaIdZona]) VALUES (5, N'Pajaritos',1)
SET IDENTITY_INSERT [dbo].[TerminalSets] OFF

--//TERMINALE


--TURNO

SET IDENTITY_INSERT [dbo].[TurnoSets] ON
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno],[Paga], [FasciaOraria]) VALUES (1, N'Prima Mattinata',32, N'6:00 alle 10:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno],[Paga], [FasciaOraria]) VALUES (2, N'Tarda Mattinata',32, N'10:00 alle 14:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno],[Paga], [FasciaOraria]) VALUES (3, N'Pomeriggio',32, N'14:00 alle 18:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno],[Paga], [FasciaOraria]) VALUES (4, N'Sera',32, N'18:00 alle 22:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno],[Paga], [FasciaOraria]) VALUES (5, N'Tarda Serata',41.6,N'22:00 alle 2:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno],[Paga], [FasciaOraria]) VALUES (6, N'Notte',41.6,N'2:00 alle 6:00')
SET IDENTITY_INSERT [dbo].[TurnoSets] OFF

--//TURNO

