$PACKAGE L3.VmbTemplateRoutines4
*------------------------------------------------------------------------------
* <Rating>456</Rating>
*------------------------------------------------------------------------------
*;* Purpose: Bai 1 - Tao bang thong tin khach hang
*------------------------------------------------------------------------------
SUBROUTINE HUNGNH.CUSTOMER.TEST
*------------------------------------------------------------------------------
    $INSERT I_COMMON
    $INSERT I_EQUATE
    $INSERT I_ENQUIRY.COMMON
    $INSERT I_GTS.COMMON
    $INSERT I_F.OFS.STATUS.FLAG
    $INSERT I_F.CURRENCY
*------------------------------------------------------------------------------
    GOSUB DEFINE.PARAMETERS

    IF LEN(V$FUNCTION) GT 1 THEN
        GOTO V$EXIT
    END

    CALL MATRIX.UPDATE

    GOSUB INITIALISE

    LOOP

        CALL RECORDID.INPUT

    UNTIL (MESSAGE EQ 'RET')

        V$ERROR = ''

        IF MESSAGE EQ 'NEW FUNCTION' THEN
            GOSUB CHECK.FUNCTION

            IF V$FUNCTION EQ 'E' OR V$FUNCTION EQ 'L' THEN
                CALL FUNCTION.DISPLAY
                V$FUNCTION = ''
            END

        END ELSE
            GOSUB CHECK.ID
            IF V$ERROR THEN GOTO MAIN.REPEAT

            CALL RECORD.READ

            IF MESSAGE EQ 'REPEAT' THEN
                GOTO MAIN.REPEAT
            END

            GOSUB CHECK.RECORD

            CALL MATRIX.ALTER

            IF V$ERROR THEN GOTO MAIN.REPEAT

            LOOP
                GOSUB PROCESS.FIELDS
                GOSUB PROCESS.MESSAGE
            WHILE (MESSAGE EQ 'ERROR') REPEAT

        END
*------------------------------------------------------------------------------
MAIN.REPEAT:
*------------------------------------------------------------------------------
    REPEAT

V$EXIT:
    RETURN
*------------------------------------------------------------------------------
*                      S u b r o u t i n e s                            *
*------------------------------------------------------------------------------
PROCESS.FIELDS:
*------------------------------------------------------------------------------
    LOOP
        IF SCREEN.MODE EQ 'MULTI' THEN
            IF FILE.TYPE EQ 'I' THEN
                CALL FIELD.MULTI.INPUT
            END ELSE
                CALL FIELD.MULTI.DISPLAY
            END
        END ELSE
            IF FILE.TYPE EQ 'I' THEN
                CALL FIELD.INPUT
            END ELSE
                CALL FIELD.DISPLAY
            END
        END

    WHILE NOT(MESSAGE)

        GOSUB CHECK.FIELDS

        IF T.SEQU NE '' THEN T.SEQU<-1> = A + 1

    REPEAT

    RETURN
*------------------------------------------------------------------------------
PROCESS.MESSAGE:
*------------------------------------------------------------------------------
    IF MESSAGE = 'DEFAULT' THEN
        MESSAGE = 'ERROR'
        IF V$FUNCTION <> 'D' AND V$FUNCTION <> 'R' THEN
            GOSUB CROSS.VALIDATION
        END
    END

    IF MESSAGE = 'PREVIEW' THEN
        MESSAGE = 'ERROR'
        IF V$FUNCTION <> 'D' AND V$FUNCTION <> 'R' THEN
            GOSUB CROSS.VALIDATION
        END
    END

    IF MESSAGE EQ 'VAL' THEN
        MESSAGE = ''
        BEGIN CASE
            CASE V$FUNCTION EQ 'D'
                GOSUB CHECK.DELETE
            CASE V$FUNCTION EQ 'R'
                GOSUB CHECK.REVERSAL
            CASE OTHERWISE
                GOSUB CROSS.VALIDATION
                IF NOT(V$ERROR) THEN
                    GOSUB OVERRIDES
                END
        END CASE
        IF NOT(V$ERROR) THEN
            GOSUB BEFORE.UNAU.WRITE
        END
        IF NOT(V$ERROR) THEN
            CALL UNAUTH.RECORD.WRITE
            IF MESSAGE NE "ERROR" THEN
                GOSUB AFTER.UNAU.WRITE
            END
        END

    END

    IF MESSAGE EQ 'AUT' THEN
        GOSUB AUTH.CROSS.VALIDATION
        IF NOT(V$ERROR) THEN
            GOSUB BEFORE.AUTH.WRITE
        END

        IF NOT(V$ERROR) THEN
            CALL AUTH.RECORD.WRITE

            IF MESSAGE NE "ERROR" THEN
                GOSUB AFTER.AUTH.WRITE
            END
        END

    END

    RETURN
