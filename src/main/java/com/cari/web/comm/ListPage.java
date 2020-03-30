/*
 * 创建日期 2005-12-29
 * 林良益 @caripower
 */
package com.cari.web.comm;

import java.util.List;
/**
 * @author linliangyi@team of miracle
 *
 * 创建日期 2005-12-29
 * 
 * 基于List的数据页对象
 */

public class ListPage {

    public static final ListPage EMPTY_PAGE = new ListPage();

    //页面记录数
    private int currentPageSize = 10;
    //总记录数
    private long totalSize;
    //当前页页码
    private int currentPageNo;
    //当前页记录列表
    private List dataList;
    
    public ListPage() {
    }    
    /**
     * @return 返回 currentPageno。
     */
    public int getCurrentPageNo() {
        return currentPageNo;
    }
    /**
     * @param currentPageno 要设置的 currentPageno。
     */
    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }
    /**
     * @return 返回 currentPageSize。
     */
    public int getCurrentPageSize() {
        return currentPageSize;
    }
    /**
     * @param currentPageSize 要设置的 currentPageSize。
     */
    public void setCurrentPageSize(int currentPageSize) {
        this.currentPageSize = currentPageSize;
    }
    /**
     * @return 返回 dataList。
     */
    public List getDataList() {
        return dataList;
    }
    /**
     * @param dataList 要设置的 dataList。
     */
    public void setDataList(List dataList) {
        this.dataList = dataList;
    }
    /**
     * @return 返回 totalSize。
     */
    public long getTotalSize() {
        return totalSize;
    }
    /**
     * @param totalSize 要设置的 totalSize。
     */
    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }
    
    
    public long getTotalPageCount(){
     	return (getTotalSize() - 1) / getCurrentPageSize() + 1 ;
    }
    
    /**
     * 是否有下一页
     * @return 是否有下一页
     */
    public boolean hasNextPage() {
      return (this.getCurrentPageNo() < this.getTotalPageCount());
    }

    /**
     * 是否有上一页
     * @return  是否有上一页
     */
    public boolean hasPreviousPage() {
      return (this.getCurrentPageNo() > 1);
    }
    
    /**
     * 判断是否为空页
     * @return 是否为空页
     */
    public boolean isEmpty(){
        return this == ListPage.EMPTY_PAGE;
    }
    
}
