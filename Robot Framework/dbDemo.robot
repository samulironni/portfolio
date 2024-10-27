*** Settings ***
Library    DatabaseLibrary
Library    OperatingSystem
Library    String
Library    Collections
Library    env_loader.py

*** Variables ***
${DBNAME}    ${ENV:DBNAME}
${DBUSER}    ${ENV:DBUSER}
${DBPASS}    ${ENV:DBPASS}
${DBHOST}    ${ENV:DBHOST}
${DBPORT}    ${ENV:DBPORT}
${path}    C:/Module 1 2023/Ohjelmistotestaus/

*** Keywords ***
Make Connection
    [Arguments]    ${dbtoconnect}
    Connect To Database    pymysql    ${dbtoconnect}    ${dbuser}    ${dbpass}    ${dbhost}    ${dbport}

*** Test Cases ***
Create test Database
    Make Connection    mysql
    Execute Sql String    drop database if exists ${dbname};
    Execute Sql String    create database ${dbname};
    Execute Sql String    use ${dbname};

    ${GetCommands}=    Get File    ${path}Takkula_luonti.txt
    @{CreateCommands}=    Split String    ${GetCommands}    ;
    Log    ${GetCommands}
    ${TableCount}=    Get Length    ${CreateCommands}
    ${IndexToBeRemoved}=    Evaluate    ${TableCount}-1
    Remove From List    ${CreateCommands}    ${IndexToBeRemoved}
    Log    ${CreateCommands}
    ${TableCount}=    Get Length    ${CreateCommands}

    FOR    ${index}    IN RANGE   ${TableCount}
        Log    ${index}
        Execute Sql String    ${CreateCommands}[${index}]
    END
    
    Set Global Variable    ${TableCount}    
    
*** Test Cases ***
Insert data into tables
    Make Connection    ${dbname}
    
    ${GetCommands}=    Get File    ${path}takkula_tiedon_lisays.txt
    @{InsertCommands}=    Split String    ${GetCommands}    ;
    Log    ${InsertCommands}
    ${InsertCount}=    Get Length    ${InsertCommands}
    ${IndexToBeRemoved}=    Evaluate    ${InsertCount}-1
    Remove From List    ${InsertCommands}    ${IndexToBeRemoved}
    Log    ${InsertCommands}
    ${InsertCount}=    Get Length    ${InsertCommands}

    FOR    ${index}    IN RANGE   ${InsertCount}
        Log    ${index}
        Execute Sql String    ${InsertCommands}[${index}]
    END

*** Test Cases ***
Check tables
    Make Connection    ${dbname}
    ${databaseTables}=    Query    show tables;
    ${countOfTables}=    Get Length    ${databaseTables}

    Should Be Equal    ${countOfTables}    ${TableCount}

    FOR    ${index}    IN RANGE    ${TableCount}
        Log    ${index}
        ${table}=    Set Variable    ${databaseTables}[${index}]
        Log    ${table}
        ${table}=    Convert To String    ${table}
        ${table}=    Remove String    ${table}    ,    (    )    '

        Table Must Exist    ${table}
    END

*** Test Cases ***
Insert data into table opettaja

    Make connection    ${dbname}
    Execute Sql String    use ${dbname};
    Execute Sql String    insert into opettaja(opettajanro, etunimi, sukunimi, palkka, puhelin, syntpvm) values ('h123', 'Tero', 'Testi', '1234', '041-567890', '1999-09-09'); 
