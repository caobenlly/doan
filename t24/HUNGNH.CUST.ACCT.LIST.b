*------------------------------------------------------------------------------
* BAI 4 - HUNGNH.CUST.ACCT.LIST
*------------------------------------------------------------------------------
* Input  : Y.CUST.ID  (ma khach hang)
* Output : Y.OUT      (moi attribute la 1 tai khoan;
*                      cac cot trong dong phan cach bang @VM)
*          Vi tri 1 : VN.FULL.NAME
*          Vi tri 2 : NATIONAL.ID
*          Vi tri 3 : So tai khoan
*          Vi tri 4 : WORKING.BALANCE
*          Vi tri 5 : ACCOUNT.OFFICER
*
* Logic  : CUSTOMER -> CUSTOMER.ACCOUNT -> ACCOUNT
* CUSTOMER.ACCOUNT khong can file layout (theo de bai)
*------------------------------------------------------------------------------
SUBROUTINE HUNGNH.CUST.ACCT.LIST(Y.CUST.ID, Y.OUT)
*------------------------------------------------------------------------------
    $INSERT I_COMMON
    $INSERT I_EQUATE
    $INSERT I_F.CUSTOMER
    $INSERT I_F.ACCOUNT

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
    Y.POS.VN.NAME = ''
    Y.POS.NAT.ID = ''

*   Lay vi tri hai local field tren CUSTOMER
    CALL GET.LOC.REF('CUSTOMER', 'VN.FULL.NAME', Y.POS.VN.NAME)
    CALL GET.LOC.REF('CUSTOMER', 'NATIONAL.ID', Y.POS.NAT.ID)

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

*   VN.FULL.NAME va NATIONAL.ID tren local reference cua CUSTOMER
    Y.VN.NAME = R.CUSTOMER<EB.CUS.SHORT.NAME>
    IF Y.POS.VN.NAME NE '' THEN
        IF R.CUSTOMER<EB.CUS.LOCAL.REF, Y.POS.VN.NAME> NE '' THEN
            Y.VN.NAME = R.CUSTOMER<EB.CUS.LOCAL.REF, Y.POS.VN.NAME>
        END
    END

    Y.NAT.ID = R.CUSTOMER<EB.CUS.LEGAL.ID>
    IF Y.POS.NAT.ID NE '' THEN
        IF R.CUSTOMER<EB.CUS.LOCAL.REF, Y.POS.NAT.ID> NE '' THEN
            Y.NAT.ID = R.CUSTOMER<EB.CUS.LOCAL.REF, Y.POS.NAT.ID>
        END
    END

*   --- 2. Doc CUSTOMER.ACCOUNT (key = ma KH) ---
    CALL F.READ(FN.CUST.ACCT, F.CUST.ACCT, Y.CUST.ID, R.CUST.ACCT, F.CA.ERR, '')
    IF F.CA.ERR THEN
        RETURN
    END

*   De bai yeu cau khong dung file layout CUSTOMER.ACCOUNT:
*   danh sach so tai khoan nam o attribute 1
    Y.NO.ACCT = DCOUNT(R.CUST.ACCT<1>, @VM)

    FOR Y.I = 1 TO Y.NO.ACCT
        Y.ACCT.NO = R.CUST.ACCT<1, Y.I>
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

        Y.OUT<-1> = Y.VN.NAME : @VM : Y.NAT.ID : @VM : Y.ACCT.NO : @VM : Y.WORK.BAL : @VM : Y.ACCT.OFF

NEXT.ACCT:
    NEXT Y.I

    RETURN
*------------------------------------------------------------------------------
END
