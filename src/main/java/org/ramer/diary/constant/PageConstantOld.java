package org.ramer.diary.constant;

/**
 * @author ramer
 *
 */
public enum PageConstantOld {
  HOME("home"), USERINPUT("userInput"), SUCCESS("redirect:/success"), ERROR("redirect:/error");
  private String page;

  private PageConstantOld(String page) {
    this.page = page;
  }

  private String getPage() {
    return page;
  }

  @Override
  public String toString() {
    return this.getPage();
  }
}