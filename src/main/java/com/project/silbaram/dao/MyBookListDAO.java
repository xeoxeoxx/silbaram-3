package com.project.silbaram.dao;


import com.project.silbaram.dto.PageRequestDTO;
import com.project.silbaram.vo.OrderInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyBookListDAO {

    void insertOrder(OrderInfoVO orderInfoVO);

    // 전체조회
    List<OrderInfoVO> selectAllMyBooks(PageRequestDTO PageRequestDTO, Long memberId);

    int getCount(PageRequestDTO pageRequestDTO);

}
