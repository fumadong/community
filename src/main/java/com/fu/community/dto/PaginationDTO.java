package com.fu.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {

    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;  //当前页
    private List<Integer> pages = new ArrayList<>(); //页码数列表
    private Integer totalPages;

    public void setPagination(Integer totalPages, Integer page) {
        this.totalPages = totalPages;
        this.page = page;
        //计算页面列表数 如 1 2 3 page 4 5 6
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= totalPages) {
                pages.add(page + i);
            }
        }

        //判断是否显示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }
        //判断是否显示下一页
        if (page == this.totalPages) {
            showNext = false;
        } else {
            showNext = true;
        }

        //判断是否显示首页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }
        //判断是否显示尾页
        if (pages.contains(totalPages)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }

    }
}
