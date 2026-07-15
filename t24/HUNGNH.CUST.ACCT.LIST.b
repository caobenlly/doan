*------------------------------------------------------------------------------
* BAI 4 - CHUONG TRINH DANH SACH TAI KHOAN KHACH HANG
* Input  : nhap ma khach hang tren CRT
* Output : VN.FULL.NAME, NATIONAL.ID, ACCOUNT, WORKING.BALANCE,
*          ACCOUNT.OFFICER
* Logic  : CUSTOMER -> CUSTOMER.ACCOUNT -> ACCOUNT
*------------------------------------------------------------------------------
PROGRAM HUNGNH.CUST.ACCT.LIST
*------------------------------------------------------------------------------
    $INSERT I_COMMON
    $INSERT I_EQUATE
    $INSERT I_F.CUSTOMER
    $INSERT I_F.ACCOUNT

    GOSUB INITIALISE
    GOSUB INPUT.DATA
    GOSUB PROCESS

    STOP

*------------------------------------------------------------------------------
INITIALISE:
*------------------------------------------------------------------------------
    FN.CUSTOMER = 'F.CUSTOMER'
    F.CUSTOMER = ''
    CALL OPF(FN.CUSTOMER, F.CUSTOMER)

*   Theo de bai: CUSTOMER.ACCOUNT khong dung file layout
    FN.CUST.ACCT = 'F.CUSTOMER.ACCOUNT'
    F.CUST.ACCT = ''
    CALL OPF(FN.CUST.ACCT, F.CUST.ACCT)

    FN.ACCOUNT = 'F.ACCOUNT'
    F.ACCOUNT = ''
    CALL OPF(FN.ACCOUNT, F.ACCOUNT)

    Y.POS.VN.NAME = ''
    Y.POS.NAT.ID = ''

    CALL GET.LOC.REF('CUSTOMER', 'VN.FULL.NAME', Y.POS.VN.NAME)
    CALL GET.LOC.REF('CUSTOMER', 'NATIONAL.ID', Y.POS.NAT.ID)

    RETURN

*------------------------------------------------------------------------------
INPUT.DATA:
*------------------------------------------------------------------------------
    CRT
    CRT '=============================================================='
    CRT ' DANH SACH TAI KHOAN CUA KHACH HANG'
    CRT '=============================================================='
    CRT 'NHAP MA KHACH HANG: ':
    INPUT Y.CUST.ID

    RETURN

*------------------------------------------------------------------------------
PROCESS:
*------------------------------------------------------------------------------
    IF Y.CUST.ID EQ '' THEN
        CRT 'MA KHACH HANG KHONG DUOC DE TRONG'
        RETURN
    END

*   1. Doc CUSTOMER
    R.CUSTOMER = ''
    F.CUS.ERR = ''
    CALL F.READ(FN.CUSTOMER, F.CUSTOMER, Y.CUST.ID, R.CUSTOMER, F.CUS.ERR, '')

    IF F.CUS.ERR THEN
        CRT 'KHONG TIM THAY KHACH HANG: ':Y.CUST.ID
        RETURN
    END

*   Lay VN.FULL.NAME, neu khong co thi dung SHORT.NAME
    Y.VN.NAME = R.CUSTOMER<EB.CUS.SHORT.NAME>
    IF Y.POS.VN.NAME NE '' THEN
        IF R.CUSTOMER<EB.CUS.LOCAL.REF, Y.POS.VN.NAME> NE '' THEN
            Y.VN.NAME = R.CUSTOMER<EB.CUS.LOCAL.REF, Y.POS.VN.NAME>
        END
    END

*   Lay NATIONAL.ID, neu khong co thi dung LEGAL.ID
    Y.NAT.ID = R.CUSTOMER<EB.CUS.LEGAL.ID>
    IF Y.POS.NAT.ID NE '' THEN
        IF R.CUSTOMER<EB.CUS.LOCAL.REF, Y.POS.NAT.ID> NE '' THEN
            Y.NAT.ID = R.CUSTOMER<EB.CUS.LOCAL.REF, Y.POS.NAT.ID>
        END
    END

*   2. Doc CUSTOMER.ACCOUNT theo ma khach hang
    R.CUST.ACCT = ''
    F.CA.ERR = ''
    CALL F.READ(FN.CUST.ACCT, F.CUST.ACCT, Y.CUST.ID, R.CUST.ACCT, F.CA.ERR, '')

    IF F.CA.ERR THEN
        CRT 'KHACH HANG KHONG CO TAI KHOAN'
        RETURN
    END

*   CUSTOMER.ACCOUNT khong co layout: danh sach tai khoan o attribute 1
    Y.NO.ACCT = DCOUNT(R.CUST.ACCT<1>, @VM)

    CRT
    CRT 'TEN KH             : ':Y.VN.NAME
    CRT 'NATIONAL.ID        : ':Y.NAT.ID
    CRT
    CRT 'SO TAI KHOAN       SO DU                  ACCOUNT OFFICER'
    CRT '------------------  ---------------------  ---------------'

    Y.FOUND = 0

    FOR Y.I = 1 TO Y.NO.ACCT
        Y.ACCT.NO = R.CUST.ACCT<1, Y.I>

        IF Y.ACCT.NO NE '' THEN
*           3. Doc ACCOUNT de lay so du va account officer
            R.ACCOUNT = ''
            F.ACC.ERR = ''
            CALL F.READ(FN.ACCOUNT, F.ACCOUNT, Y.ACCT.NO, R.ACCOUNT, F.ACC.ERR, '')

            IF F.ACC.ERR EQ '' THEN
                Y.WORK.BAL = R.ACCOUNT<AC.WORKING.BALANCE>
                Y.ACCT.OFF = R.ACCOUNT<AC.ACCOUNT.OFFICER>

                CRT FMT(Y.ACCT.NO, '18L') : '  ' :
                    FMT(Y.WORK.BAL, '21R') : '  ' : Y.ACCT.OFF

                Y.FOUND = 1
            END
        END
    NEXT Y.I

    IF Y.FOUND EQ 0 THEN
        CRT 'KHONG TIM THAY TAI KHOAN HOP LE'
    END

    RETURN

*------------------------------------------------------------------------------
END
