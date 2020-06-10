
USE [viroli]

--
-- STARE ATTENTI CON LE FOREIGN KEY, CHIEDERE PRIMA !!!!!!! =)
--

--ZONA

SET IDENTITY_INSERT [dbo].[ZonaSets] ON
INSERT INTO [dbo].[ZonaSets] ([IdZona], [Zone]) VALUES (1, N'Cesena')
INSERT INTO [dbo].[ZonaSets] ([IdZona], [Zone]) VALUES (2, N'Cesenatico')
INSERT INTO [dbo].[ZonaSets] ([IdZona], [Zone]) VALUES (3, N'SantGiovanni')
INSERT INTO [dbo].[ZonaSets] ([IdZona], [Zone]) VALUES (4, N'Savignano')
SET IDENTITY_INSERT [dbo].[ZonaSets] OFF

--//ZONA

--TERMINALE

SET IDENTITY_INSERT [dbo].[TerminalSets] ON
INSERT INTO [dbo].[TerminalSets] ([IdTerminale], [NomeTerminale], [ZonaIdZona]) VALUES (1, N'Cansas', 1)
INSERT INTO [dbo].[TerminalSets] ([IdTerminale], [NomeTerminale], [ZonaIdZona]) VALUES (2, N'Casablanca', 2)
INSERT INTO [dbo].[TerminalSets] ([IdTerminale], [NomeTerminale], [ZonaIdZona]) VALUES (3, N'Florida', 3)
INSERT INTO [dbo].[TerminalSets] ([IdTerminale], [NomeTerminale], [ZonaIdZona]) VALUES (4, N'Roma', 4)
INSERT INTO [dbo].[TerminalSets] ([IdTerminale], [NomeTerminale], [ZonaIdZona]) VALUES (5, N'Pajaritos',1)
SET IDENTITY_INSERT [dbo].[TerminalSets] OFF

--//TERMINALE


--TURNO

