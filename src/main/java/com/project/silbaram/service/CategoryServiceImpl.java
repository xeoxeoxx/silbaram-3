package com.project.silbaram.service;

import com.project.silbaram.dao.CategoryDAO;
import com.project.silbaram.dto.CategoryDTO;
import com.project.silbaram.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService{
    private final CategoryDAO categoryDAO;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryDTO> selectAll() {
        return categoryDAO.selectAll();
    }

    @Override
    public CategoryDTO getById(Long cid){
        CategoryVO categoryVO = categoryDAO.getById(cid);
        log.info(categoryVO);
        CategoryDTO categoryDTO = modelMapper.map(categoryVO, CategoryDTO.class);
        log.info(categoryDTO);

        return categoryDTO;
    }

}
