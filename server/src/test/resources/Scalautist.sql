
-- --------------------------------------------------
-- Entity Designer DDL Script for SQL Server 2005, 2008, 2012 and Azure
-- --------------------------------------------------
-- Date Created: 06/18/2020 17:30:48
-- Generated from EDMX file: C:\Users\faspe\source\repos\ViroliDataBases\ViroliDataBases\ViroliDatabase.edmx
-- --------------------------------------------------

SET QUOTED_IDENTIFIER OFF;

USE [viroli];

IF SCHEMA_ID(N'dbo') IS NULL EXECUTE(N'CREATE SCHEMA [dbo]');


-- --------------------------------------------------
-- Dropping existing FOREIGN KEY constraints
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[FK_AssenzaPersoneSet]', 'F') IS NOT NULL
ALTER TABLE [dbo].[AssenzaSets] DROP CONSTRAINT [FK_AssenzaPersoneSet];

IF OBJECT_ID(N'[dbo].[FK_StoricoContrattoContrato]', 'F') IS NOT NULL
ALTER TABLE [dbo].[StoricoContrattoSets] DROP CONSTRAINT [FK_StoricoContrattoContrato];

IF OBJECT_ID(N'[dbo].[FK_DisponibilitaStraordinarioPersoneSet]', 'F') IS NOT NULL
ALTER TABLE [dbo].[PersoneSets] DROP CONSTRAINT [FK_DisponibilitaStraordinarioPersoneSet];

IF OBJECT_ID(N'[dbo].[FK_GiornoSettimana]', 'F') IS NOT NULL
ALTER TABLE [dbo].[GiornoInSettimanaSets] DROP CONSTRAINT [FK_GiornoSettimana];

IF OBJECT_ID(N'[dbo].[FK_SettimanaTurno]', 'F') IS NOT NULL
ALTER TABLE [dbo].[GiornoInSettimanaSets] DROP CONSTRAINT [FK_SettimanaTurno];

IF OBJECT_ID(N'[dbo].[FK_SettimaneSettimanaSet]', 'F') IS NOT NULL
ALTER TABLE [dbo].[GiornoInSettimanaSets] DROP CONSTRAINT [FK_SettimaneSettimanaSet];

IF OBJECT_ID(N'[dbo].[FK_RichiestaGiorno]', 'F') IS NOT NULL
ALTER TABLE [dbo].[RichiestaSets] DROP CONSTRAINT [FK_RichiestaGiorno];

IF OBJECT_ID(N'[dbo].[FK_ParametriGruppiTerminali]', 'F') IS NOT NULL
ALTER TABLE [dbo].[GruppiTerminaliSets] DROP CONSTRAINT [FK_ParametriGruppiTerminali];

IF OBJECT_ID(N'[dbo].[FK_ParametriSetSettimane]', 'F') IS NOT NULL
ALTER TABLE [dbo].[SettimaneSets] DROP CONSTRAINT [FK_ParametriSetSettimane];

IF OBJECT_ID(N'[dbo].[FK_PresenzaPersone]', 'F') IS NOT NULL
ALTER TABLE [dbo].[PresenzaSets] DROP CONSTRAINT [FK_PresenzaPersone];

IF OBJECT_ID(N'[dbo].[FK_RisultatoPersone]', 'F') IS NOT NULL
ALTER TABLE [dbo].[RisultatoSets] DROP CONSTRAINT [FK_RisultatoPersone];

IF OBJECT_ID(N'[dbo].[FK_StipendioPersoneSet]', 'F') IS NOT NULL
ALTER TABLE [dbo].[StipendioSets] DROP CONSTRAINT [FK_StipendioPersoneSet];

IF OBJECT_ID(N'[dbo].[FK_StoricoContrattoPersone]', 'F') IS NOT NULL
ALTER TABLE [dbo].[StoricoContrattoSets] DROP CONSTRAINT [FK_StoricoContrattoPersone];

IF OBJECT_ID(N'[dbo].[FK_TerminalePersone]', 'F') IS NOT NULL
ALTER TABLE [dbo].[PersoneSets] DROP CONSTRAINT [FK_TerminalePersone];

IF OBJECT_ID(N'[dbo].[FK_PresenzaTurno]', 'F') IS NOT NULL
ALTER TABLE [dbo].[PresenzaSets] DROP CONSTRAINT [FK_PresenzaTurno];

IF OBJECT_ID(N'[dbo].[FK_RichiestaTeoricaRichiesta]', 'F') IS NOT NULL
ALTER TABLE [dbo].[RichiestaSets] DROP CONSTRAINT [FK_RichiestaTeoricaRichiesta];

