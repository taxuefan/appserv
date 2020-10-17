package com.hs.edu.db.core;

import com.hs.edu.db.util.Constants;
import java.util.List;
/**
 * @Author taxuefan
 * @Description //用来处理代码分页的
 * @Date 11:09 2020/9/30
 * @Param
 * @return
 **/
public class PageHolder<T> {
    private int pageSize;
    private int totalCount;
    private int currentPage;
    private int startIndex;
    private int[] indexes = new int[0];
    private int nextIndex;
    private int previousIndex;
    private int pageCount;
    private List<T> items;
    private int lastIndex;

    public <T> PageHolder(){}

    public <T> PageHolder(List<T> items, int totalCount, int startIndex) {
        pageSize = Constants.PAGE_SIZE;
        setPageSize(pageSize);
        setTotalCount(totalCount);
        setItems(items);
        setStartIndex(startIndex);
    }

    public void setTotalCount(int totalCount) {
        if (totalCount > 0) {
            this.totalCount = totalCount;
            int count = totalCount / pageSize;
            if (totalCount % pageSize > 0) {
                count++;
            }
            indexes = new int[count];
            for (int i = 0; i < count; i++) {
                indexes[i] = pageSize * i;
            }
        } else {
            this.totalCount = 0;
        }
    }

    /**
     * 得到总记录数
     *
     * @return
     */
    public int getTotalCount() {
        return totalCount;
    }

    public void setIndexes(int[] indexes) {
        this.indexes = indexes;
    }

    /**
     * 得到分页索引的数组
     *
     * @return
     */
    public int[] getIndexes() {
        return indexes;
    }

    public void setStartIndex(int startIndex) {
        if (totalCount <= 0) {
            this.startIndex = 0;
        } else if (startIndex >= totalCount) {
            this.startIndex = indexes[indexes.length - 1];
        } else if (startIndex < 0) {
            this.startIndex = 0;
        } else {
            this.startIndex = indexes[startIndex / pageSize];
        }
    }

    /**
     * 当前页
     *
     * @return
     */
    public int getStartIndex() {
        return startIndex;
    }

    public void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }
    /**
     * 下一页
     *
     * @return
     */
    public int getNextIndex() {
        int nextIndex = getStartIndex() + pageSize;
        if (nextIndex >= totalCount) {
            return getStartIndex();
        } else {
            return nextIndex;
        }
    }

    public void setPreviousIndex(int previousIndex) {
        this.previousIndex = previousIndex;
    }

    /**
     * 上一页
     *
     * @return
     */
    public int getPreviousIndex() {
        int previousIndex = getStartIndex() - pageSize;
        if (previousIndex < 0) {
            return 0;
        } else {
            return previousIndex;
        }
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageCount() {
        int count = totalCount / pageSize;
        if (totalCount % pageSize > 0)
            count++;
        return count;
    }

    public int getCurrentPage() {
        return getStartIndex() / pageSize + 1;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int getLastIndex() {
        if (indexes.length == 0) {
            return 0;
        } else {
            return indexes[indexes.length - 1];
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    /**
     * 得到已分页好的结果集
     *
     * @return
     */
    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}
