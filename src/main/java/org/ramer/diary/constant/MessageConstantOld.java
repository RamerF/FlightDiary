package org.ramer.diary.constant;

public enum MessageConstantOld {
  ERRORMESSAGE("非法操作,请以消费者的身份使用本系统"), SUCCESSMESSAGE("操作成功"), WRONGFORMAT(
      "数据格式错误"), SUCCESSCHANGEPASS("修改密码成功"), NOPICMESSAGE("必须要有图片哦");
  private String message;

  private MessageConstantOld(String message) {
    this.message = message;
  }

  private String getMessage() {
    return this.message;
  }

  @Override
  public String toString() {
    return getMessage();
  }

}
