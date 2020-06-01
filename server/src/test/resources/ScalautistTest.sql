
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
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno], [FasciaOraria]) VALUES (1, N'Prima Mattinata', N'6:00 alle 10:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno], [FasciaOraria]) VALUES (2, N'Tarda Mattinata', N'10:00 alle 14:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno], [FasciaOraria]) VALUES (3, N'Pomeriggio', N'14:00 alle 18:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno], [FasciaOraria]) VALUES (4, N'Sera', N'18:00 alle 22:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno], [FasciaOraria]) VALUES (5, N'Tarda Serata',N'22:00 alle 2:00')
INSERT INTO [dbo].[TurnoSets] ([IdTurno], [NomeTurno], [FasciaOraria]) VALUES (6, N'Notte',N'2:00 alle 6:00')
SET IDENTITY_INSERT [dbo].[TurnoSets] OFF

--//TURNO

--PERSONA

SET IDENTITY_INSERT [dbo].[PersoneSets] ON
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName]) VALUES (1, N'Fabian',  N'Aspee', N'569918598',1, null,  N'admin',1,N'admin')
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName]) VALUES (2, N'Francesco', N'Cassano',N'9184756',3, 1,  N'admin2',1,N'admin2')
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName]) VALUES (3, N'Giovanni', N'Mormone',N'394562358',2, null,  N'root',1,  N'root')
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName]) VALUES (4, N'Luciano',  N'Fuentes',N'365478962',3,2,  N'yoyo',1,  N'yoyo')
INSERT INTO [dbo].[PersoneSets] ([Matricola], [Nome], [Cognome], [NumTelefono], [Ruolo], [Terminale_IdTerminale], [Password],[IsNew],[UserName]) VALUES (5, N'Valerio', N'Vigliano',N'91485236',3,3,  N'tutu',1,  N'tutu')
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
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso]) VALUES (1, N'Full-Time-5x2', 1)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso]) VALUES (2, N'Full-Time-5x2', 0)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso]) VALUES (3, N'Part-Time-5x2', 1)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso]) VALUES (4, N'Part-Time-5x2', 0)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso]) VALUES (5, N'Full-Time-6x1', 1)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso]) VALUES (6, N'Full-Time-6x1', 0)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso]) VALUES (7, N'Part-Time-6x1', 1)
INSERT INTO [dbo].[ContratoeSets] ([IdContratto], [TipoContratto], [TurnoFisso]) VALUES (8, N'Part-Time-6x1', 0)
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
INSERT INTO [dbo].[StoricoContrattoSets] ([IdStoricoContratto], [DataInizio], [DataFine], [Persone_Matricola], [Contrato_IdContratto], [Turno_IdTurno], [Turno1_IdTurno]) VALUES (5, N'20200602',N'20201222',5,5,5,6)
SET IDENTITY_INSERT [dbo].[StoricoContrattoSets] OFF

--//STORICOCONTRATTO
