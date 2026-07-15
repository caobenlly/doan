*------------------------------------------------------------------------------
* BAI 4 - HUNGNH.CUST.ACCT.LIST
*------------------------------------------------------------------------------
* Input  : Y.CUST.ID  (ma khach hang - gan gia tri truoc khi CALL)
* Output : Y.OUT      (moi dong 1 tai khoan, phan cach @FM)
*          Vi tri 1 : VN.FULL.NAME
*          Vi tri 2 : NATIONAL.ID
*          Vi tri 3 : So tai khoan
*          Vi tri 4 : WORKING.BALANCE
*          Vi tri 5 : ACCOUNT.OFFICER
*
* Logic  : CUSTOMER -> CUSTOMER.ACCOUNT -> ACCOUNT
* CUSTOMER.ACCOUNT khong can file layout (theo de bai)
*------------------------------------------------------------------------------
SUBROUTINE HUNGNH.CUST.ACCT.LIST
*------------------------------------------------------------------------------
    $INSERT I_COMMON
    $INSERT I_EQUATE
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

    FN.CUST.ACCT = 'F.CUSTOMER.ACCOUNT'
    F.CUST.ACCT = ''
    CALL OPF(FN.CUST.ACCT, F.CUST.ACCT)

    FN.ACCOUNT = 'F.ACCOUNT'
    F.ACCOUNT = ''
    CALL OPF(FN.ACCOUNT, F.ACCOUNT)

    Y.OUT = ''

    RETURN
*------------------------------------------------------------------------------
PROCESS:
*------------------------------------------------------------------------------
    IF Y.CUST.ID EQ '' THEN
        RETURN
    END

*   --- 1. Doc CUSTOMER ---
    CALL F.READ(FN.CUSTOMER, F.CUSTOMER, Y.CUST.ID, R.CUSTOMER, F.CUS.ERR, '')
    IF F.CUS.ERR THEN
        RETURN
    END

*   VN.FULL.NAME - uu tien LOCAL.REF, neu khong co thi lay SHORT.NAME
    Y.VN.NAME = R.CUSTOMER<EB.CUS.SHORT.NAME>
    IF R.CUSTOMER<EB.CUS.LOCAL.REF, 1> NE '' THEN
        Y.VN.NAME = R.CUSTOMER<EB.CUS.LOCAL.REF, 1>
    END

    Y.NAT.ID = R.CUSTOMER<EB.CUS.LEGAL.ID>

*   --- 2. Doc CUSTOMER.ACCOUNT (key = ma KH) ---
    CALL F.READ(FN.CUST.ACCT, F.CUST.ACCT, Y.CUST.ID, R.CUST.ACCT, F.CA.ERR, '')
    IF F.CA.ERR THEN
        RETURN
    END

    Y.NO.ACCT = DCOUNT(R.CUST.ACCT<EB.CUSTOMER.ACCOUNT.ACCOUNT>, @VM)

    FOR Y.I = 1 TO Y.NO.ACCT
        Y.ACCT.NO = R.CUST.ACCT<EB.CUSTOMER.ACCOUNT.ACCOUNT, Y.I>
        IF Y.ACCT.NO EQ '' THEN
            GOTO NEXT.ACCT
        END

*       --- 3. Doc ACCOUNT ---
        CALL F.READ(FN.ACCOUNT, F.ACCOUNT, Y.ACCT.NO, R.ACCOUNT, F.ACC.ERR, '')
        IF F.ACC.ERR THEN
            GOTO NEXT.ACCT
        END

        Y.WORK.BAL = R.ACCOUNT<AC.WORKING.BALANCE>
        Y.ACCT.OFF = R.ACCOUNT<AC.ACCOUNT.OFFICER>

        Y.OUT<-1> = Y.VN.NAME : @FM : Y.NAT.ID : @FM : Y.ACCT.NO : @FM : Y.WORK.BAL : @FM : Y.ACCT.OFF

NEXT.ACCT:
    NEXT Y.I

    RETURN
*------------------------------------------------------------------------------
END
