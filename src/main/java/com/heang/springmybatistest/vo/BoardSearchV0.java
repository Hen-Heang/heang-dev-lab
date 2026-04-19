package com.heang.springmybatistest.vo;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class BoardSearchV0 {

    private int currentPage = 1;
    private int pageSize = 10;
    private String keyword = "";
    private String searchType = "title";

    public int getOffset(){
        return (currentPage - 1) * pageSize;
    }

}