SET IDENTITY_INSERT [dbo].[TurnoSets] ON
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno],[Notturno], [FasciaOraria]) VALUES (1, N'Prima Mattinata',0, N'6:00 alle 10:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno],[Notturno], [FasciaOraria]) VALUES (2, N'Tarda Mattinata',0, N'10:00 alle 14:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno],[Notturno], [FasciaOraria]) VALUES (3, N'Pomeriggio',0, N'14:00 alle 18:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno],[Notturno], [FasciaOraria]) VALUES (4, N'Sera',0, N'18:00 alle 22:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno],[Notturno], [FasciaOraria]) VALUES (5, N'Tarda Serata',1,N'22:00 alle 2:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno],[Notturno], [FasciaOraria]) VALUES (6, N'Notte',1,N'2:00 alle 6:00')
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

SET IDENTITY_INSERT [dbo].[AssenzaSet] ON
INSERT INTO [dbo].[AssenzaSet] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (1,N'20200622',N'20200822',1,1)
INSERT INTO [dbo].[AssenzaSet] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (2,N'20200619',N'20201019',0,1)
INSERT INTO [dbo].[AssenzaSet] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (3,N'20200530',N'20210530',1,3)
INSERT INTO [dbo].[AssenzaSet] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (4,N'20200622',N'20200822',0,3)
INSERT INTO [dbo].[AssenzaSet] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (5,N'20200619',N'20201019',0,2)
INSERT INTO [dbo].[AssenzaSet] ([IdAssenza], [DataInizio], [DataFine], [IsMalattia],[PersoneSet_Matricola]) VALUES (6,N'20200530',N'20210530',1,2)
SET IDENTITY_INSERT [dbo].[AssenzaSet] OFF

--//ASSENZA

--PRESENZA

SET IDENTITY_INSERT [dbo].[PresenzaSets] ON
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (1,N'20200622',1,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (2,N'20200619',1,3)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (3,N'20200530',1,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (4,N'20200622',1,3)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (5,N'20200619',1,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (6,N'20200619',1,5)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (7,N'20200530',1,4)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (8,N'20200622',1,4)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (9,N'20200619',1,3)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (10,N'20200530',1,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (11,N'20200622',1,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (12,N'20200619',1,3)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (13,N'20200619',1,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (14,N'20200530',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (15,N'20200622',1,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (16,N'20200619',1,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (17,N'20200530',1,6)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (18,N'20200622',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (19,N'20200619',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (20,N'20200619',1,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (21,N'20200530',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (22,N'20200622',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (23,N'20200619',1,7)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (24,N'20200530',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (25,N'20200622',1,8)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (26,N'20200619',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (27,N'20200619',1,8)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (28,N'20200530',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (29,N'20200622',1,8)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (30,N'20200619',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (31,N'20200622',1,7)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (32,N'20200619',1,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (33,N'20200530',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (34,N'20200622',1,6)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (35,N'20200619',1,5)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (36,N'20200619',1,5)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (37,N'20200530',1,5)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (38,N'20200622',1,6)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (39,N'20200619',1,6)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (40,N'20200530',1,6)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (41,N'20200622',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (42,N'20200619',1,3)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (43,N'20200619',1,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (44,N'20200530',1,3)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (45,N'20200622',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (46,N'20200619',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (47,N'20200530',1,3)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (48,N'20200622',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (49,N'20200619',1,4)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (50,N'20200619',1,1)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (51,N'20200530',1,5)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (52,N'20200622',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (53,N'20200619',1,7)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (54,N'20200530',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (55,N'20200622',1,8)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (56,N'20200619',1,2)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (57,N'20200619',1,8)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (58,N'20200530',1,6)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (59,N'20200622',1,8)
INSERT INTO [dbo].[PresenzaSets] ([IdPresenza], [Data],[Turno_IdTurno],[Persone_Matricola]) VALUES (60,N'20200619',1,7)

SET IDENTITY_INSERT [dbo].[PresenzaSets] OFF

--//PRESENZA

--STIPENDIO

SET IDENTITY_INSERT [dbo].[StipendioSet] ON
INSERT INTO [dbo].[StipendioSet] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (1,N'20200622',30000.95,1)
INSERT INTO [dbo].[StipendioSet] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (2,N'20200619',31000.96,1)
INSERT INTO [dbo].[StipendioSet] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (3,N'20200530',32000.97,2)
INSERT INTO [dbo].[StipendioSet] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (4,N'20200622',33000.98,2)
INSERT INTO [dbo].[StipendioSet] ([IdStipendio], [Data],[Valore],[PersoneSet_Matricola]) VALUES (5,N'20200619',34000.99,2)
SET IDENTITY_INSERT [dbo].[StipendioSet] OFF

--//STIPENDIO

--CONTRATTO

SET IDENTITY_INSERT [dbo].[ContratoeSets] ON
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[Ruolo]) VALUES (1, N'Full-Time-5x2', 1,3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[Ruolo]) VALUES (2, N'Full-Time-5x2', 0,3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[Ruolo]) VALUES (3, N'Part-Time-5x2', 1,3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[Ruolo]) VALUES (4, N'Part-Time-5x2', 0,3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[Ruolo]) VALUES (5, N'Full-Time-6x1', 1,3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[Ruolo]) VALUES (6, N'Full-Time-6x1', 0,3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[Ruolo]) VALUES (7, N'Part-Time-6x1', 1,3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[Ruolo]) VALUES (8, N'Part-Time-6x1', 0,3)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[Ruolo]) VALUES (9, N'Manager',0,1)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso],[Ruolo]) VALUES (10, N'Risorse Umane',0,2)
SET IDENTITY_INSERT [dbo].[ContratoeSets] OFF

--//CONTRATTO

--STRAORDINARIO

SET IDENTITY_INSERT [dbo].[StraordinariSets] ON
INSERT INTO [dbo].[StraordinariSets] ([IdStraordinari], [Data], [Persone_Matricola], [Turno_IdTurno]) VALUES (1, N'20200622', 2,1)
INSERT INTO [dbo].[StraordinariSets] ([IdStraordinari], [Data], [Persone_Matricola], [Turno_IdTurno]) VALUES (2, N'20200619', 4,2)
INSERT INTO [dbo].[StraordinariSets] ([IdStraordinari], [Data], [Persone_Matricola], [Turno_IdTurno]) VALUES (3, N'20200530', 5,3)
INSERT INTO [dbo].[StraordinariSets] ([IdStraordinari], [Data], [Persone_Matricola], [Turno_IdTurno]) VALUES (4, N'20200531', 2,4)
INSERT INTO [dbo].[StraordinariSets] ([IdStraordinari], [Data], [Persone_Matricola], [Turno_IdTurno]) VALUES (5, N'20200602', 4,5)
SET IDENTITY_INSERT [dbo].[StraordinariSets] OFF

--//STRAORDINARIO

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
select * from AssenzaSet;
select * from [dbo].[StraordinariSets] where data < '2020-06-06'
select * from StipendioSet
select * from PersoneSets
select * from StoricoContrattoSets
select * from TurnoSets
