package com.project.silbaram.dao;

import com.project.silbaram.dto.CategoryDTO;
import com.project.silbaram.vo.CategoryVO;

import java.util.List;

public interface CategoryDAO {
    List<CategoryDTO> selectAll();
    CategoryVO getById(Long cid);
}