IF OBJECT_ID(N'[dbo].[FK_RichiestaTurno]', 'F') IS NOT NULL
ALTER TABLE [dbo].[RichiestaSets] DROP CONSTRAINT [FK_RichiestaTurno];

IF OBJECT_ID(N'[dbo].[FK_TurnoRisultato]', 'F') IS NOT NULL
ALTER TABLE [dbo].[RisultatoSets] DROP CONSTRAINT [FK_TurnoRisultato];

IF OBJECT_ID(N'[dbo].[FK_StoricoContrattoTurno]', 'F') IS NOT NULL
ALTER TABLE [dbo].[StoricoContrattoSets] DROP CONSTRAINT [FK_StoricoContrattoTurno];

IF OBJECT_ID(N'[dbo].[FK_TurnoStoricoContratto]', 'F') IS NOT NULL
ALTER TABLE [dbo].[StoricoContrattoSets] DROP CONSTRAINT [FK_TurnoStoricoContratto];

IF OBJECT_ID(N'[dbo].[FK_ZonaTerminale]', 'F') IS NOT NULL
ALTER TABLE [dbo].[TerminalSets] DROP CONSTRAINT [FK_ZonaTerminale];

IF OBJECT_ID(N'[dbo].[FK_TerminalSetRichiestaTeoricaSet]', 'F') IS NOT NULL
ALTER TABLE [dbo].[RichiestaTeoricaSets] DROP CONSTRAINT [FK_TerminalSetRichiestaTeoricaSet];


