package com.blankj.bus;

class BusExtension {

    boolean abortOnError = true;
    String busUtilsClass = "com.bsnl.base.utils.BusUtils";
    String onlyScanLibRegex = ""
    String jumpScanLibRegex = ""

    @Override
    String toString() {
        return "BusExtension { " +
                "abortOnError: " + abortOnError +
                ", busUtilsClass: " + busUtilsClass +
                (onlyScanLibRegex == "" ? "" : ", onlyScanLibRegex: " + onlyScanLibRegex) +
                (jumpScanLibRegex == "" ? "" : ", jumpScanLibRegex: " + jumpScanLibRegex) +
                " }";
    }
}
