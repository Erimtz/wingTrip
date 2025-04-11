package com.wingtrip.user.exception;

public class UsernameNotFoundException extends Exception {

    public UsernameNotFoundException(MessageCode exp) {
        super(exp.getMsg());
    }
}
