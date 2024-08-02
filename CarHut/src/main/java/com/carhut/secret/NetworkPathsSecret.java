package com.carhut.secret;

public class NetworkPathsSecret {
    public final static String publicIPAddress = "http://localhost";
    public final static String publicWebAddress = publicIPAddress + ":3000";
    public final static String emailSender = "carhutsenderbot@gmail.com";
    public final static String successfulRegistrationVerificationAddress = publicIPAddress + ":3000/register/successfulVerification";
    public static String corsWebAddress = publicWebAddress;
}
