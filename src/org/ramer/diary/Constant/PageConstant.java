package org.ramer.diary.Constant;

/**
 * @author ramer
 *
 */
public enum PageConstant {
  HOME("home"), USERINPUT("userInput"), SUCCESS("redirect:/success"), ERROR("redirect:/error");
  private String page;

  private PageConstant(String page) {
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