-- --------------------------------------------------
-- Dropping existing tables
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[AssenzaSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[AssenzaSets];

IF OBJECT_ID(N'[dbo].[ContratoeSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[ContratoeSets];

IF OBJECT_ID(N'[dbo].[database_firewall_rules]', 'U') IS NOT NULL
    DROP TABLE [dbo].[database_firewall_rules];

IF OBJECT_ID(N'[dbo].[database_firewall_rules1]', 'U') IS NOT NULL
    DROP TABLE [dbo].[database_firewall_rules1];

IF OBJECT_ID(N'[dbo].[DisponibilitaStraordinarioSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[DisponibilitaStraordinarioSets];

IF OBJECT_ID(N'[dbo].[GiornoInSettimanaSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[GiornoInSettimanaSets];

IF OBJECT_ID(N'[dbo].[GiornoSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[GiornoSets];

IF OBJECT_ID(N'[dbo].[GruppiTerminaliSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[GruppiTerminaliSets];

IF OBJECT_ID(N'[dbo].[ParametriSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[ParametriSets];

IF OBJECT_ID(N'[dbo].[PersoneSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[PersoneSets];

IF OBJECT_ID(N'[dbo].[PresenzaSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[PresenzaSets];

IF OBJECT_ID(N'[dbo].[RichiestaSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[RichiestaSets];

IF OBJECT_ID(N'[dbo].[RichiestaTeoricaSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[RichiestaTeoricaSets];

IF OBJECT_ID(N'[dbo].[RisultatoSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[RisultatoSets];

IF OBJECT_ID(N'[dbo].[SettimaneSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[SettimaneSets];

IF OBJECT_ID(N'[dbo].[StipendioSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[StipendioSets];

IF OBJECT_ID(N'[dbo].[StoricoContrattoSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[StoricoContrattoSets];

IF OBJECT_ID(N'[dbo].[sysdiagrams]', 'U') IS NOT NULL
    DROP TABLE [dbo].[sysdiagrams];

IF OBJECT_ID(N'[dbo].[TerminalSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[TerminalSets];

IF OBJECT_ID(N'[dbo].[TurnoSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[TurnoSets];

IF OBJECT_ID(N'[dbo].[ZonaSets]', 'U') IS NOT NULL
    DROP TABLE [dbo].[ZonaSets];

IF OBJECT_ID(N'[dbo].[database_firewall_rules2]', 'U') IS NOT NULL
    DROP TABLE [dbo].[database_firewall_rules2];


-- --------------------------------------------------
-- Creating all tables
-- --------------------------------------------------

-- Creating table 'AssenzaSets'
CREATE TABLE [dbo].[AssenzaSets] (
                                     [IdAssenza] int IDENTITY(1,1) NOT NULL,
                                     [DataInizio] datetime  NOT NULL,
                                     [DataFine] datetime  NOT NULL,
                                     [IsMalattia] bit  NOT NULL,
                                     [PersoneSet_Matricola] int  NOT NULL
);


-- Creating table 'ContratoeSets'
CREATE TABLE [dbo].[ContratoeSets] (
                                       [IdContratto] int IDENTITY(1,1) NOT NULL,
                                       [TipoContratto] nvarchar(max)  NOT NULL,
                                       [TurnoFisso] bit  NOT NULL,
                                       [PartTime] bit  NOT NULL,
                                       [Ruolo] int  NOT NULL
);


-- Creating table 'database_firewall_rules'
CREATE TABLE [dbo].[database_firewall_rules] (
                                                 [id] int IDENTITY(1,1) NOT NULL,
                                                 [name] nvarchar(128)  NOT NULL,
                                                 [start_ip_address] varchar(45)  NOT NULL,
                                                 [end_ip_address] varchar(45)  NOT NULL,
                                                 [create_date] datetime  NOT NULL,
                                                 [modify_date] datetime  NOT NULL
);


-- Creating table 'database_firewall_rules1'
CREATE TABLE [dbo].[database_firewall_rules1] (
                                                  [id] int IDENTITY(1,1) NOT NULL,
                                                  [name] nvarchar(128)  NOT NULL,
                                                  [start_ip_address] varchar(45)  NOT NULL,
                                                  [end_ip_address] varchar(45)  NOT NULL,
                                                  [create_date] datetime  NOT NULL,
                                                  [modify_date] datetime  NOT NULL
);


-- Creating table 'DisponibilitaStraordinarioSets'
CREATE TABLE [dbo].[DisponibilitaStraordinarioSets] (
                                                        [IdDisponibilitaStraordinario] int IDENTITY(1,1) NOT NULL,
                                                        [Giorno1] nvarchar(max)  NOT NULL,
                                                        [Giorno2] nvarchar(max)  NOT NULL,
                                                        [Settimana] int  NOT NULL
);


-- Creating table 'GiornoInSettimanaSets'
CREATE TABLE [dbo].[GiornoInSettimanaSets] (
                                               [IdSettimana] int IDENTITY(1,1) NOT NULL,
                                               [GiornoIdGiorno] int  NOT NULL,
                                               [Turno_IdTurno] int  NOT NULL,
                                               [Parametri_IdParametri] int  NOT NULL,
                                               [SettimaneIdSettimane] int  NOT NULL
);


-- Creating table 'GiornoSets'
CREATE TABLE [dbo].[GiornoSets] (
                                    [IdGiorno] int IDENTITY(1,1) NOT NULL,
                                    [Quantita] int  NOT NULL,
                                    [NomeGiorno] nvarchar(max)  NOT NULL,
                                    [IdGiornoInSettimana] nvarchar(max)  NOT NULL
);


-- Creating table 'GruppiTerminaliSets'
CREATE TABLE [dbo].[GruppiTerminaliSets] (
                                             [IdGrupoTerminale] int IDENTITY(1,1) NOT NULL,
                                             [ParametriIdParametri] int  NOT NULL
);


-- Creating table 'ParametriSets'
CREATE TABLE [dbo].[ParametriSets] (
                                       [IdParametri] int IDENTITY(1,1) NOT NULL,
                                       [TreSabato] bit  NOT NULL,
                                       [Rela] nvarchar(max)  NOT NULL
);


-- Creating table 'PersoneSets'
CREATE TABLE [dbo].[PersoneSets] (
                                     [Matricola] int IDENTITY(1,1) NOT NULL,
                                     [Nome] nvarchar(max)  NOT NULL,
                                     [Cognome] nvarchar(max)  NOT NULL,
                                     [NumTelefono] nvarchar(max)  NOT NULL,
                                     [Ruolo] int  NOT NULL,
                                     [Terminale_IdTerminale] int  NULL,
                                     [Password] nvarchar(max)  NOT NULL,
                                     [IsNew] bit  NOT NULL,
                                     [UserName] nvarchar(max)  NOT NULL,
                                     [DisponibilitaStraordinarioSetIdDisponibilitaStraordinario] int  NULL
);


-- Creating table 'PresenzaSets'
CREATE TABLE [dbo].[PresenzaSets] (
                                      [IdPresenza] int IDENTITY(1,1) NOT NULL,
                                      [Data] datetime  NOT NULL,
                                      [Persone_Matricola] int  NOT NULL,
                                      [Turno_IdTurno] int  NOT NULL,
                                      [IsStraordinario] bit  NOT NULL
);


-- Creating table 'RichiestaSets'
CREATE TABLE [dbo].[RichiestaSets] (
                                       [IdRichiesta] int IDENTITY(1,1) NOT NULL,
                                       [Turno_IdTurno] int  NOT NULL,
                                       [Giorno_IdGiorno] int  NOT NULL,
                                       [RichiestaTeorica_IdRichiestaTeorica] int  NOT NULL
);


-- Creating table 'RichiestaTeoricaSets'
CREATE TABLE [dbo].[RichiestaTeoricaSets] (
                                              [IdRichiestaTeorica] int IDENTITY(1,1) NOT NULL,
                                              [DataInizio] datetime  NOT NULL,
                                              [DataFine] datetime  NULL,
                                              [TerminalSetIdTerminale] int  NOT NULL
);


-- Creating table 'RisultatoSets'
CREATE TABLE [dbo].[RisultatoSets] (
                                       [IdRisultato] int IDENTITY(1,1) NOT NULL,
                                       [Data] datetime  NOT NULL,
                                       [Persone_Matricola] int  NOT NULL,
                                       [Turno_IdTurno] int  NOT NULL
);


-- Creating table 'SettimaneSets'
CREATE TABLE [dbo].[SettimaneSets] (
                                       [IdSettimane] int IDENTITY(1,1) NOT NULL,
                                       [ParametriSetIdParametri] int  NOT NULL
);


-- Creating table 'StipendioSets'
CREATE TABLE [dbo].[StipendioSets] (
                                       [IdStipendio] int IDENTITY(1,1) NOT NULL,
                                       [Data] datetime  NOT NULL,
                                       [Valore] float  NOT NULL,
                                       [PersoneSet_Matricola] int  NOT NULL
);


-- Creating table 'StoricoContrattoSets'
CREATE TABLE [dbo].[StoricoContrattoSets] (
                                              [IdStoricoContratto] int IDENTITY(1,1) NOT NULL,
                                              [DataInizio] datetime  NOT NULL,
                                              [DataFine] datetime  NULL,
                                              [Persone_Matricola] int  NOT NULL,
                                              [Contrato_IdContratto] int  NOT NULL,
                                              [Turno_IdTurno] int  NULL,
                                              [Turno1_IdTurno] int  NULL
);


-- Creating table 'sysdiagrams'
CREATE TABLE [dbo].[sysdiagrams] (
                                     [name] nvarchar(128)  NOT NULL,
                                     [principal_id] int  NOT NULL,
                                     [diagram_id] int IDENTITY(1,1) NOT NULL,
                                     [version] int  NULL,
                                     [definition] varbinary(max)  NULL
);


-- Creating table 'TerminalSets'
CREATE TABLE [dbo].[TerminalSets] (
                                      [IdTerminale] int IDENTITY(1,1) NOT NULL,
                                      [NomeTerminale] nvarchar(max)  NOT NULL,
                                      [ZonaIdZona] int  NOT NULL
);


-- Creating table 'TurnoSets'
CREATE TABLE [dbo].[TurnoSets] (
                                   [IdTurno] int IDENTITY(1,1) NOT NULL,
                                   [NomeTurno] nvarchar(max)  NOT NULL,
                                   [Paga] float  NOT NULL,
                                   [FasciaOraria] nvarchar(max)  NOT NULL
);


-- Creating table 'ZonaSets'
CREATE TABLE [dbo].[ZonaSets] (
                                  [IdZona] int IDENTITY(1,1) NOT NULL,
                                  [Zone] nvarchar(max)  NOT NULL
);


-- Creating table 'database_firewall_rules2'
CREATE TABLE [dbo].[database_firewall_rules2] (
                                                  [id] int IDENTITY(1,1) NOT NULL,
                                                  [name] nvarchar(128)  NOT NULL,
                                                  [start_ip_address] varchar(45)  NOT NULL,
                                                  [end_ip_address] varchar(45)  NOT NULL,
                                                  [create_date] datetime  NOT NULL,
                                                  [modify_date] datetime  NOT NULL
);


-- --------------------------------------------------
-- Creating all PRIMARY KEY constraints
-- --------------------------------------------------

-- Creating primary key on [IdAssenza] in table 'AssenzaSets'
ALTER TABLE [dbo].[AssenzaSets]
    ADD CONSTRAINT [PK_AssenzaSets]
        PRIMARY KEY CLUSTERED ([IdAssenza] ASC);


-- Creating primary key on [IdContratto] in table 'ContratoeSets'
ALTER TABLE [dbo].[ContratoeSets]
    ADD CONSTRAINT [PK_ContratoeSets]
        PRIMARY KEY CLUSTERED ([IdContratto] ASC);


-- Creating primary key on [id], [name], [start_ip_address], [end_ip_address], [create_date], [modify_date] in table 'database_firewall_rules'
ALTER TABLE [dbo].[database_firewall_rules]
    ADD CONSTRAINT [PK_database_firewall_rules]
        PRIMARY KEY CLUSTERED ([id], [name], [start_ip_address], [end_ip_address], [create_date], [modify_date] ASC);


-- Creating primary key on [id], [name], [start_ip_address], [end_ip_address], [create_date], [modify_date] in table 'database_firewall_rules1'
ALTER TABLE [dbo].[database_firewall_rules1]
    ADD CONSTRAINT [PK_database_firewall_rules1]
        PRIMARY KEY CLUSTERED ([id], [name], [start_ip_address], [end_ip_address], [create_date], [modify_date] ASC);


-- Creating primary key on [IdDisponibilitaStraordinario] in table 'DisponibilitaStraordinarioSets'
ALTER TABLE [dbo].[DisponibilitaStraordinarioSets]
    ADD CONSTRAINT [PK_DisponibilitaStraordinarioSets]
        PRIMARY KEY CLUSTERED ([IdDisponibilitaStraordinario] ASC);


-- Creating primary key on [IdSettimana] in table 'GiornoInSettimanaSets'
ALTER TABLE [dbo].[GiornoInSettimanaSets]
    ADD CONSTRAINT [PK_GiornoInSettimanaSets]
        PRIMARY KEY CLUSTERED ([IdSettimana] ASC);


-- Creating primary key on [IdGiorno] in table 'GiornoSets'
ALTER TABLE [dbo].[GiornoSets]
    ADD CONSTRAINT [PK_GiornoSets]
        PRIMARY KEY CLUSTERED ([IdGiorno] ASC);


-- Creating primary key on [IdGrupoTerminale] in table 'GruppiTerminaliSets'
ALTER TABLE [dbo].[GruppiTerminaliSets]
    ADD CONSTRAINT [PK_GruppiTerminaliSets]
        PRIMARY KEY CLUSTERED ([IdGrupoTerminale] ASC);


-- Creating primary key on [IdParametri] in table 'ParametriSets'
ALTER TABLE [dbo].[ParametriSets]
    ADD CONSTRAINT [PK_ParametriSets]
        PRIMARY KEY CLUSTERED ([IdParametri] ASC);


-- Creating primary key on [Matricola] in table 'PersoneSets'
ALTER TABLE [dbo].[PersoneSets]
    ADD CONSTRAINT [PK_PersoneSets]
        PRIMARY KEY CLUSTERED ([Matricola] ASC);


-- Creating primary key on [IdPresenza] in table 'PresenzaSets'
ALTER TABLE [dbo].[PresenzaSets]
    ADD CONSTRAINT [PK_PresenzaSets]
        PRIMARY KEY CLUSTERED ([IdPresenza] ASC);


-- Creating primary key on [IdRichiesta] in table 'RichiestaSets'
ALTER TABLE [dbo].[RichiestaSets]
    ADD CONSTRAINT [PK_RichiestaSets]
        PRIMARY KEY CLUSTERED ([IdRichiesta] ASC);


-- Creating primary key on [IdRichiestaTeorica] in table 'RichiestaTeoricaSets'
ALTER TABLE [dbo].[RichiestaTeoricaSets]
    ADD CONSTRAINT [PK_RichiestaTeoricaSets]
        PRIMARY KEY CLUSTERED ([IdRichiestaTeorica] ASC);


-- Creating primary key on [IdRisultato] in table 'RisultatoSets'
ALTER TABLE [dbo].[RisultatoSets]
    ADD CONSTRAINT [PK_RisultatoSets]
        PRIMARY KEY CLUSTERED ([IdRisultato] ASC);


-- Creating primary key on [IdSettimane] in table 'SettimaneSets'
ALTER TABLE [dbo].[SettimaneSets]
    ADD CONSTRAINT [PK_SettimaneSets]
        PRIMARY KEY CLUSTERED ([IdSettimane] ASC);


-- Creating primary key on [IdStipendio] in table 'StipendioSets'
ALTER TABLE [dbo].[StipendioSets]
    ADD CONSTRAINT [PK_StipendioSets]
        PRIMARY KEY CLUSTERED ([IdStipendio] ASC);


-- Creating primary key on [IdStoricoContratto] in table 'StoricoContrattoSets'
ALTER TABLE [dbo].[StoricoContrattoSets]
    ADD CONSTRAINT [PK_StoricoContrattoSets]
        PRIMARY KEY CLUSTERED ([IdStoricoContratto] ASC);


-- Creating primary key on [diagram_id] in table 'sysdiagrams'
ALTER TABLE [dbo].[sysdiagrams]
    ADD CONSTRAINT [PK_sysdiagrams]
        PRIMARY KEY CLUSTERED ([diagram_id] ASC);


-- Creating primary key on [IdTerminale] in table 'TerminalSets'
ALTER TABLE [dbo].[TerminalSets]
    ADD CONSTRAINT [PK_TerminalSets]
        PRIMARY KEY CLUSTERED ([IdTerminale] ASC);


-- Creating primary key on [IdTurno] in table 'TurnoSets'
ALTER TABLE [dbo].[TurnoSets]
    ADD CONSTRAINT [PK_TurnoSets]
        PRIMARY KEY CLUSTERED ([IdTurno] ASC);


-- Creating primary key on [IdZona] in table 'ZonaSets'
ALTER TABLE [dbo].[ZonaSets]
    ADD CONSTRAINT [PK_ZonaSets]
        PRIMARY KEY CLUSTERED ([IdZona] ASC);


-- Creating primary key on [id], [name], [start_ip_address], [end_ip_address], [create_date], [modify_date] in table 'database_firewall_rules2'
ALTER TABLE [dbo].[database_firewall_rules2]
    ADD CONSTRAINT [PK_database_firewall_rules2]
        PRIMARY KEY CLUSTERED ([id], [name], [start_ip_address], [end_ip_address], [create_date], [modify_date] ASC);


-- --------------------------------------------------
-- Creating all FOREIGN KEY constraints
-- --------------------------------------------------

-- Creating foreign key on [PersoneSet_Matricola] in table 'AssenzaSets'
ALTER TABLE [dbo].[AssenzaSets]
    ADD CONSTRAINT [FK_AssenzaPersoneSet]
        FOREIGN KEY ([PersoneSet_Matricola])
            REFERENCES [dbo].[PersoneSets]
                ([Matricola])
            ON DELETE CASCADE ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_AssenzaPersoneSet'
CREATE INDEX [IX_FK_AssenzaPersoneSet]
    ON [dbo].[AssenzaSets]
        ([PersoneSet_Matricola]);


-- Creating foreign key on [Contrato_IdContratto] in table 'StoricoContrattoSets'
ALTER TABLE [dbo].[StoricoContrattoSets]
    ADD CONSTRAINT [FK_StoricoContrattoContrato]
        FOREIGN KEY ([Contrato_IdContratto])
            REFERENCES [dbo].[ContratoeSets]
                ([IdContratto])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_StoricoContrattoContrato'
CREATE INDEX [IX_FK_StoricoContrattoContrato]
    ON [dbo].[StoricoContrattoSets]
        ([Contrato_IdContratto]);


-- Creating foreign key on [DisponibilitaStraordinarioSetIdDisponibilitaStraordinario] in table 'PersoneSets'
ALTER TABLE [dbo].[PersoneSets]
    ADD CONSTRAINT [FK_DisponibilitaStraordinarioPersoneSet]
        FOREIGN KEY ([DisponibilitaStraordinarioSetIdDisponibilitaStraordinario])
            REFERENCES [dbo].[DisponibilitaStraordinarioSets]
                ([IdDisponibilitaStraordinario])
            ON DELETE CASCADE ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_DisponibilitaStraordinarioPersoneSet'
CREATE INDEX [IX_FK_DisponibilitaStraordinarioPersoneSet]
    ON [dbo].[PersoneSets]
        ([DisponibilitaStraordinarioSetIdDisponibilitaStraordinario]);


-- Creating foreign key on [GiornoIdGiorno] in table 'GiornoInSettimanaSets'
ALTER TABLE [dbo].[GiornoInSettimanaSets]
    ADD CONSTRAINT [FK_GiornoSettimana]
        FOREIGN KEY ([GiornoIdGiorno])
            REFERENCES [dbo].[GiornoSets]
                ([IdGiorno])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_GiornoSettimana'
CREATE INDEX [IX_FK_GiornoSettimana]
    ON [dbo].[GiornoInSettimanaSets]
        ([GiornoIdGiorno]);


-- Creating foreign key on [Turno_IdTurno] in table 'GiornoInSettimanaSets'
ALTER TABLE [dbo].[GiornoInSettimanaSets]
    ADD CONSTRAINT [FK_SettimanaTurno]
        FOREIGN KEY ([Turno_IdTurno])
            REFERENCES [dbo].[TurnoSets]
                ([IdTurno])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_SettimanaTurno'
CREATE INDEX [IX_FK_SettimanaTurno]
    ON [dbo].[GiornoInSettimanaSets]
        ([Turno_IdTurno]);


-- Creating foreign key on [SettimaneIdSettimane] in table 'GiornoInSettimanaSets'
ALTER TABLE [dbo].[GiornoInSettimanaSets]
    ADD CONSTRAINT [FK_SettimaneSettimanaSet]
        FOREIGN KEY ([SettimaneIdSettimane])
            REFERENCES [dbo].[SettimaneSets]
                ([IdSettimane])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_SettimaneSettimanaSet'
CREATE INDEX [IX_FK_SettimaneSettimanaSet]
    ON [dbo].[GiornoInSettimanaSets]
        ([SettimaneIdSettimane]);


-- Creating foreign key on [Giorno_IdGiorno] in table 'RichiestaSets'
ALTER TABLE [dbo].[RichiestaSets]
    ADD CONSTRAINT [FK_RichiestaGiorno]
        FOREIGN KEY ([Giorno_IdGiorno])
            REFERENCES [dbo].[GiornoSets]
                ([IdGiorno])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_RichiestaGiorno'
CREATE INDEX [IX_FK_RichiestaGiorno]
    ON [dbo].[RichiestaSets]
        ([Giorno_IdGiorno]);


-- Creating foreign key on [ParametriIdParametri] in table 'GruppiTerminaliSets'
ALTER TABLE [dbo].[GruppiTerminaliSets]
    ADD CONSTRAINT [FK_ParametriGruppiTerminali]
        FOREIGN KEY ([ParametriIdParametri])
            REFERENCES [dbo].[ParametriSets]
                ([IdParametri])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_ParametriGruppiTerminali'
CREATE INDEX [IX_FK_ParametriGruppiTerminali]
    ON [dbo].[GruppiTerminaliSets]
        ([ParametriIdParametri]);


-- Creating foreign key on [ParametriSetIdParametri] in table 'SettimaneSets'
ALTER TABLE [dbo].[SettimaneSets]
    ADD CONSTRAINT [FK_ParametriSetSettimane]
        FOREIGN KEY ([ParametriSetIdParametri])
            REFERENCES [dbo].[ParametriSets]
                ([IdParametri])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_ParametriSetSettimane'
CREATE INDEX [IX_FK_ParametriSetSettimane]
    ON [dbo].[SettimaneSets]
        ([ParametriSetIdParametri]);


-- Creating foreign key on [Persone_Matricola] in table 'PresenzaSets'
ALTER TABLE [dbo].[PresenzaSets]
    ADD CONSTRAINT [FK_PresenzaPersone]
        FOREIGN KEY ([Persone_Matricola])
            REFERENCES [dbo].[PersoneSets]
                ([Matricola])
            ON DELETE CASCADE ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_PresenzaPersone'
CREATE INDEX [IX_FK_PresenzaPersone]
    ON [dbo].[PresenzaSets]
        ([Persone_Matricola]);


-- Creating foreign key on [Persone_Matricola] in table 'RisultatoSets'
ALTER TABLE [dbo].[RisultatoSets]
    ADD CONSTRAINT [FK_RisultatoPersone]
        FOREIGN KEY ([Persone_Matricola])
            REFERENCES [dbo].[PersoneSets]
                ([Matricola])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_RisultatoPersone'
CREATE INDEX [IX_FK_RisultatoPersone]
    ON [dbo].[RisultatoSets]
        ([Persone_Matricola]);


-- Creating foreign key on [PersoneSet_Matricola] in table 'StipendioSets'
ALTER TABLE [dbo].[StipendioSets]
    ADD CONSTRAINT [FK_StipendioPersoneSet]
        FOREIGN KEY ([PersoneSet_Matricola])
            REFERENCES [dbo].[PersoneSets]
                ([Matricola])
            ON DELETE CASCADE ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_StipendioPersoneSet'
CREATE INDEX [IX_FK_StipendioPersoneSet]
    ON [dbo].[StipendioSets]
        ([PersoneSet_Matricola]);


-- Creating foreign key on [Persone_Matricola] in table 'StoricoContrattoSets'
ALTER TABLE [dbo].[StoricoContrattoSets]
    ADD CONSTRAINT [FK_StoricoContrattoPersone]
        FOREIGN KEY ([Persone_Matricola])
            REFERENCES [dbo].[PersoneSets]
                ([Matricola])
            ON DELETE CASCADE ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_StoricoContrattoPersone'
CREATE INDEX [IX_FK_StoricoContrattoPersone]
    ON [dbo].[StoricoContrattoSets]
        ([Persone_Matricola]);


-- Creating foreign key on [Terminale_IdTerminale] in table 'PersoneSets'
ALTER TABLE [dbo].[PersoneSets]
    ADD CONSTRAINT [FK_TerminalePersone]
        FOREIGN KEY ([Terminale_IdTerminale])
            REFERENCES [dbo].[TerminalSets]
                ([IdTerminale])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_TerminalePersone'
CREATE INDEX [IX_FK_TerminalePersone]
    ON [dbo].[PersoneSets]
        ([Terminale_IdTerminale]);


-- Creating foreign key on [Turno_IdTurno] in table 'PresenzaSets'
ALTER TABLE [dbo].[PresenzaSets]
    ADD CONSTRAINT [FK_PresenzaTurno]
        FOREIGN KEY ([Turno_IdTurno])
            REFERENCES [dbo].[TurnoSets]
                ([IdTurno])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_PresenzaTurno'
CREATE INDEX [IX_FK_PresenzaTurno]
    ON [dbo].[PresenzaSets]
        ([Turno_IdTurno]);


-- Creating foreign key on [RichiestaTeorica_IdRichiestaTeorica] in table 'RichiestaSets'
ALTER TABLE [dbo].[RichiestaSets]
    ADD CONSTRAINT [FK_RichiestaTeoricaRichiesta]
        FOREIGN KEY ([RichiestaTeorica_IdRichiestaTeorica])
            REFERENCES [dbo].[RichiestaTeoricaSets]
                ([IdRichiestaTeorica])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_RichiestaTeoricaRichiesta'
CREATE INDEX [IX_FK_RichiestaTeoricaRichiesta]
    ON [dbo].[RichiestaSets]
        ([RichiestaTeorica_IdRichiestaTeorica]);


-- Creating foreign key on [Turno_IdTurno] in table 'RichiestaSets'
ALTER TABLE [dbo].[RichiestaSets]
    ADD CONSTRAINT [FK_RichiestaTurno]
        FOREIGN KEY ([Turno_IdTurno])
            REFERENCES [dbo].[TurnoSets]
                ([IdTurno])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_RichiestaTurno'
CREATE INDEX [IX_FK_RichiestaTurno]
    ON [dbo].[RichiestaSets]
        ([Turno_IdTurno]);


-- Creating foreign key on [Turno_IdTurno] in table 'RisultatoSets'
ALTER TABLE [dbo].[RisultatoSets]
    ADD CONSTRAINT [FK_TurnoRisultato]
        FOREIGN KEY ([Turno_IdTurno])
            REFERENCES [dbo].[TurnoSets]
                ([IdTurno])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_TurnoRisultato'
CREATE INDEX [IX_FK_TurnoRisultato]
    ON [dbo].[RisultatoSets]
        ([Turno_IdTurno]);


-- Creating foreign key on [Turno1_IdTurno] in table 'StoricoContrattoSets'
ALTER TABLE [dbo].[StoricoContrattoSets]
    ADD CONSTRAINT [FK_StoricoContrattoTurno]
        FOREIGN KEY ([Turno1_IdTurno])
            REFERENCES [dbo].[TurnoSets]
                ([IdTurno])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_StoricoContrattoTurno'
CREATE INDEX [IX_FK_StoricoContrattoTurno]
    ON [dbo].[StoricoContrattoSets]
        ([Turno1_IdTurno]);


-- Creating foreign key on [Turno_IdTurno] in table 'StoricoContrattoSets'
ALTER TABLE [dbo].[StoricoContrattoSets]
    ADD CONSTRAINT [FK_TurnoStoricoContratto]
        FOREIGN KEY ([Turno_IdTurno])
            REFERENCES [dbo].[TurnoSets]
                ([IdTurno])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_TurnoStoricoContratto'
CREATE INDEX [IX_FK_TurnoStoricoContratto]
    ON [dbo].[StoricoContrattoSets]
        ([Turno_IdTurno]);


-- Creating foreign key on [ZonaIdZona] in table 'TerminalSets'
ALTER TABLE [dbo].[TerminalSets]
    ADD CONSTRAINT [FK_ZonaTerminale]
        FOREIGN KEY ([ZonaIdZona])
            REFERENCES [dbo].[ZonaSets]
                ([IdZona])
            ON DELETE CASCADE ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_ZonaTerminale'
CREATE INDEX [IX_FK_ZonaTerminale]
    ON [dbo].[TerminalSets]
        ([ZonaIdZona]);


-- Creating foreign key on [TerminalSetIdTerminale] in table 'RichiestaTeoricaSets'
ALTER TABLE [dbo].[RichiestaTeoricaSets]
    ADD CONSTRAINT [FK_TerminalSetRichiestaTeoricaSet]
        FOREIGN KEY ([TerminalSetIdTerminale])
            REFERENCES [dbo].[TerminalSets]
                ([IdTerminale])
            ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating non-clustered index for FOREIGN KEY 'FK_TerminalSetRichiestaTeoricaSet'
CREATE INDEX [IX_FK_TerminalSetRichiestaTeoricaSet]
    ON [dbo].[RichiestaTeoricaSets]
        ([TerminalSetIdTerminale]);


-- --------------------------------------------------
-- Script has ended
-- --------------------------------------------------