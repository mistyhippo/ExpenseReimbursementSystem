package com.revature.exceptions;

public class NotATeacherException extends Exception {

    public NotATeacherException(){
        super("Person assigned to class is not a teacher");
    }

    public NotATeacherException(String message){
        super(message);
    }

}
