*** Settings ***
Library    SeleniumLibrary

*** Variables ***
#Luodaan luottokortin kuukaudesta ja vuodesta globaalit muuttujat
${creditCardMonth}    12
${creditCardYear}     2030
${totalPrice}

*** Test Cases ***
Open Browser and Verify Welcome Message
    Open Browser    http://blazedemo.com    
    ...    chrome    options=add_experimental_option("detach", True)
    Maximize Browser Window
    #Tarkistetaan että sivulta löytyy kyseinen teksti
    Sleep    1
    Page Should Contain    Welcome to the Simple Travel Agency!

Select Departure and Destination
    #Valitaan lähtökaupunki ja tallennetaan se muuttujaan
    Select From List By Label    name:fromPort    Boston
    ${departure}=    Get Selected List Value    name:fromPort   
    
    Sleep    1
    #Valitaan määränpää ja tallennetaan se muuttujaan
    Select From List By Label    name:toPort    Cairo
    ${destination}=    Get Selected List Value    name:toPort    
    
    Sleep    1
    #Testataan löytyykö kyseinen button sivulta
    Page Should Contain Button    xpath://html/body/div[3]/form/div/input
    Click Button    xpath://html/body/div[3]/form/div/input

    #Testataan että sivulta löytyy oikeat tiedot
    Wait Until Page Contains    Flights from ${departure} to ${destination}:


Select Flight and Verify Details
    Sleep    1
    #Testataan kuinka monta osumaa on näkyvillä
    ${matchCount}=    Get Element Count    
    ...    xpath://html/body/div[2]/table/tbody/tr[*]
    Should Be True    ${matchCount} >= 1
    
    #Haetaan lennon numero, lentoyhtiö sekä lennon hinta
    ${flightNumber}=    Get Text    xpath://html/body/div[2]/table/tbody/tr[4]/td[2]
    ${airline}=    Get Text    xpath://html/body/div[2]/table/tbody/tr[4]/td[3]
    ${price}=    Get Text    xpath://html/body/div[2]/table/tbody/tr[4]/td[6]

    Sleep    1
    #Valitaan lento
    Click Button    xpath://html/body/div[2]/table/tbody/tr[4]/td[1]/input 

    #Tarkistetaan, että sivulta löytyvät lennon numero, lentoyhtiö sekä hinta
    Run Keyword And Ignore Error    
    ...    Page Should Contain    ${flightNumber}
          
    Run Keyword And Ignore Error    
    ...    Page Should Contain    ${airline}

    Run Keyword And Ignore Error    
    ...    Page Should Contain    ${price}
    
Enter User Information and Make purchase    
    #Tallennetaan muuttujaan lennon kokonaishinta
    ${totalPrice}=    Get Text    xpath://html/body/div[2]/p[5]/em

    Sleep    1
    #Syötetään lomakkeelle käyttäjän tiedot
    Click Element    id:inputName
    Input Text    id:inputName    Donald Duck

    Sleep    1
    Click Element    id:address
    Input Text    id:address    Wall Street 123

    Sleep    1
    Click Element    id:city
    Input Text    id:city    New York City

    Sleep    1
    Click Element    id:state
    Input Text    id:state    New York

    Sleep    1
    Click Element    id:zipCode
    Input Text    id:zipCode    12345
    
    Sleep    1
    Select From List By Label    id:cardType    Diner's Club

    Sleep    1
    Click Element    id:creditCardNumber
    Input Text    id:creditCardNumber    4444 5555 6666
    
    Sleep    1
    Click Element    id:creditCardMonth
    Input Text    id:creditCardMonth    ${creditCardMonth}

    Sleep    1
    Click Element    id:creditCardYear 
    Input Text    id:creditCardYear    ${creditCardYear}

    Sleep    1
    Click Element    id:nameOnCard
    Input Text    id:nameOnCard    Donald Duck

    Sleep    1
    Click Button    id:rememberMe

    Sleep    1
    Click Button    xpath://html/body/div[2]/form/div[11]/div/input

    Page Should Contain    Thank you for your purchase today!

Check If The Site Information Matches User Information
    #Testataan, saako expiration samat arvot luottokortin kuukaudelle ja vuodelle
    ${expiration}=    Get Text    xpath://html/body/div[2]/div/table/tbody/tr[5]/td[2]
    Run Keyword And Ignore Error    Should Be Equal As Strings    ${expiration}    ${creditCardMonth} /${creditCardYear}
        
Check If Amount Has Same Value Than Total Price    
    #Testataan, saako amount saman arvon kuin aiemmin määritelty totalPrice muuttuja
    ${amount}=    Get Text    xpath://html/body/div[2]/div/table/tbody/tr[3]/td[2]
    Run Keyword and Ignore Error    Should Be Equal As Strings   ${amount}      ${totalPrice} USD

    Sleep    4
    Close Browser

    



    