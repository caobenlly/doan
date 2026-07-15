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

    CALL HUNGNH.CUST.ACCT.LIST

    NOREC = DCOUNT(Y.OUT, @FM)
    IF NOREC LT 1 THEN
        RETURN
    END

    FOR I = 1 TO NOREC
        Y.LINE = FIELD(Y.OUT<I>, @FM, 1) : "*" : FIELD(Y.OUT<I>, @FM, 2) : "*" : FIELD(Y.OUT<I>, @FM, 3) : "*" : FIELD(Y.OUT<I>, @FM, 4) : "*" : FIELD(Y.OUT<I>, @FM, 5)
        RETURN.ARRAY<-1> = Y.LINE
    NEXT I

    RETURN
*------------------------------------------------------------------------------
END