*------------------------------------------------------------------------------
*                      Special Tailored Subroutines                     *
*------------------------------------------------------------------------------
CHECK.ID:
*------------------------------------------------------------------------------
    Y.ID = ID.NEW
    IF Y.ID[1,3] NE 'KHT' THEN
        ETEXT = "ID KHONG DUNG DINH DANG, 3 KI TU DAU PHAI LA: KHT"
        CALL STORE.END.ERROR
        RETURN
    END
    RETURN
*------------------------------------------------------------------------------
CHECK.RECORD:
*------------------------------------------------------------------------------
    IF OFS$STATUS<STAT.FLAG.FIRST.TIME> THEN

    END

    RETURN
*------------------------------------------------------------------------------
CHECK.FIELDS:
*------------------------------------------------------------------------------
    BEGIN CASE
        CASE A EQ ID.TYPE.POS
            Y.AV.VAL = AV
            IF Y.AV.VAL LT 1 THEN Y.AV.VAL = 1
            GOSUB VALIDATE.ID.TYPE

        CASE A EQ ID.NUMBER.POS
            Y.AV.VAL = AV
            IF Y.AV.VAL LT 1 THEN Y.AV.VAL = 1
            GOSUB VALIDATE.ID.NUMBER

        CASE A EQ PHONE.NUMBER.POS
            GOSUB VALIDATE.PHONE
    END CASE

    RETURN
*------------------------------------------------------------------------------
VALIDATE.ID.TYPE:
*------------------------------------------------------------------------------
    Y.ID.TYPE = R.NEW(ID.TYPE.POS)<1, Y.AV.VAL>

    IF Y.ID.TYPE NE '' THEN
        IF Y.ID.TYPE NE 'CMTND' AND Y.ID.TYPE NE 'CCCD' AND Y.ID.TYPE NE 'KHAC' THEN
            ETEXT = "LOAI GIAY TO CHI DUOC CHON: CMTND, CCCD, KHAC"
            CALL STORE.END.ERROR
        END
    END

    RETURN
*------------------------------------------------------------------------------
VALIDATE.ID.NUMBER:
*------------------------------------------------------------------------------
    Y.ID.TYPE = R.NEW(ID.TYPE.POS)<1, Y.AV.VAL>
    Y.ID.NUM  = R.NEW(ID.NUMBER.POS)<1, Y.AV.VAL>

    IF Y.ID.NUM NE '' THEN
        IF Y.ID.TYPE EQ '' THEN
            ETEXT = "PHAI NHAP LOAI GIAY TO TRUOC KHI NHAP SO GIAY TO"
            CALL STORE.END.ERROR
            RETURN
        END

        BEGIN CASE
            CASE Y.ID.TYPE EQ 'CMTND'
                IF LEN(Y.ID.NUM) NE 9 OR NUM(Y.ID.NUM) NE 1 THEN
                    ETEXT = "SO CMTND PHAI DU 9 KY TU SO"
                    CALL STORE.END.ERROR
                END
            CASE Y.ID.TYPE EQ 'CCCD'
                IF LEN(Y.ID.NUM) NE 12 OR NUM(Y.ID.NUM) NE 1 THEN
                    ETEXT = "SO CCCD PHAI DU 12 KY TU SO"
                    CALL STORE.END.ERROR
                END
        END CASE
    END

    RETURN
*------------------------------------------------------------------------------
VALIDATE.PHONE:
*------------------------------------------------------------------------------
    Y.PHONE = R.NEW(PHONE.NUMBER.POS)

    IF Y.PHONE NE '' THEN
        IF LEN(Y.PHONE) NE 10 OR NUM(Y.PHONE) NE 1 THEN
            ETEXT = "SO DIEN THOAI PHAI DU 10 KY TU SO"
            CALL STORE.END.ERROR
        END ELSE
            IF Y.PHONE[1,1] NE '0' THEN
                ETEXT = "SO DIEN THOAI PHAI BAT DAU BANG SO 0"
                CALL STORE.END.ERROR
            END
        END
    END

    RETURN
