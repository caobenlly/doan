$PACKAGE L3.VmbTemplateRoutines4
*------------------------------------------------------------------------------
* Bai 4 - Xuat danh sach tai khoan cua khach hang
* Input : Ma khach hang (tu enquiry selection)
* Output: VN.FULL.NAME, NATIONAL.ID, So TK, Working Balance, Account Officer
*------------------------------------------------------------------------------
SUBROUTINE HUNGNH.CUST.ACCT.LIST
*------------------------------------------------------------------------------
    $INSERT I_COMMON
    $INSERT I_EQUATE
    $INSERT I_ENQUIRY.COMMON
    $INSERT I_F.CUSTOMER
    $INSERT I_F.ACCOUNT
    $INSERT I_F.CUSTOMER.ACCOUNT

    GOSUB INITIALISE
    GOSUB PROCESS

    RETURN
*------------------------------------------------------------------------------
INITIALISE:
*------------------------------------------------------------------------------
    FN.CUSTOMER = 'F.CUSTOMER'
    F.CUSTOMER = ''
    CALL OPF(FN.CUSTOMER, F.CUSTOMER)

    FN.ACCOUNT = 'F.ACCOUNT'
    F.ACCOUNT = ''
    CALL OPF(FN.ACCOUNT, F.ACCOUNT)

    FN.CUST.ACCT = 'F.CUSTOMER.ACCOUNT'
    F.CUST.ACCT = ''
    CALL OPF(FN.CUST.ACCT, F.CUST.ACCT)

    Y.OUT = ''
    Y.CUST.ID = ''

    RETURN
*------------------------------------------------------------------------------
PROCESS:
*------------------------------------------------------------------------------
*   Lay ma KH tu selection cua enquiry (NOFILE)
    LOCATE 'CUSTOMER.ID' IN D.FIELDS<1> SETTING Y.SEL.POS THEN
        Y.CUST.ID = D.RANGE.AND.VALUE<Y.SEL.POS>
    END ELSE
        IF D.RANGE.AND.VALUE<1> NE '' THEN
            Y.CUST.ID = D.RANGE.AND.VALUE<1>
        END
    END

    IF Y.CUST.ID EQ '' THEN
        RETURN
    END

*   --- Doc CUSTOMER ---
    CALL F.READ(FN.CUSTOMER, F.CUSTOMER, Y.CUST.ID, R.CUSTOMER, F.CUS.ERR, '')
    IF F.CUS.ERR THEN
        RETURN
    END

*   VN.FULL.NAME - thuong la LOCAL.REF hoac field VN tren CUSTOMER
*   Neu lab khac ten field, doi lai vi tri cho dung STANDARD.SELECTION
    Y.VN.NAME = R.CUSTOMER<EB.CUS.SHORT.NAME>
    IF R.CUSTOMER<EB.CUS.LOCAL.REF, 1> NE '' THEN
        Y.VN.NAME = R.CUSTOMER<EB.CUS.LOCAL.REF, 1>
    END

    Y.NAT.ID = R.CUSTOMER<EB.CUS.LEGAL.ID>
    IF Y.NAT.ID EQ '' THEN
        Y.NAT.ID = R.CUSTOMER<EB.CUS.LEGAL.ID, 1>
    END

*   --- Doc CUSTOMER.ACCOUNT ---
    CALL F.READ(FN.CUST.ACCT, F.CUST.ACCT, Y.CUST.ID, R.CUST.ACCT, F.CA.ERR, '')
    IF F.CA.ERR THEN
        RETURN
    END

    Y.NO.ACCT = DCOUNT(R.CUST.ACCT<EB.CUSTOMER.ACCOUNT.ACCOUNT>, @VM)
    IF Y.NO.ACCT LT 1 THEN
        Y.NO.ACCT = DCOUNT(R.CUST.ACCT, @VM)
    END

    FOR Y.I = 1 TO Y.NO.ACCT
        Y.ACCT.NO = R.CUST.ACCT<EB.CUSTOMER.ACCOUNT.ACCOUNT, Y.I>
        IF Y.ACCT.NO EQ '' THEN
            Y.ACCT.NO = R.CUST.ACCT<1, Y.I>
        END
        IF Y.ACCT.NO EQ '' THEN
            CONTINUE
        END

*       --- Doc ACCOUNT ---
        CALL F.READ(FN.ACCOUNT, F.ACCOUNT, Y.ACCT.NO, R.ACCOUNT, F.ACC.ERR, '')
        IF F.ACC.ERR THEN
            CONTINUE
        END

        Y.WORK.BAL = R.ACCOUNT<AC.WORKING.BALANCE>
        Y.ACCT.OFF = R.ACCOUNT<AC.ACCOUNT.OFFICER>

        Y.OUT<-1> = Y.VN.NAME : @FM : Y.NAT.ID : @FM : Y.ACCT.NO : @FM : Y.WORK.BAL : @FM : Y.ACCT.OFF
    NEXT Y.I

    RETURN
*------------------------------------------------------------------------------
END
