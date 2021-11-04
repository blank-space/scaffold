package com.dawn.sample.pkg.feature.view.hexStatusManager;

/**
 * @author : LeeZhaoXing
 * @date : 2021/7/16
 * @desc :
 */
public class ModelManager {

    private boolean status1 = true;
    private boolean status2 = true;
    private boolean status3 = true;
    private boolean status4 = false;
    private boolean status5 = false;
    private boolean status6 = false;
    private boolean status7 = false;
    private boolean status8 = false;

    public void setModeA() {
        status1 = true;
        status2 = true;
        status3 = true;
        status4 = false;
        status5 = false;
        status6 = false;
        status7 = false;
        status8 = false;
    }

    public void setModeB() {
        status1 = true;
        status2 = false;
        status3 = false;
        status4 = true;
        status5 = true;
        status6 = true;
        status7 = false;
        status8 = false;
    }

    public void setModeC() {
        status1 = true;
        status2 = false;
        status3 = false;
        status4 = false;
        status5 = false;
        status6 = false;
        status7 = true;
        status8 = true;
    }


    private int STATUSES;

    private final int STATUS_1 = 0x0001;
    private final int STATUS_2 = 0x0002;
    private final int STATUS_3 = 0x0004;
    private final int STATUS_4 = 0x0008;
    private final int STATUS_5 = 0x0010;
    private final int STATUS_6 = 0x0020;
    private final int STATUS_7 = 0x0040;
    private final int STATUS_8 = 0x0080;

    private final int MODE_A = STATUS_1 | STATUS_2 | STATUS_3;
    private final int MODE_B = STATUS_1 | STATUS_4 | STATUS_5 | STATUS_6;
    private final int MODE_C = STATUS_1 | STATUS_7 | STATUS_8;


    public void change2ModelA(){
        STATUSES = MODE_A;
    }

    public void add() {
        STATUSES |= STATUS_1;
    }

    public void remove() {
        STATUSES &= ~STATUS_1;
    }

    public boolean isContain() {
        return (STATUSES & STATUS_1) != 0;
    }

    public int getSTATUSES() {
        return STATUSES;
    }
}
