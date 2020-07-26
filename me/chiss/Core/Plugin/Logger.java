package me.chiss.Core.Plugin;

import java.io.PrintStream;

public class Logger implements ILogger {
  public void Log(String source, String message) {
    System.out.println("[" + source + "]" + " " + message);
  }
}
