/*
 * User: levon
 * Date: 25.03.2023
 * Time: 16:09
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
  public static final int backpackMaxWeight = 4;
  public static final List<Item> items = new ArrayList<>(){{
    add(new Item("Guitar", 1500, 1));
    add(new Item("Tape recorder", 3000, 4));
    add(new Item("Laptop", 2000, 3));
    add(new Item("IPhone", 2000, 1));
  }};

  public static void main(String[] args) {
    System.out.println("backpack: " + backpackMaxWeight);
    System.out.println("items: " + items.size());

    final List<List<Set<Item>>> dp = new ArrayList<>();
    for (int i = 0; i < items.size(); i++) {
      dp.add(new ArrayList<>()); // row

      Item item = items.get(i); // item for current row

      for (int w = 0; w < backpackMaxWeight + 1; w++) {
        Set<Item> pos = new HashSet<>(); // col
        dp.get(i).add(pos); // cell[i][j]

        // Can we put current item to backpack?
        if (item.weight() <= w) {
          // Calc sum what was last time
          int sumLastTime = i > 0 ? sumPrice(dp.get(i - 1).get(w)) : 0;
          // Calc sum item + sum best set of items what was last time
          int sumItemPlus = item.price() + (i > 0 ? sumPrice(dp.get(i - 1).get(w - item.weight())) : 0);
          
          if (sumLastTime > sumItemPlus) {
            Set<Item> t = dp.get(i - 1).get(w);
            pos.addAll(t);
          } else {
            pos.add(item);
            if (i > 0)
              pos.addAll(dp.get(i - 1).get(w - item.weight()));
          }
        } else {
          if (i > 0)
            pos.addAll(dp.get(i - 1).get(w));
        }
      }
    }

    System.out.println("dp");
    for (int i = 0; i < items.size(); i++) {
      System.out.println(i + ": " + dp.get(i));
    }

    System.out.println("best case");
    for (Item item : dp.get(items.size() - 1).get(backpackMaxWeight)) {
      System.out.println(item);
    }
    System.out.println("sum: " + sumPrice(dp.get(items.size() - 1).get(backpackMaxWeight)));
  }

  /**
   * Calculation sum of items
   * @param items items
   * @return sum
   */
  public static int sumPrice(Set<Item> items) {
    if (items == null)
      return 0;

    int sum = 0;
    for (Item item : items) {
      sum += item.price();
    }
    return sum;
  }
}