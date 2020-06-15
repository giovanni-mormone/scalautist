
-- --------------------------------------------------
-- Script has ended
-- --------------------------------------------------

USE [viroli]

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

--DISPONIBILITA

SET IDENTITY_INSERT [dbo].[DisponibilitaStraordinarioSets] ON
INSERT INTO [dbo].[DisponibilitaStraordinarioSets] ([IdDisponibilitaStraordinario], [Giorno1], [Giorno2]) VALUES (1, N'Lunes', N'Sabato')
INSERT INTO [dbo].[DisponibilitaStraordinarioSets] ([IdDisponibilitaStraordinario], [Giorno1], [Giorno2]) VALUES (2, N'Martes', N'Venerdi')
INSERT INTO [dbo].[DisponibilitaStraordinarioSets] ([IdDisponibilitaStraordinario], [Giorno1], [Giorno2]) VALUES (3, N'Miercoles', N'Giovedi')
INSERT INTO [dbo].[DisponibilitaStraordinarioSets] ([IdDisponibilitaStraordinario], [Giorno1], [Giorno2]) VALUES (4, N'Jueves', N'Mercoledi')
INSERT INTO [dbo].[DisponibilitaStraordinarioSets] ([IdDisponibilitaStraordinario], [Giorno1], [Giorno2]) VALUES (5, N'Viernes Serata',N'Martedi')
INSERT INTO [dbo].[DisponibilitaStraordinarioSets] ([IdDisponibilitaStraordinario], [Giorno1], [Giorno2]) VALUES (6, N'Sabado',N'Lunedi')
SET IDENTITY_INSERT [dbo].[DisponibilitaStraordinarioSets] OFF

--//DISPONIBILITA

--PERSONA

SET IDENTITY_INSERT [dbo].[PersoneSets] ON
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName]) VALUES (1, N'Fabian',  N'Aspee', N'569918598',1, null,  N'admin',1,N'admin')
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName]) VALUES (2, N'Francesco', N'Cassano',N'9184756',3, 1,  N'admin2',1,N'admin2')
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName]) VALUES (3, N'Giovanni', N'Mormone',N'394562358',2, null,  N'root',1,  N'root')
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName]) VALUES (4, N'Luciano',  N'Fuentes',N'365478962',3,2,  N'yoyo',1,  N'yoyo')
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName]) VALUES (5, N'Valerio', N'Vigliano',N'91485236',3,3,  N'tutu',1,  N'tutu')
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName],[DisponibilitaStraordinarioSetIdDisponibilitaStraordinario]) VALUES (6, N'Conducente', N'Maestro',N'91485236',3,3,  N'tutu2',1,  N'tutu2',1)
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName],[DisponibilitaStraordinarioSetIdDisponibilitaStraordinario]) VALUES (7, N'Pro', N'Va',N'91485236',3,3,  N'tutu2',1,  N'tutu2',1)
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName],[DisponibilitaStraordinarioSetIdDisponibilitaStraordinario]) VALUES (8, N'MAtto', N'Mattesi',N'91485236',3,3,  N'tutu2',1,  N'tutu2',1)

SET IDENTITY_INSERT [dbo].[PersoneSets] OFF

--//PERSONA

--ASSENZA

SET IDENTITY_INSERT [dbo].[AssenzaSets] ON
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (1,N'20200622',N'20200629',0,2)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (2,N'20200822',N'20200829',0,2)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (3,N'20201222',N'20201231',0,5)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (4,N'20200220',N'20200325',0,6)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (5,N'20200622',N'20200629',0,7)

INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (6,N'20200522',N'20200529',0,2)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (7,N'20200522',N'20200529',0,4)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (8,N'20200522',N'20200531',0,5)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (10,N'20200522',N'20200529',0,7)

INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (11,N'20200422',N'20200429',1,2)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (12,N'20200422',N'20200429',0,4)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (13,N'20200422',N'20200431',1,5)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (15,N'20200422',N'20200429',1,7)

INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (16,N'20200322',N'20200629',1,2)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (18,N'20200322',N'20200331',1,5)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (20,N'20200322',N'20200529',1,7)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (21,N'20200511',N'20200514',1,5)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (22,N'20200502',N'20200505',0,5)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (23,N'20200507',N'20200510',0,5)
INSERT INTO [dbo].[AssenzaSets] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (24,N'20200523',N'20200529',1,5)

