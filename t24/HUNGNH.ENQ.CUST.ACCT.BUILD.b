$PACKAGE L3.VmbTemplateRoutines4
*------------------------------------------------------------------------------
* Bai 4 - NOFILE routine (RTN.CALL)
* Tra ve RETURN.ARRAY, moi dong: ten*cmnd*soTK*soDu*AO
*------------------------------------------------------------------------------
SUBROUTINE HUNGNH.ENQ.CUST.ACCT.BUILD(RETURN.ARRAY)
*------------------------------------------------------------------------------
    $INSERT I_COMMON
    $INSERT I_EQUATE
    $INSERT I_ENQUIRY.COMMON

    RETURN.ARRAY = ''
    Y.CUST.ID = ''

    LOCATE 'CUSTOMER.ID' IN D.FIELDS<1> SETTING Y.SEL.POS THEN
        Y.CUST.ID = D.RANGE.AND.VALUE<Y.SEL.POS>
    END

    CALL HUNGNH.CUST.ACCT.LIST(Y.CUST.ID, Y.OUT)

    NOREC = DCOUNT(Y.OUT, @FM)
    IF NOREC LT 1 THEN
        RETURN
    END

    FOR I = 1 TO NOREC
        Y.LINE = Y.OUT<I, 1> : "*" : Y.OUT<I, 2> : "*" : Y.OUT<I, 3> : "*" : Y.OUT<I, 4> : "*" : Y.OUT<I, 5>
        RETURN.ARRAY<-1> = Y.LINE
    NEXT I

    RETURN
*------------------------------------------------------------------------------
END
