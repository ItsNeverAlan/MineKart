package me.chiss.Core.Utility;

import java.util.HashSet;
import me.chiss.Core.Modules.Utility;

public class UtilInput
  extends AUtility
{
  public UtilInput(Utility util)
  {
    super(util);
  }
  

  protected HashSet<Character> validSet = new HashSet();
  protected HashSet<String> filterSet = new HashSet();
  
  public boolean valid(String input)
  {
    if (this.validSet.isEmpty()) {
      addChars();
    }
    for (char cur : input.toCharArray()) {
      if (!this.validSet.contains(Character.valueOf(cur)))
        return false;
    }
    return true;
  }
  
  public String filter(String input)
  {
    if (this.filterSet.isEmpty()) {
      addDictionary();
    }
    for (String cur : this.filterSet)
    {
      if (input.equalsIgnoreCase(cur))
      {
        String out = input.charAt(0);
        while (out.length() < input.length())
          out = out + '*';
        return out;
      }
    }
    
    return input;
  }
  
  public void addDictionary()
  {
    this.filterSet.add("fuck");
    this.filterSet.add("shit");
    this.filterSet.add("cunt");
    this.filterSet.add("ass");
    this.filterSet.add("asshole");
    this.filterSet.add("faggot");
    this.filterSet.add("fag");
    this.filterSet.add("gay");
  }
  
  public void addChars()
  {
    this.validSet.add(Character.valueOf('1'));
    this.validSet.add(Character.valueOf('2'));
    this.validSet.add(Character.valueOf('3'));
    this.validSet.add(Character.valueOf('4'));
    this.validSet.add(Character.valueOf('5'));
    this.validSet.add(Character.valueOf('6'));
    this.validSet.add(Character.valueOf('7'));
    this.validSet.add(Character.valueOf('8'));
    this.validSet.add(Character.valueOf('9'));
    this.validSet.add(Character.valueOf('0'));
    
    this.validSet.add(Character.valueOf('a'));
    this.validSet.add(Character.valueOf('b'));
    this.validSet.add(Character.valueOf('c'));
    this.validSet.add(Character.valueOf('d'));
    this.validSet.add(Character.valueOf('e'));
    this.validSet.add(Character.valueOf('f'));
    this.validSet.add(Character.valueOf('g'));
    this.validSet.add(Character.valueOf('h'));
    this.validSet.add(Character.valueOf('i'));
    this.validSet.add(Character.valueOf('j'));
    this.validSet.add(Character.valueOf('k'));
    this.validSet.add(Character.valueOf('l'));
    this.validSet.add(Character.valueOf('m'));
    this.validSet.add(Character.valueOf('n'));
    this.validSet.add(Character.valueOf('o'));
    this.validSet.add(Character.valueOf('p'));
    this.validSet.add(Character.valueOf('q'));
    this.validSet.add(Character.valueOf('r'));
    this.validSet.add(Character.valueOf('s'));
    this.validSet.add(Character.valueOf('t'));
    this.validSet.add(Character.valueOf('u'));
    this.validSet.add(Character.valueOf('v'));
    this.validSet.add(Character.valueOf('w'));
    this.validSet.add(Character.valueOf('x'));
    this.validSet.add(Character.valueOf('y'));
    this.validSet.add(Character.valueOf('z'));
    
    this.validSet.add(Character.valueOf('A'));
    this.validSet.add(Character.valueOf('B'));
    this.validSet.add(Character.valueOf('C'));
    this.validSet.add(Character.valueOf('D'));
    this.validSet.add(Character.valueOf('E'));
    this.validSet.add(Character.valueOf('F'));
    this.validSet.add(Character.valueOf('G'));
    this.validSet.add(Character.valueOf('H'));
    this.validSet.add(Character.valueOf('I'));
    this.validSet.add(Character.valueOf('J'));
    this.validSet.add(Character.valueOf('K'));
    this.validSet.add(Character.valueOf('L'));
    this.validSet.add(Character.valueOf('M'));
    this.validSet.add(Character.valueOf('N'));
    this.validSet.add(Character.valueOf('O'));
    this.validSet.add(Character.valueOf('P'));
    this.validSet.add(Character.valueOf('Q'));
    this.validSet.add(Character.valueOf('R'));
    this.validSet.add(Character.valueOf('S'));
    this.validSet.add(Character.valueOf('T'));
    this.validSet.add(Character.valueOf('U'));
    this.validSet.add(Character.valueOf('V'));
    this.validSet.add(Character.valueOf('W'));
    this.validSet.add(Character.valueOf('X'));
    this.validSet.add(Character.valueOf('Y'));
    this.validSet.add(Character.valueOf('Z'));
  }
}