SET IDENTITY_INSERT [dbo].[AssenzaSets] OFF

--//ASSENZA

--PRESENZA

SET IDENTITY_INSERT [dbo].[PresenzaSets] ON
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (1,N'20200601',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (2,N'20200602',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (3,N'20200603',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (4,N'20200604',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (5,N'20200609',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (6,N'20200605',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (7,N'20200606',6,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (8,N'20200607',6,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (9,N'20200608',4,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (10,N'20200601',1,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (11,N'20200602',2,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (12,N'20200603',2,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (13,N'20200604',5,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (14,N'20200609',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (15,N'20200605',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (16,N'20200606',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (17,N'20200607',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (18,N'20200608',4,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (19,N'20200601',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (20,N'20200602',2,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (21,N'20200603',1,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (22,N'20200604',5,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (23,N'20200609',6,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (24,N'20200605',6,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (25,N'20200606',5,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (26,N'20200607',4,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (27,N'20200608',4,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (28,N'20200601',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (29,N'20200602',2,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (30,N'20200603',1,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (31,N'20200604',2,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (32,N'20200609',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (33,N'20200605',4,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (34,N'20200606',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (35,N'20200607',4,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (36,N'20200608',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (37,N'20200601',3,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (38,N'20200602',3,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (39,N'20200603',3,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (40,N'20200604',5,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (41,N'20200609',5,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (42,N'20200605',5,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (43,N'20200606',6,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (44,N'20200607',5,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (45,N'20200608',4,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (46,N'20200601',3,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (47,N'20200602',2,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (48,N'20200603',2,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (49,N'20200604',1,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (50,N'20200609',6,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (51,N'20200605',6,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (52,N'20200606',6,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (53,N'20200607',5,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (54,N'20200608',5,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (55,N'20200609',4,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (56,N'20200610',3,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (57,N'20200611',2,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (58,N'20200612',2,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (59,N'20200613',2,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (60,N'20200614',2,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (61,N'20200615',3,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (62,N'20200616',3,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (63,N'20200617',1,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (64,N'20200618',1,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (65,N'20200619',1,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (66,N'20200620',6,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (67,N'20200621',6,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (68,N'20200622',6,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (69,N'20200623',6,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (70,N'20200624',6,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (71,N'20200625',6,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (72,N'20200626',6,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (73,N'20200627',1,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (74,N'20200628',1,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (75,N'20200629',1,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (76,N'20200630',5,8,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (77,N'20200615',5,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (78,N'20200616',5,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (79,N'20200617',5,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (80,N'20200618',5,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (81,N'20200619',5,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (82,N'20200620',5,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (83,N'20200621',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (84,N'20200622',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (85,N'20200623',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (86,N'20200624',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (87,N'20200625',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (88,N'20200626',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (89,N'20200627',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (90,N'20200628',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (91,N'20200629',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (92,N'20200630',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (93,N'20200627',1,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (94,N'20200628',1,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (95,N'20200629',1,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (96,N'20200630',1,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (97,N'20200615',1,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (98,N'20200616',1,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (99,N'20200617',1,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (100,N'20200521',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (101,N'20200522',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (102,N'20200523',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (103,N'20200524',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (104,N'20200525',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (105,N'20200526',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (106,N'20200527',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (107,N'20200528',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (108,N'20200529',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (109,N'20200530',1,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (110,N'20200501',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (111,N'20200521',2,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (112,N'20200522',5,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (113,N'20200523',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (114,N'20200524',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (115,N'20200525',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (116,N'20200526',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (117,N'20200527',4,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (118,N'20200528',3,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (119,N'20200529',2,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (120,N'20200530',1,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (121,N'20200501',5,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (122,N'20200522',6,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (123,N'20200523',6,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (124,N'20200524',5,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (125,N'20200525',4,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (126,N'20200526',4,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (127,N'20200527',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (128,N'20200528',2,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (129,N'20200529',1,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (130,N'20200530',2,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (131,N'20200501',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (132,N'20200501',4,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (133,N'20200522',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (134,N'20200523',4,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (135,N'20200524',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (136,N'20200525',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (137,N'20200526',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (138,N'20200527',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (139,N'20200528',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (140,N'20200529',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (141,N'20200530',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (142,N'20200501',6,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (143,N'20200501',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (144,N'20200522',4,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (145,N'20200523',3,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (146,N'20200524',2,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (147,N'20200525',2,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (148,N'20200526',1,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (149,N'20200527',6,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (150,N'20200528',6,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (151,N'20200529',6,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (152,N'20200530',5,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (153,N'20200501',5,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (154,N'20200501',4,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (155,N'20200422',3,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (156,N'20200423',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (157,N'20200424',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (158,N'20200425',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (159,N'20200426',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (160,N'20200427',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (161,N'20200428',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (162,N'20200429',5,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (163,N'20200430',6,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (164,N'20200401',6,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (165,N'20200402',6,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (166,N'20200422',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (167,N'20200423',4,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (168,N'20200424',3,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (169,N'20200425',2,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (170,N'20200426',1,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (171,N'20200427',5,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (172,N'20200428',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (173,N'20200429',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (174,N'20200430',5,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (175,N'20200401',4,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (176,N'20200402',4,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (177,N'20200422',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (178,N'20200423',2,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (179,N'20200424',1,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (180,N'20200425',2,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (181,N'20200426',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (182,N'20200427',4,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (183,N'20200428',5,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (184,N'20200429',4,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (185,N'20200430',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (186,N'20200402',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (187,N'20200403',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (188,N'20200422',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (189,N'20200423',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (190,N'20200424',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (191,N'20200425',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (192,N'20200426',6,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (193,N'20200427',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (194,N'20200428',4,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (195,N'20200429',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (196,N'20200430',2,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (197,N'20200402',2,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (198,N'20200405',1,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (199,N'20200401',6,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (200,N'20200321',6,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (201,N'20200322',6,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (202,N'20200323',5,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (203,N'20200324',5,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (204,N'20200325',4,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (205,N'20200326',3,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (206,N'20200327',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (207,N'20200328',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (208,N'20200329',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (209,N'20200330',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (210,N'20200301',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (211,N'20200321',2,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (212,N'20200322',5,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (213,N'20200323',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (214,N'20200324',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (215,N'20200325',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (216,N'20200326',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (217,N'20200327',4,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (218,N'20200328',3,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (219,N'20200329',2,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (220,N'20200330',1,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (221,N'20200301',5,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (222,N'20200322',6,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (223,N'20200323',6,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (224,N'20200324',5,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (225,N'20200325',4,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (226,N'20200326',4,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (227,N'20200327',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (228,N'20200328',2,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (229,N'20200329',1,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (230,N'20200330',2,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (231,N'20200301',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (232,N'20200301',4,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (233,N'20200322',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (234,N'20200323',4,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (235,N'20200324',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (236,N'20200325',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (237,N'20200326',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (238,N'20200327',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (239,N'20200328',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (240,N'20200329',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (241,N'20200330',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (242,N'20200301',6,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (243,N'20200301',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (244,N'20200322',4,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (245,N'20200323',3,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (246,N'20200324',2,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (247,N'20200325',2,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (248,N'20200326',1,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (249,N'20200327',6,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (250,N'20200328',6,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (251,N'20200329',6,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (252,N'20200330',5,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (253,N'20200301',5,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (254,N'20200301',4,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (255,N'20200222',3,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (256,N'20200223',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (257,N'20200224',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (258,N'20200225',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (259,N'20200226',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (260,N'20200227',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (261,N'20200228',2,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (262,N'20200229',5,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (263,N'20200211',6,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (264,N'20200201',6,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (265,N'20200202',6,2,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (266,N'20200222',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (267,N'20200223',4,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (268,N'20200224',3,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (269,N'20200225',2,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (270,N'20200226',1,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (271,N'20200227',5,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (272,N'20200228',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (273,N'20200229',6,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (274,N'20200212',5,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (275,N'20200201',4,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (276,N'20200202',4,4,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (277,N'20200222',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (278,N'20200223',2,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (279,N'20200224',1,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (280,N'20200225',2,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (281,N'20200226',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (282,N'20200227',4,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (283,N'20200228',5,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (284,N'20200229',4,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (285,N'20200212',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (286,N'20200202',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (287,N'20200203',3,5,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (288,N'20200222',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (289,N'20200223',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (290,N'20200224',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (291,N'20200225',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (292,N'20200226',6,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (293,N'20200227',5,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (294,N'20200228',4,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (295,N'20200229',3,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (296,N'20200212',2,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (297,N'20200202',2,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (298,N'20200205',1,6,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (299,N'20200201',6,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (300,N'20200211',6,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (301,N'20200222',6,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (302,N'20200223',5,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (303,N'20200224',5,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (304,N'20200225',4,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (305,N'20200226',3,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (306,N'20200227',2,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (307,N'20200228',2,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (308,N'20200229',2,7,0)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (309,N'20200212',2,7,0)

INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (310, N'20200622',2,2,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (311, N'20200619',4,4,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (312, N'20200630',5,5,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (313, N'20200618',2,7,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola],[IsStraordinario]) VALUES (314, N'20200602',4,8,1)

SET IDENTITY_INSERT [dbo].[PresenzaSets] OFF

--//PRESENZA

--STIPENDIO

SET IDENTITY_INSERT [dbo].[StipendioSets] ON
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (1,N'20200501',1700,2)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (2,N'20200501',2000,4)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (3,N'20200501',2900,5)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (4,N'20200501',3300,6)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (5,N'20200501',3000,7)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (6,N'20200401',1700,2)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (7,N'20200401',2000,4)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (8,N'20200401',2900,5)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (9,N'20200401',3300,6)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (10,N'20200401',3000,7)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (11,N'20200301',3000,2)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (12,N'20200301',3000,4)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (13,N'20200301',3000,5)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (14,N'20200301',3000,6)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (15,N'20200301',3000,7)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (16,N'20200201',3000,2)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (17,N'20200201',3000,4)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (18,N'20200201',3000,5)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (19,N'20200201',3000,6)
INSERT INTO [dbo].[StipendioSets] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (20,N'20200201',3000,7)

SET IDENTITY_INSERT [dbo].[StipendioSets] OFF

--//STIPENDIO

--CONTRATTO

SET IDENTITY_INSERT [dbo].[ContratoeSets] ON
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[PartTime],[Ruolo]) VALUES (1, N'Full-Time-5x2', 1, 0, 3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[PartTime],[Ruolo]) VALUES (2, N'Full-Time-5x2', 0, 0, 3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[PartTime],[Ruolo]) VALUES (3, N'Part-Time-5x2', 1, 1, 3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[PartTime],[Ruolo]) VALUES (4, N'Part-Time-5x2', 0, 1, 3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[PartTime],[Ruolo]) VALUES (5, N'Full-Time-6x1', 1, 0, 3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[PartTime],[Ruolo]) VALUES (6, N'Full-Time-6x1', 0, 0, 3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[PartTime],[Ruolo]) VALUES (7, N'Part-Time-6x1', 1, 1, 3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[PartTime],[Ruolo]) VALUES (8, N'Part-Time-6x1', 0, 1, 3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[PartTime],[Ruolo]) VALUES (9, N'Manager',0, 0, 1)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[PartTime],[Ruolo]) VALUES (10, N'Risorse Umane',0, 0, 2)
SET IDENTITY_INSERT [dbo].[ContratoeSets] OFF

--//CONTRATTO

--STORICOCONTRATTO

SET IDENTITY_INSERT [dbo].[StoricoContrattoSets] ON
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (1, N'20200622',N'20200822',1,1,1,2 )
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (2, N'20200619',N'20201019',2,2,2,3 )
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (3, N'20200530',N'20210530',3,3,3, null)
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (4, N'20200531',N'20220531',4,4,4, null)
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (5, N'20200602',N'20201222',6,5,5,6)

INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (6, N'20200602',N'20201222',6,5,5,6)
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (7, N'20200602',N'20201222',6,5,5,6)
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (8, N'20200602',N'20201222',6,5,5,6)
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (9, N'20200602',N'20201222',5,5,5,6)
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (10, N'20200602',N'20201222',5,5,5,6)

INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (11, N'20200602',N'20201222',8,5,5,6)
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (12, N'20200602',N'20201222',8,5,5,6)
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (13, N'20200602',N'20201222',7,5,5,6)
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (14, N'20200602',N'20201222',7,5,5,6)
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (15, N'20200602',N'20201222',7,5,5,6)

SET IDENTITY_INSERT [dbo].[StoricoContrattoSets] OFF

--//STORICOCONTRATTO
select * from [dbo].[PresenzaSets] where data < '2020-06-06'
select * from AssenzaSets;
select * from StipendioSets
select * from PersoneSets
select * from PersoneSets full join AssenzaSets on Matricola=PersoneSet_Matricola

select * from PersoneSets where Ruolo = 3
select * from StoricoContrattoSets

select * from PresenzaSets
select * from TurnoSets