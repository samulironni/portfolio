*** Settings ***
Library    OperatingSystem
Library    String
Library    Collections

*** Test Cases ***
Read webpages_txt 
    ${path}    Set Variable    C:/Module 1 2023/Ohjelmistotestaus/RobotFiles/RobotTesting
    ${file_contents}    Get File    ${path}/webpages.txt
    ${addresses}=    Split String    ${file_contents}
    Log    Contents of webpages file:\n${addresses}
    Set Global Variable    ${addresses}
   
*** Test Cases ***
Create file
    ${path}=    Set Variable    C:/Module 1 2023/Ohjelmistotestaus/RobotFiles/RobotTesting
    Create File    ${path}/ipaddresses.txt
    File Should Exist    ${path}/ipaddresses.txt
    File Should Be Empty    ${path}/ipaddresses.txt

*** Test Cases ***
Calculate IP addresses
    ${path}=    Set Variable    C:/Module 1 2023/Ohjelmistotestaus/RobotFiles/RobotTesting
     ${count}=    Get Length    ${addresses}
    
    FOR    ${address}    IN RANGE    ${count}
    ${output}=    Run And Return Rc And Output    ping ${addresses}[${address}] 
    @{wordlist}=    Split String    ${output}[1]

    ${index}=    Get Index From List    ${wordlist}    Pinging
    ${index}=    Evaluate    ${index}+2
    ${IP_Address}=    Set Variable    ${wordlist}[${index}]

    ${index2}=    Get Index From List    ${wordlist}    Average
    ${index2}=    Evaluate    ${index2}+2
    ${Average}=    Set Variable    ${wordlist}[${index2}]

    ${AverageNumber}=    Set Variable    ${Average}[:-2]
    ${AverageNumberOnly}=    Convert To Number    ${AverageNumber}

    Should Be True    ${AverageNumberOnly} < 50.0

    Append To File    ${path}/ipaddresses.txt    IP Address: ${IP_Address}, Average: ${Average}\n 
   
    END