*------------------------------------------------------------------------------
CROSS.VALIDATION:
*------------------------------------------------------------------------------
    Y.NO.AV = DCOUNT(R.NEW(ID.TYPE.POS), @VM)
    IF Y.NO.AV LT 1 THEN Y.NO.AV = 1

    FOR Y.AV.VAL = 1 TO Y.NO.AV
        GOSUB VALIDATE.ID.TYPE
        IF V$ERROR THEN RETURN
        GOSUB VALIDATE.ID.NUMBER
        IF V$ERROR THEN RETURN
    NEXT Y.AV.VAL

    GOSUB VALIDATE.PHONE

    RETURN
*------------------------------------------------------------------------------
OVERRIDES:

    RETURN
*------------------------------------------------------------------------------
AUTH.CROSS.VALIDATION:

    RETURN
*------------------------------------------------------------------------------
CHECK.DELETE:

    RETURN
*------------------------------------------------------------------------------
CHECK.REVERSAL:

    RETURN
*------------------------------------------------------------------------------
DELIVERY.PREVIEW:

    RETURN
*------------------------------------------------------------------------------
BEFORE.UNAU.WRITE:
*------------------------------------------------------------------------------
    IF TEXT = "NO" THEN
        CALL TRANSACTION.ABORT
        V$ERROR = 1
        MESSAGE = "ERROR"
        RETURN
    END

    RETURN
*------------------------------------------------------------------------------
AFTER.UNAU.WRITE:

    RETURN
*------------------------------------------------------------------------------
AFTER.AUTH.WRITE:

    RETURN
*------------------------------------------------------------------------------
BEFORE.AUTH.WRITE:
*------------------------------------------------------------------------------
    BEGIN CASE
        CASE R.NEW(V-8)[1,3] = "INA"
        CASE R.NEW(V-8)[1,3] = "RNA"
    END CASE

    RETURN
*------------------------------------------------------------------------------
CHECK.FUNCTION:
*------------------------------------------------------------------------------
    IF INDEX('V',V$FUNCTION,1) THEN
        E = 'EB.RTN.FUNT.NOT.ALLOWED.APP'
        CALL ERR
        V$FUNCTION = ''
    END

    RETURN
*------------------------------------------------------------------------------
INITIALISE:

    RETURN
*------------------------------------------------------------------------------
DEFINE.PARAMETERS:
*------------------------------------------------------------------------------
    MAT F = "" ; MAT N = "" ; MAT T = ""
    MAT CHECKFILE = "" ; MAT CONCATFILE = ""
    ID.CHECKFILE = "" ; ID.CONCATFILE = ""

    ID.F  = "@ID"; ID.N  = "10"; ID.T  = "A"
    C$NS.OPERATION = 'ALL'

    Z = 0

    Z+=1 ; F(Z) = "CUSTOMER.NAME" ; N(Z) = "200..C" ; T(Z) = "A"

    Z+=1 ; F(Z) = "XX.ID.TYPE" ; N(Z) = "10" ; T(Z) = "A"
    ID.TYPE.POS = Z

    Z+=1 ; F(Z) = "XX.ID.NUMBER" ; N(Z) = "12" ; T(Z) = "A"
    ID.NUMBER.POS = Z

    Z+=1 ; F(Z) = "ID.ISSUE.DATE" ; N(Z) = "8" ; T(Z) = "D"

    Z+=1 ; F(Z) = "PLACE.OF.BIRTH" ; N(Z) = "200..C" ; T(Z) = "A"
    CHECKFILE(Z) = "COUNTRY":@FM:EB.CUR.CCY.NAME:@FM:"L"

    Z+=1 ; F(Z) = "PHONE.NUMBER" ; N(Z) = "10" ; T(Z) = "A"
    PHONE.NUMBER.POS = Z

    Z += 1; F(Z) = "XX.RESERVED.7"; N(Z)= "35"; T(Z)<3> = "NOINPUT"
    Z += 1; F(Z) = "XX.RESERVED.8"; N(Z)= "35"; T(Z)<3> = "NOINPUT"
    Z += 1; F(Z) = "XX.LOCAL.REF"; N(Z) = "5"; T(Z)<1>="A";

    V=Z+9

    PREFIX = "KHT"

    RETURN
*------------------------------------------------------------------------------
END
