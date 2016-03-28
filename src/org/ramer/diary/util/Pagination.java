package org.ramer.diary.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类:
 *  使用数组解决取出的用户id不连续问题
 *  将取出的用户数据放入数组中,就可以通过数组的下标直接获取.
 * @author ramer
 *
 */
@SuppressWarnings("unused")
public class Pagination<T> {
  //  总记录数
  private int totalNumber;
  //  下一页
  private int nextPage;
  //  上一页
  private int lastPage;
  //  是否还有下一页
  private boolean hasNext;
  //  是否有上一页
  private boolean hasLast;
  //  当前页号
  private int pageNum;
  //  每页大小
  private int pageSize;

  //  用于存放满足条件的用户信息
  private List<T> content;

  public Pagination(List<T> ts, int page, int size) {
    this.pageNum = page;
    this.pageSize = size;
    this.totalNumber = ts.size();
    content = getSpecificaion(ts, getPageNum(), getPageSize());
  }

  public List<T> getContent() {
    return content;
  }

  public int getNextPage() {
    double totalPageD = Math.ceil(totalNumber / pageSize);
    int totalPage = (int) totalPageD;
    if (pageNum >= totalPage) {
      return totalPage;
    }
    return totalPage + 1;
  }

  public int getLastPage() {
    double totalPageD = Math.ceil(totalNumber / pageSize);
    int totalPage = (int) totalPageD;
    if (pageNum <= 0) {
      return 0;
    }
    return totalPage - 1;
  }

  public boolean getHasNext() {
    return pageNum < Math.ceil(totalNumber / pageSize) ? true : false;
  }

  public boolean getHasLast() {
    return pageNum > 1 ? true : false;
  }

  public void setPageNum(int pageNum) {
    this.pageNum = pageNum;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPageNum() {
    double totalPageD = Math.ceil(totalNumber / pageSize);
    int totalPage = (int) totalPageD;
    return pageNum <= 0 ? 1 : pageNum >= totalPage ? totalPage : pageNum;
  }

  public int getPageSize() {
    return pageSize;
  }

  // 将获取到的不连续的信息,临时放入到数组中
  private List<T> getSpecificaion(List<T> ts, int page, int size) {
    List<T> tempList = new ArrayList<>();
    @SuppressWarnings("unchecked")
    T[] tArray = (T[]) ts.toArray();
    page = page - 1;
    for (int i = page * size; i < (page + 1) * size; i++) {
      tempList.add(tArray[i]);
    }
    return tempList;
  }

}
