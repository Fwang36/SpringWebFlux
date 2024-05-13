package com.example.hello;

public class Greeting {

  private String message;


  public Greeting() {
  }
  public Greeting(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getSecondMessage() {
	return "message";
	
  }

  @Override
  public String toString() {
    return "Greeting{" +
        "message='" + message + '\'' +
        '}';
  }
}