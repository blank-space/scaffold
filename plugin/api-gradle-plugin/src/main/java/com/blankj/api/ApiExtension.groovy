package com.blankj.api

class ApiExtension {

    boolean abortOnError = true
    String apiUtilsClass = "com.bsnl.base.utils.ApiUtils";
    String onlyScanLibRegex = ""
    String jumpScanLibRegex = ""

    @Override
    String toString() {
        return "ApiExtension { " +
                "abortOnError: " + abortOnError +
                ", apiUtilsClass: " + apiUtilsClass +
                (onlyScanLibRegex == "" ? "" : ", onlyScanLibRegex: " + onlyScanLibRegex) +
                (jumpScanLibRegex == "" ? "" : ", jumpScanLibRegex: " + jumpScanLibRegex) +
                " }";
    }
}
